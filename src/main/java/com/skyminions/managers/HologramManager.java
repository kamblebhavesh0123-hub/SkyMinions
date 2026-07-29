package com.skyminions.managers;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.models.Minion;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HologramManager {

    private final SkyMinionsPlugin plugin;
    private final Map<UUID, List<ArmorStand>> activeHolograms = new HashMap<>();

    public HologramManager(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
    }

    public void spawnHologram(Minion minion) {
        removeHologram(minion.getMinionId());

        Location loc = minion.getLocation().clone().add(0, 1.2, 0);
        List<ArmorStand> lines = new ArrayList<>();

        String[] templateLines = new String[]{
            "§e§l" + minion.getType() + " MINION",
            "§7Tier §a" + minion.getLevel(),
            "§7Storage: §e" + minion.getStoredAmount() + "/" + minion.getStorage().getCapacity(),
            "§aStatus: " + minion.getState().name()
        };

        double lineSpacing = 0.28;
        for (int i = templateLines.length - 1; i >= 0; i--) {
            Location lineLoc = loc.clone().add(0, (templateLines.length - 1 - i) * lineSpacing, 0);
            ArmorStand stand = (ArmorStand) loc.getWorld().spawnEntity(lineLoc, EntityType.ARMOR_STAND);
            stand.setVisible(false);
            stand.setGravity(false);
            stand.setCustomName(templateLines[i]);
            stand.setCustomNameVisible(true);
            stand.setMarker(true);
            lines.add(stand);
        }

        activeHolograms.put(minion.getMinionId(), lines);
    }

    public void updateHologram(Minion minion) {
        spawnHologram(minion);
    }

    public void removeHologram(UUID minionId) {
        List<ArmorStand> lines = activeHolograms.remove(minionId);
        if (lines != null) {
            for (ArmorStand stand : lines) {
                if (stand != null && stand.isValid()) {
                    stand.remove();
                }
            }
        }
    }

    public void removeAll() {
        for (UUID id : new ArrayList<>(activeHolograms.keySet())) {
            removeHologram(id);
        }
    }
          }
                                                   
