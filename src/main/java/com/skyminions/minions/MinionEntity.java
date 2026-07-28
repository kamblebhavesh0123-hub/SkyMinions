package com.skyminions.minions;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.models.Minion;
import com.skyminions.models.MinionConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class MinionEntity {

    public static ArmorStand spawnMinionStand(Minion minion) {
        Location loc = minion.getLocation();
        if (loc == null || loc.getWorld() == null) return null;

        ArmorStand stand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        stand.setSmall(true);
        stand.setArms(true);
        stand.setBasePlate(false);
        stand.setGravity(false);
        stand.setCanPickupItems(false);
        stand.setCustomNameVisible(true);
        stand.setInvulnerable(true);
        stand.setPersistent(true);

        SkyMinionsPlugin plugin = SkyMinionsPlugin.getPlugin(SkyMinionsPlugin.class);
        MinionConfig config = plugin.getConfigManager().getMinionConfig(minion.getType());

        // Set Head Texture
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (config != null && config.getHeadTexture() != null && !config.getHeadTexture().isEmpty()) {
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            if (meta != null) {
                PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
                PlayerTextures textures = profile.getTextures();
                try {
                    textures.setSkin(new URL(config.getHeadTexture()));
                } catch (MalformedURLException ignored) {}
                profile.setTextures(textures);
                meta.setOwnerProfile(profile);
                head.setItemMeta(meta);
            }
        }
        stand.getEquipment().setHelmet(head);

        // Set Leather Armor
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta armorMeta = (LeatherArmorMeta) chestplate.getItemMeta();
        if (armorMeta != null && config != null && config.getChestplateColor() != null) {
            armorMeta.setColor(config.getChestplateColor());
            chestplate.setItemMeta(armorMeta);
        }
        stand.getEquipment().setChestplate(chestplate);

        // Set Main Hand Tool
        Material toolMat = config != null ? config.getToolMaterial() : Material.DIAMOND_PICKAXE;
        if (toolMat == null && config != null) toolMat = config.getHeldItemFallback();
        stand.getEquipment().setItemInMainHand(new ItemStack(toolMat != null ? toolMat : Material.DIAMOND_PICKAXE));

        // Set Live Name Tag
        updateNameTag(minion, stand);

        return stand;
    }

    public static void updateNameTag(Minion minion, ArmorStand stand) {
        boolean full = minion.getStorage().isFull();
        NamedTextColor storageColor = full ? NamedTextColor.RED : NamedTextColor.GRAY;

        Component line1 = Component.text(minion.getType() + " Minion ", NamedTextColor.AQUA, TextDecoration.BOLD)
                .append(Component.text("[Lv." + minion.getLevel() + "]", NamedTextColor.YELLOW));

        Component line2 = Component.text("Storage: " + minion.getStorage().getStoredAmount() + "/" + minion.getStorage().getCapacity(), storageColor);

        if (full) {
            line2 = line2.append(Component.text(" (FULL!)", NamedTextColor.RED, TextDecoration.BOLD));
        }

        stand.customName(line1.append(Component.text(" - ")).append(line2));
    }

    public static void playCollectAnimation(SkyMinionsPlugin plugin, ArmorStand stand) {
        EulerAngle restingPose = new EulerAngle(0, 0, 0);
        EulerAngle swingPose = new EulerAngle(Math.toRadians(-60), 0, 0);

        stand.setRightArmPose(swingPose);

        new BukkitRunnable() {
            @Override
            public void run() {
                stand.setRightArmPose(restingPose);
            }
        }.runTaskLater(plugin, 4L);
    }

    public static void playParticleFeedback(Location loc, boolean success) {
        if (loc == null || loc.getWorld() == null) return;

        if (success) {
            loc.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, loc.clone().add(0, 1.2, 0), 5, 0.2, 0.2, 0.2);
        } else {
            loc.getWorld().spawnParticle(Particle.SMOKE, loc.clone().add(0, 1.2, 0), 8, 0.2, 0.2, 0.2);
        }
    }
            }
            
