package com.skyminions.models;

import org.bukkit.Location;

import java.util.UUID;

public class Minion {

    private final UUID minionId;
    private final UUID ownerUUID;
    private final String type;
    private int level;
    private Location location;
    private int storedAmount;
    private boolean hasFuel;
    private boolean hasSmelter;
    private boolean hasCompactor;

    public Minion(UUID minionId, UUID ownerUUID, String type, int level, Location location) {
        this.minionId = minionId;
        this.ownerUUID = ownerUUID;
        this.type = type;
        this.level = level;
        this.location = location;
        this.storedAmount = 0;
        this.hasFuel = false;
        this.hasSmelter = false;
        this.hasCompactor = false;
    }

    public UUID getMinionId() { return minionId; }
    public UUID getOwnerUUID() { return ownerUUID; }
    public String getType() { return type; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public int getStoredAmount() { return storedAmount; }
    public void setStoredAmount(int storedAmount) { this.storedAmount = storedAmount; }
    public void addStoredAmount(int amount) { this.storedAmount += amount; }

    public boolean hasFuel() { return hasFuel; }
    public void setHasFuel(boolean hasFuel) { this.hasFuel = hasFuel; }

    public boolean hasSmelter() { return hasSmelter; }
    public void setHasSmelter(boolean hasSmelter) { this.hasSmelter = hasSmelter; }

    public boolean hasCompactor() { return hasCompactor; }
    public void setHasCompactor(boolean hasCompactor) { this.hasCompactor = hasCompactor; }
    }
    
