package com.skyminions.config;

import com.skyminions.SkyMinionsPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final SkyMinionsPlugin plugin;
    private FileConfiguration guiConfig;
    private File guiConfigFile;
    private FileConfiguration upgradeConfig;
    private File upgradeConfigFile;

    public ConfigManager(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
        reloadConfigs();
    }

    public void reloadConfigs() {
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
}
