package com.skyminions.models;

import org.bukkit.Location;
import java.util.UUID;

public class Minion extends AbstractMinion {

    private boolean hasSmelter;
    private boolean hasCompactor;

    public Minion(UUID minionId, UUID ownerId, String type, int level, Location location) {
        super(minionId, ownerId, type, level, location);
    }

    @Override
    public void tick() {
        // Individual tick logic managed by centralized MinionTickerTask
    }

    public boolean hasSmelter() { return hasSmelter; }
    public void setHasSmelter(boolean hasSmelter) { this.hasSmelter = hasSmelter; }

    public boolean hasCompactor() { return hasCompactor; }
    public void setHasCompactor(boolean hasCompactor) { this.hasCompactor = hasCompactor; }
}
