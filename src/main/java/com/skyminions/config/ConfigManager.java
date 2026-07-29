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

public class ConfigManager {

    private final SkyMinionsPlugin plugin;
    private FileConfiguration guiConfig;
    private File guiConfigFile;
    private FileConfiguration upgradeConfig;
    private File upgradeConfigFile;
    private final Map<String, MinionConfig> minionConfigs = new HashMap<>();

    public ConfigManager(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
        loadConfigs();
    }

    public void loadConfigs() {
        plugin.reloadConfig();

        guiConfigFile = new File(plugin.getDataFolder(), "gui.yml");
        if (!guiConfigFile.exists()) {
            plugin.saveResource("gui.yml", false);
        }
        guiConfig = YamlConfiguration.loadConfiguration(guiConfigFile);

        upgradeConfigFile = new File(plugin.getDataFolder(), "upgrades.yml");
        if (!upgradeConfigFile.exists()) {
            plugin.saveResource("upgrades.yml", false);
        }
        upgradeConfig = YamlConfiguration.loadConfiguration(upgradeConfigFile);

        // Load minion-specific configs and map them to MinionConfig models
        minionConfigs.clear();
        File minionsDir = new File(plugin.getDataFolder(), "minions");
        if (minionsDir.exists() && minionsDir.isDirectory()) {
            File[] files = minionsDir.listFiles((dir, name) -> name.endsWith(".yml"));
            if (files != null) {
                for (File file : files) {
                    String typeName = file.getName().replace(".yml", "").toLowerCase();
                    FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                    MinionConfig minionConfig = parseMinionConfig(typeName, cfg);
                    if (minionConfig != null) {
                        minionConfigs.put(typeName, minionConfig);
                    }
                }
            }
        }
    }

    private MinionConfig parseMinionConfig(String type, FileConfiguration cfg) {
        try {
            String displayName = cfg.getString("display-name", type + " Minion");
            Material toolMaterial = Material.valueOf(cfg.getString("tool", "DIAMOND_PICKAXE").toUpperCase());
            Material targetBlock = Material.valueOf(cfg.getString("target-block", "COBBLESTONE").toUpperCase());
            Material dropMaterial = Material.valueOf(cfg.getString("drop-item", "COBBLESTONE").toUpperCase());
            String headSkin = cfg.getString("head-skin", "");
            
            // Armor color parsing (default BLUE)
            int r = cfg.getInt("armor-color.r", 0);
            int g = cfg.getInt("armor-color.g", 100);
            int b = cfg.getInt("armor-color.b", 255);
            Color armorColor = Color.fromRGB(r, g, b);

            int delay = cfg.getInt("action-delay", 5);

            return new MinionConfig(type, displayName, toolMaterial, targetBlock, dropMaterial, headSkin, armorColor, delay);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to parse minion config for type: " + type);
            return null;
        }
    }

    public void reloadConfigs() {
        loadConfigs();
    }

    public FileConfiguration getMainConfig() {
        return plugin.getConfig();
    }

    public FileConfiguration getGuiConfig() {
        return guiConfig;
    }

    public FileConfiguration getUpgradeConfig() {
        return upgradeConfig;
    }

    public MinionConfig getMinionConfig(String type) {
        if (type == null) return null;
        return minionConfigs.get(type.toLowerCase());
    }

    public Map<String, MinionConfig> getAllConfigs() {
        return minionConfigs;
    }
                }
