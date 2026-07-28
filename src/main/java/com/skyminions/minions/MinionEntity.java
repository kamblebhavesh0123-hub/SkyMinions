package com.skyminions.minions;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.models.Minion;
import com.skyminions.models.MinionConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
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

        SkyMinionsPlugin plugin = SkyMinionsPlugin.getPlugin(SkyMinionsPlugin.class);
        MinionConfig config = plugin.getConfigManager().getMinionConfig(minion.getType());

        ArmorStand stand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        stand.setGravity(false);
        stand.setArms(true);
        stand.setBasePlate(false);
        stand.setSmall(true);
        stand.setCustomNameVisible(true);

        Component customName = Component.text(minion.getType() + " Minion ", NamedTextColor.AQUA)
                .append(Component.text("[Lv." + minion.getLevel() + "]", NamedTextColor.GRAY));
        stand.customName(customName);

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        if (headMeta != null) {
            String url = (config != null && !config.getHeadTexture().isEmpty()) 
                    ? config.getHeadTexture() 
                    : "http://textures.minecraft.net/texture/e839d7b6aab7c45c23b1cc82b37742a7c7185d5d43a35bd5f87f57ddfa3";
            setSkullTexture(headMeta, url);
            head.setItemMeta(headMeta);
        }
        stand.setItem(EquipmentSlot.HEAD, head);

        ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta chestMeta = (LeatherArmorMeta) chest.getItemMeta();
        if (chestMeta != null) {
            chestMeta.setColor(config != null ? config.getArmorColor() : Color.fromRGB(25, 25, 25));
            chest.setItemMeta(chestMeta);
        }
        stand.setItem(EquipmentSlot.CHEST, chest);

        Material toolMat = config != null ? config.getToolMaterial() : Material.DIAMOND_PICKAXE;
        stand.setItem(EquipmentSlot.HAND, new ItemStack(toolMat));

        return stand;
    }

    private static void setSkullTexture(SkullMeta meta, String textureUrl) {
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();
        try {
            textures.setSkin(new URL(textureUrl));
        } catch (MalformedURLException ignored) {}
        profile.setTextures(textures);
        meta.setOwnerProfile(profile);
    }
            }
                
