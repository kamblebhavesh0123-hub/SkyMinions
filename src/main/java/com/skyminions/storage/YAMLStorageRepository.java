package com.skyminions.storage;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.models.Minion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class YAMLStorageRepository implements StorageRepository {

    private final SkyMinionsPlugin plugin;
    private File file;
    private FileConfiguration config;

    public YAMLStorageRepository(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        file = new File(plugin.getDataFolder(), "data.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create data.yml: " + e.getMessage());
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void saveMinion(Minion minion) {
        String path = "minions." + minion.getMinionId().toString() + ".";
        config.set(path + "owner", minion.getOwnerId().toString());
        config.set(path + "type", minion.getType());
        config.set(path + "level", minion.getLevel());
        config.set(path + "location.world", minion.getLocation().getWorld().getName());
        config.set(path + "location.x", minion.getLocation().getX());
        config.set(path + "location.y", minion.getLocation().getY());
        config.set(path + "location.z", minion.getLocation().getZ());
        config.set(path + "location.yaw", minion.getLocation().getYaw());
        config.set(path + "location.pitch", minion.getLocation().getPitch());
        config.set(path + "storedAmount", minion.getStoredAmount());
        config.set(path + "itemsGenerated", minion.getTotalItemsGenerated());
        config.set(path + "lastActive", minion.getLastActiveTimestamp());

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save data.yml: " + e.getMessage());
        }
    }

    @Override
    public void saveAllMinions(Collection<Minion> minions) {
        for (Minion minion : minions) {
            saveMinion(minion);
        }
    }

    @Override
    public Minion loadMinion(UUID minionId) {
        String path = "minions." + minionId.toString() + ".";
        if (!config.contains(path + "owner")) return null;

        UUID ownerId = UUID.fromString(config.getString(path + "owner"));
        String type = config.getString(path + "type");
        int level = config.getInt(path + "level");
        String world = config.getString(path + "location.world");
        double x = config.getDouble(path + "location.x");
        double y = config.getDouble(path + "location.y");
        double z = config.getDouble(path + "location.z");
        float yaw = (float) config.getDouble(path + "location.yaw");
        float pitch = (float) config.getDouble(path + "location.pitch");

        Location loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        Minion minion = new Minion(minionId, ownerId, type, level, loc);
        minion.setStoredAmount(config.getInt(path + "storedAmount", 0));
        minion.setLastActiveTimestamp(config.getLong(path + "lastActive", System.currentTimeMillis()));

        return minion;
    }

    @Override
    public Collection<Minion> loadAllMinions() {
        List<Minion> minions = new ArrayList<>();
        if (!config.contains("minions")) return minions;

        for (String key : config.getConfigurationSection("minions").getKeys(false)) {
            UUID id = UUID.fromString(key);
            Minion m = loadMinion(id);
            if (m != null) minions.add(m);
        }
        return minions;
    }

    @Override
    public void deleteMinion(UUID minionId) {
        config.set("minions." + minionId.toString(), null);
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save data.yml after deleting minion: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        // Nothing special required for YAML closure
    }
                   }
                   
