package com.skyminions.config;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.models.MinionConfig;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MinionConfigManager {

    private final SkyMinionsPlugin plugin;
    private final Map<String, MinionConfig> configs = new HashMap<>();

    public MinionConfigManager(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfigs() {
        configs.clear();
        File file = new File(plugin.getDataFolder(), "minions.yml");
        if (!file.exists()) {
            plugin.saveResource("minions.yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (config.contains("minions")) {
            for (String key : config.getConfigurationSection("minions").getKeys(false)) {
                String path = "minions." + key + ".";
                String displayName = config.getString(path + "display-name", key + " Minion");
                
                Material resourceMat = Material.matchMaterial(config.getString(path + "resource-material", "COBBLESTONE"));
                Material toolMat = Material.matchMaterial(config.getString(path + "tool-material", "DIAMOND_PICKAXE"));
                Material fallbackMat = Material.matchMaterial(config.getString(path + "held-item-fallback", "STONE_PICKAXE"));
                
                String texture = config.getString(path + "head-texture", "");
                
                // Parse Color
                String colorHex = config.getString(path + "armor-color", "#191919");
                Color armorColor = Color.fromRGB(0, 150, 255);
                try {
                    java.awt.Color awtColor = java.awt.Color.decode(colorHex);
                    armorColor = Color.fromRGB(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());
                } catch (Exception ignored) {}

                int shopSlot = config.getInt(path + "shop-slot", 10);

                MinionConfig minionConfig = new MinionConfig(
                        key.toUpperCase(), displayName, resourceMat, toolMat, fallbackMat, texture, armorColor, shopSlot
                );
                configs.put(key.toUpperCase(), minionConfig);
            }
        }
    }

    public MinionConfig getMinionConfig(String type) {
        return configs.get(type.toUpperCase());
    }

    public Map<String, MinionConfig> getAllConfigs() {
        return configs;
    }
}
