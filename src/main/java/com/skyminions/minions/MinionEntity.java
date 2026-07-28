package com.skyminions.minions;

import com.skyminions.models.Minion;
import com.skyminions.models.MinionConfig;
import com.skyminions.SkyMinionsPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

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

        Component customName = Component.text(minion.getType() + " Minion ", NamedTextColor.AQUA)
                .append(Component.text("[Lv." + minion.getLevel() + "]", NamedTextColor.GRAY));
        stand.customName(customName);

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

        // Set Leather Chestplate
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta armorMeta = (LeatherArmorMeta) chestplate.getItemMeta();
        if (armorMeta != null) {
            armorMeta.setColor(Color.fromRGB(0, 150, 255));
            chestplate.setItemMeta(armorMeta);
        }
        stand.getEquipment().setChestplate(chestplate);

        // Set Main Hand Tool
        Material toolMat = config != null ? config.getToolMaterial() : Material.DIAMOND_PICKAXE;
        stand.getEquipment().setItemInMainHand(new ItemStack(toolMat));

        return stand;
    }
            }
            
