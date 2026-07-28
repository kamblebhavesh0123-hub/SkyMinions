package com.skyminions.models;

import org.bukkit.Location;
import java.util.UUID;

public class Minion {
    private final UUID minionId;
    private final UUID ownerId;
    private final String type;
    private int level;
    private Location location;
    private final MinionStorage storage;
    private boolean hasSmelter;
    private boolean hasCompactor;
    private boolean hasFuel;

    public Minion(UUID minionId, UUID ownerId, String type, int level, Location location) {
        this.minionId = minionId;
        this.ownerId = ownerId;
        this.type = type;
        this.level = level;
        this.location = location;
        this.storage = new MinionStorage(level * 128);
    }

    public UUID getMinionId() { return minionId; }
    public UUID getOwnerId() { return ownerId; }
    public String getType() { return type; }
    public int getLevel() { return level; }
    public void setLevel(int level) { 
        this.level = level; 
        if (this.storage != null) {
            this.storage.setCapacity(level * 128);
        }
    }
    public Location getLocation() { return location; }
    
    // Storage Getter
    public MinionStorage getStorage() { return storage; }

    // Direct helper methods for stored amount
    public int getStoredAmount() { 
        return storage != null ? storage.getStoredAmount() : 0; 
    }
    public void setStoredAmount(int amount) { 
        if (storage != null) storage.setStoredAmount(amount); 
    }
    public void addStoredAmount(int amount) { 
        if (storage != null) storage.setStoredAmount(getStoredAmount() + amount); 
    }

    // Upgrade Getters & Setters
    public boolean hasSmelter() { return hasSmelter; }
    public void setHasSmelter(boolean hasSmelter) { this.hasSmelter = hasSmelter; }

    public boolean hasCompactor() { return hasCompactor; }
    public void setHasCompactor(boolean hasCompactor) { this.hasCompactor = hasCompactor; }

    public boolean hasFuel() { return hasFuel; }
    public void setHasFuel(boolean hasFuel) { this.hasFuel = hasFuel; }
    }
