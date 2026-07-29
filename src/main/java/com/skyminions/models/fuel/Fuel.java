package com.skyminions.models.fuel;

public class Fuel {

    private final String id;
    private final String displayName;
    private final double speedMultiplier;
    private final long durationSeconds;

    public Fuel(String id, String displayName, double speedMultiplier, long durationSeconds) {
        this.id = id;
        this.displayName = displayName;
        this.speedMultiplier = speedMultiplier;
        this.durationSeconds = durationSeconds;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public double getSpeedMultiplier() { return speedMultiplier; }
    public long getDurationSeconds() { return durationSeconds; }
    public long getDurationMillis() { return durationSeconds * 1000L; }
}
