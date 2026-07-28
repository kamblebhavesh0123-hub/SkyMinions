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

    public Minion(UUID minionId, UUID ownerUUID, String type, int level, Location location) {
        this.minionId = minionId;
        this.ownerUUID = ownerUUID;
        this.type = type;
        this.level = level;
        this.location = location;
        this.storedAmount = 0;
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
}
