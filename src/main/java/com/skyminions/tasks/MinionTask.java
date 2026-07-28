package com.skyminions.tasks;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.models.Minion;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class MinionTask extends BukkitRunnable {

    private final SkyMinionsPlugin plugin;

    public MinionTask(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Minion minion : plugin.getMinionManager().getAllMinions()) {
            processMinionWork(minion);
        }
    }

    private void processMinionWork(Minion minion) {
        Location loc = minion.getLocation();
        if (loc == null || loc.getWorld() == null) return;

        Block targetBlock = loc.clone().add(0, -1, 0).getBlock();

        // Core work logic based on minion type
        if (minion.getType().equalsIgnoreCase("MINING")) {
            if (targetBlock.getType() == Material.AIR) {
                targetBlock.setType(Material.COBBLESTONE);
            }
        } else if (minion.getType().equalsIgnoreCase("FARMING")) {
            if (targetBlock.getType() == Material.DIRT || targetBlock.getType() == Material.FARMLAND) {
                Block cropBlock = loc.getBlock();
                if (cropBlock.getType() == Material.AIR) {
                    cropBlock.setType(Material.WHEAT);
                }
            }
        }
    }
                   }
