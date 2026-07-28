package com.skyminions.models;

import org.bukkit.Location;
import java.util.UUID;

public abstract class AbstractMinion {

    protected final UUID minionId;
    protected final UUID ownerId;
    protected final String type;
    protected int level;
    protected Location location;
    protected final MinionStorage storage;
    
    protected long lastActiveTimestamp;
    protected long fuelRemainingMillis;
    protected double speedMultiplier = 1.0;

    // Statistics
    protected long totalItemsGenerated;
    protected long totalBlocksMined;

    public AbstractMinion(UUID minionId, UUID ownerId, String type, int level, Location location) {
        this.minionId = minionId;
        this.ownerId = ownerId;
        this.type = type;
        this.level = level;
        this.location = location;
        this.storage = new MinionStorage(level * 128);
        this.lastActiveTimestamp = System.currentTimeMillis();
    }

    public UUID getMinionId() { return minionId; }
    public UUID getOwnerId() { return ownerId; }
    public UUID getOwnerUUID() { return ownerId; }
    public String getType() { return type; }
    public int getLevel() { return level; }
    
    public void setLevel(int level) { 
        this.level = level; 
        if (this.storage != null) {
            this.storage.setCapacity(level * 128);
        }
    }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public MinionStorage getStorage() { return storage; }

    public long getLastActiveTimestamp() { return lastActiveTimestamp; }
    public void setLastActiveTimestamp(long timestamp) { this.lastActiveTimestamp = timestamp; }

    public long getFuelRemainingMillis() { return fuelRemainingMillis; }
    public void setFuelRemainingMillis(long millis) { this.fuelRemainingMillis = millis; }
    public boolean hasFuel() { return fuelRemainingMillis > 0; }

    public double getSpeedMultiplier() { return speedMultiplier; }
    public void setSpeedMultiplier(double speedMultiplier) { this.speedMultiplier = speedMultiplier; }

    public long getTotalItemsGenerated() { return totalItemsGenerated; }
    public void incrementItemsGenerated(long amount) { this.totalItemsGenerated += amount; }

    public abstract void tick();
  }
                                       
