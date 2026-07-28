package com.skyminions.tasks;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.models.Minion;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class MinionTask extends BukkitRunnable {

    private final SkyMinionsPlugin plugin;

    public MinionTask(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Minion minion : plugin.getMinionManager().getAllMinions()) {
            if (minion.getLocation() != null && minion.getLocation().getWorld() != null) {
                int maxCapacity = minion.getLevel() * 128;
                if (minion.getStoredAmount() < maxCapacity) {
                    // Produce 2x resources if fuel is active, otherwise 1
                    int amountToProduce = minion.hasFuel() ? 2 : 1;
                    minion.addStoredAmount(amountToProduce);

                    minion.getLocation().getWorld().spawnParticle(
                            Particle.HAPPY_VILLAGER,
                            minion.getLocation().clone().add(0, 1.2, 0),
                            minion.hasFuel() ? 8 : 3, 0.2, 0.2, 0.2
                    );
                    minion.getLocation().getWorld().playSound(
                            minion.getLocation(),
                            Sound.BLOCK_STONE_BREAK,
                            0.5f, 1.2f
                    );
                }
            }
        }
    }
                    }
                        
