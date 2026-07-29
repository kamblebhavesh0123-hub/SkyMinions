package com.skyminions.storage;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.models.Minion;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class SQLStorageRepository implements StorageRepository {

    private final SkyMinionsPlugin plugin;
    private final String connectionUrl;
    private Connection connection;

    public SQLStorageRepository(SkyMinionsPlugin plugin, String connectionUrl) {
        this.plugin = plugin;
        this.connectionUrl = connectionUrl;
    }

    @Override
    public void init() {
        try {
            connection = DriverManager.getConnection(connectionUrl);
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS skyminions_data (
                        minion_id VARCHAR(36) PRIMARY KEY,
                        owner_id VARCHAR(36) NOT NULL,
                        type VARCHAR(64) NOT NULL,
                        level INT NOT NULL,
                        world VARCHAR(64) NOT NULL,
                        x DOUBLE NOT NULL,
                        y DOUBLE NOT NULL,
                        z DOUBLE NOT NULL,
                        yaw FLOAT NOT NULL,
                        pitch FLOAT NOT NULL,
                        stored_amount INT NOT NULL,
                        items_generated BIGINT NOT NULL,
                        last_active BIGINT NOT NULL
                    );
                """);
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to initialize SQL database: " + e.getMessage());
        }
    }

    @Override
    public void saveMinion(Minion minion) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String sql = """
                INSERT INTO skyminions_data (minion_id, owner_id, type, level, world, x, y, z, yaw, pitch, stored_amount, items_generated, last_active)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    level = VALUES(level),
                    stored_amount = VALUES(stored_amount),
                    items_generated = VALUES(items_generated),
                    last_active = VALUES(last_active);
            """;
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, minion.getMinionId().toString());
                stmt.setString(2, minion.getOwnerId().toString());
                stmt.setString(3, minion.getType());
                stmt.setInt(4, minion.getLevel());
                stmt.setString(5, minion.getLocation().getWorld().getName());
                stmt.setDouble(6, minion.getLocation().getX());
                stmt.setDouble(7, minion.getLocation().getY());
                stmt.setDouble(8, minion.getLocation().getZ());
                stmt.setFloat(9, minion.getLocation().getYaw());
                stmt.setFloat(10, minion.getLocation().getPitch());
                stmt.setInt(11, minion.getStoredAmount());
                stmt.setLong(12, minion.getTotalItemsGenerated());
                stmt.setLong(13, minion.getLastActiveTimestamp());
                stmt.executeUpdate();
            } catch (SQLException e) {
                plugin.getLogger().severe("Failed to save minion " + minion.getMinionId() + ": " + e.getMessage());
            }
        });
    }

    @Override
    public void saveAllMinions(Collection<Minion> minions) {
        for (Minion minion : minions) {
            saveMinion(minion);
        }
    }

    @Override
    public Minion loadMinion(UUID minionId) {
        String sql = "SELECT * FROM skyminions_data WHERE minion_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, minionId.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                UUID ownerId = UUID.fromString(rs.getString("owner_id"));
                String type = rs.getString("type");
                int level = rs.getInt("level");
                String worldName = rs.getString("world");
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");
                float yaw = rs.getFloat("yaw");
                float pitch = rs.getFloat("pitch");

                Location loc = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
                Minion minion = new Minion(minionId, ownerId, type, level, loc);
                minion.setStoredAmount(rs.getInt("stored_amount"));
                minion.setLastActiveTimestamp(rs.getLong("last_active"));
                return minion;
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to load minion " + minionId + ": " + e.getMessage());
        }
        return null;
    }

    @Override
    public Collection<Minion> loadAllMinions() {
        List<Minion> list = new ArrayList<>();
        String sql = "SELECT minion_id FROM skyminions_data";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                UUID id = UUID.fromString(rs.getString("minion_id"));
                Minion m = loadMinion(id);
                if (m != null) list.add(m);
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to load all minions: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void deleteMinion(UUID minionId) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String sql = "DELETE FROM skyminions_data WHERE minion_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, minionId.toString());
                stmt.executeUpdate();
            } catch (SQLException e) {
                plugin.getLogger().severe("Failed to delete minion " + minionId + ": " + e.getMessage());
            }
        });
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to close database connection: " + e.getMessage());
        }
    }
                                   }
