package com.skyminions.managers;

import com.skyminions.models.Minion;
import org.bukkit.entity.Player;

public class OfflineProductionManager {

    public static void processOfflineProduction(Minion minion, Player owner) {
        long now = System.currentTimeMillis();
        long lastActive = minion.getLastActiveTimestamp();
        long elapsedSeconds = (now - lastActive) / 1000;

        if (elapsedSeconds < 10) return;

        long baseActionTime = 15; 
        long actions = (long) (elapsedSeconds / (baseActionTime / minion.getSpeedMultiplier()));

        if (actions <= 0) return;

        int spaceAvailable = minion.getStorage().getCapacity() - minion.getStorage().getStoredAmount();
        int itemsToProduce = (int) Math.min(actions, spaceAvailable);

        if (itemsToProduce > 0) {
            minion.addStoredAmount(itemsToProduce);
            minion.incrementItemsGenerated(itemsToProduce);

            if (owner != null && owner.isOnline()) {
                owner.sendMessage("§a[SkyMinions] Your " + minion.getType() + " Minion generated §e" 
                        + itemsToProduce + " items §awhile you were away!");
            }
        }

        minion.setLastActiveTimestamp(now);
    }
}
