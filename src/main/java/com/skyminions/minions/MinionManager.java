package com.skyminions.minions;

import com.skyminions.models.Minion;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MinionManager {

    private final Map<UUID, Minion> minions = new HashMap<>();

    public void addMinion(Minion minion) {
        minions.put(minion.getMinionId(), minion);
    }

    public void removeMinion(UUID minionId) {
        minions.remove(minionId);
    }

    public Minion getMinion(UUID minionId) {
        return minions.get(minionId);
    }

    public Collection<Minion> getAllMinions() {
        return minions.values();
    }
}
