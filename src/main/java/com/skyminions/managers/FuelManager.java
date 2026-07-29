package com.skyminions.managers;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.models.Fuel;
import com.skyminions.models.Minion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FuelManager {

    private final SkyMinionsPlugin plugin;
    private final Map<String, Fuel> loadedFuels = new HashMap<>();

    public FuelManager(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
        loadFuels();
    }

    public void loadFuels() {
        loadedFuels.clear();
        File file = new File(plugin.getDataFolder(), "fuel.yml");
        if (!file.exists()) {
            plugin.saveResource("fuel.yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("fuels");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            String name = section.getString(key + ".display-name", key);
            double speedMultiplier = section.getDouble(key + ".speed-multiplier", 1.0);
            long duration = section.getLong(key + ".duration-seconds", 3600);

            Fuel fuel = new Fuel(key, name, speedMultiplier, duration);
            loadedFuels.put(key.toLowerCase(), fuel);
        }
        plugin.getLogger().info("Loaded " + loadedFuels.size() + " fuel types.");
    }

    public Fuel getFuel(String id) {
        return loadedFuels.get(id.toLowerCase());
    }

    public void applyFuel(Minion minion, Fuel fuel) {
        minion.setSpeedMultiplier(fuel.getSpeedMultiplier());
        minion.setHasFuel(true);
        plugin.getMinionManager().saveMinions();
    }

    public Map<String, Fuel> getLoadedFuels() {
        return loadedFuels;
    }
                               }
