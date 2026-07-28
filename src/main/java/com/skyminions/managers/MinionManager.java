package com.skyminions.managers;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.models.Minion;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MinionManager {

    private final SkyMinionsPlugin plugin;
    private final Map<UUID, Minion> activeMinions = new HashMap<>();
    private File dataFile;
    private FileConfiguration dataConfig;

    public MinionManager(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
        setupDataFile();
        loadMinions();
    }

    private void setupDataFile() {
        dataFile = new File(plugin.getDataFolder(), "data/minions_data.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create minions_data.yml!");
            }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void addMinion(Minion minion) {
        activeMinions.put(minion.getMinionId(), minion);
        saveMinions();
    }

    public void removeMinion(UUID id) {
        activeMinions.remove(id);
        dataConfig.set("minions." + id.toString(), null);
        saveDataFile();
    }

    public Minion getMinion(UUID id) {
        return activeMinions.get(id);
    }

    public Collection<Minion> getAllMinions() {
        return activeMinions.values();
    }

    public void saveMinions() {
        for (Minion minion : activeMinions.values()) {
            String path = "minions." + minion.getMinionId().toString() + ".";
            dataConfig.set(path + "owner", minion.getOwnerUUID().toString());
            dataConfig.set(path + "type", minion.getType());
            dataConfig.set(path + "level", minion.getLevel());
            dataConfig.set(path + "location", minion.getLocation());
        }
        saveDataFile();
    }

    private void loadMinions() {
        ConfigurationSection section = dataConfig.getConfigurationSection("minions");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            UUID id = UUID.fromString(key);
            String path = "minions." + key + ".";
            String ownerStr = dataConfig.getString(path + "owner");
            if (ownerStr == null) continue;

            UUID owner = UUID.fromString(ownerStr);
            String type = dataConfig.getString(path + "type");
            int level = dataConfig.getInt(path + "level");
            Location loc = dataConfig.getLocation(path + "location");

            Minion minion = new Minion(id, owner, type, level, loc);
            activeMinions.put(id, minion);
        }
    }

    private void saveDataFile() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save minions_data.yml!");
        }
    }
    }
                
