package com.skyminions.models;

import org.bukkit.Location;
import java.util.UUID;

public class Minion extends AbstractMinion {

    private boolean hasSmelter;
    private boolean hasCompactor;
    private boolean hasFuel;

    public Minion(UUID minionId, UUID ownerId, String type, int level, Location location) {
        super(minionId, ownerId, type, level, location);
    }

    @Override
    public void tick() {
        // Individual tick logic managed by centralized MinionTickerTask
    }

    // Direct helper methods for stored amounts used by GUIs & Tasks
    public int getStoredAmount() { 
        return storage != null ? storage.getStoredAmount() : 0; 
    }

    public void setStoredAmount(int amount) { 
        if (storage != null) {
            storage.setStoredAmount(amount); 
        }
    }

    public void addStoredAmount(int amount) { 
        if (storage != null) {
            storage.setStoredAmount(getStoredAmount() + amount); 
        }
    }

    // Upgrade Getters & Setters
    public boolean hasSmelter() { return hasSmelter; }
    public void setHasSmelter(boolean hasSmelter) { this.hasSmelter = hasSmelter; }

    public boolean hasCompactor() { return hasCompactor; }
    public void setHasCompactor(boolean hasCompactor) { this.hasCompactor = hasCompactor; }

    public boolean hasFuel() { return hasFuel || fuelRemainingMillis > 0; }
    public void setHasFuel(boolean hasFuel) { this.hasFuel = hasFuel; }
        }
