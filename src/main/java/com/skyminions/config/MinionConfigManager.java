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
    private final Map<String, MinionConfig> minionConfigs = new HashMap<>();

    public MinionConfigManager(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
        loadMinions();
    }

    public void loadMinions() {
        minionConfigs.clear();
        File file = new File(plugin.getDataFolder(), "minions.yml");
        if (!file.exists()) {
            plugin.saveResource("minions.yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!config.contains("minions")) return;

        for (String key : config.getConfigurationSection("minions").getKeys(false)) {
            String path = "minions." + key + ".";
            String displayName = config.getString(path + "display-name", "&a" + key + " Minion");
            Material resourceMat = Material.valueOf(config.getString(path + "resource-material", "COBBLESTONE"));
            Material toolMat = Material.valueOf(config.getString(path + "tool-material", "DIAMOND_PICKAXE"));
            String headTexture = config.getString(path + "head-texture", "");
            String colorHex = config.getString(path + "armor-color", "#191919");
            int shopSlot = config.getInt(path + "shop-slot", 10);

            Color color = parseHexColor(colorHex);
            minionConfigs.put(key.toUpperCase(), new MinionConfig(key.toUpperCase(), displayName, resourceMat, toolMat, headTexture, color, shopSlot));
        }
    }

    public MinionConfig getMinionConfig(String type) {
        return minionConfigs.get(type.toUpperCase());
    }

    public Map<String, MinionConfig> getAllConfigs() {
        return minionConfigs;
    }

    private Color parseHexColor(String hex) {
        try {
            java.awt.Color javaColor = java.awt.Color.decode(hex);
            return Color.fromRGB(javaColor.getRed(), javaColor.getGreen(), javaColor.getBlue());
        } catch (Exception e) {
            return Color.fromRGB(25, 25, 25);
        }
    }
    }
          
