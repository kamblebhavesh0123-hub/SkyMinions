package com.skyminions.config;

import com.skyminions.SkyMinionsPlugin;
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
    private final Map<String, FileConfiguration> minionConfigs = new HashMap<>();

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

        // Load minion-specific configs from minions folder if present
        minionConfigs.clear();
        File minionsDir = new File(plugin.getDataFolder(), "minions");
        if (minionsDir.exists() && minionsDir.isDirectory()) {
            File[] files = minionsDir.listFiles((dir, name) -> name.endsWith(".yml"));
            if (files != null) {
                for (File file : files) {
                    String typeName = file.getName().replace(".yml", "").toLowerCase();
                    minionConfigs.put(typeName, YamlConfiguration.loadConfiguration(file));
                }
            }
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

    public FileConfiguration getMinionConfig(String type) {
        if (type == null) return getMainConfig();
        FileConfiguration cfg = minionConfigs.get(type.toLowerCase());
        return cfg != null ? cfg : getMainConfig();
    }

    public Map<String, FileConfiguration> getAllConfigs() {
        return minionConfigs;
    }
    }
                                 
