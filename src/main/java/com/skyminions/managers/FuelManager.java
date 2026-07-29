package com.skyminions.managers;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.models.fuel.Fuel;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FuelManager {

    private final SkyMinionsPlugin plugin;
    private final Map<String, Fuel> fuels = new HashMap<>();

    public FuelManager(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
        loadFuels();
    }

    public void loadFuels() {
        fuels.clear();
        File file = new File(plugin.getDataFolder(), "fuels.yml");
        if (!file.exists()) {
            plugin.saveResource("fuels.yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (config.getConfigurationSection("fuels") != null) {
            for (String key : config.getConfigurationSection("fuels").getKeys(false)) {
                String name = config.getString("fuels." + key + ".name", key);
                double speed = config.getDouble("fuels." + key + ".speed-multiplier", 1.25);
                long duration = config.getLong("fuels." + key + ".duration-seconds", 86400);

                fuels.put(key.toLowerCase(), new Fuel(key, name, speed, duration));
            }
        }
    }

    public Fuel getFuel(String id) {
        return fuels.get(id.toLowerCase());
    }

    public Map<String, Fuel> getAllFuels() {
        return fuels;
    }
                  }
