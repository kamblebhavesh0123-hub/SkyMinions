package com.skyminions.tasks;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.models.Minion;
import com.skyminions.models.MinionConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
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
            Location loc = minion.getLocation();
            if (loc == null || loc.getWorld() == null) continue;

            MinionConfig config = plugin.getConfigManager().getMinionConfig(minion.getType());
            Material targetBlockMat = config != null ? config.getResourceMaterial() : Material.COBBLESTONE;

            // Compute block offset 1 block ahead in facing direction
            Block targetBlock = loc.clone().add(loc.getDirection().multiply(1.2)).getBlock();

            int maxCapacity = minion.getLevel() * 128;
            if (minion.getStoredAmount() < maxCapacity) {

                if (targetBlock.getType() == Material.AIR || targetBlock.getType() == Material.SHORT_GRASS || targetBlock.getType() == Material.TALL_GRASS) {
                    targetBlock.setType(targetBlockMat);
                    loc.getWorld().playSound(targetBlock.getLocation(), Sound.BLOCK_STONE_PLACE, 0.5f, 1.0f);
                } else if (targetBlock.getType() == targetBlockMat) {
                    loc.getWorld().spawnParticle(Particle.BLOCK, targetBlock.getLocation().add(0.5, 0.5, 0.5), 10, targetBlockMat.createBlockData());
                    loc.getWorld().playSound(targetBlock.getLocation(), Sound.BLOCK_STONE_BREAK, 0.8f, 1.2f);
                    targetBlock.setType(Material.AIR);

                    int amountToProduce = minion.hasFuel() ? 2 : 1;
                    minion.addStoredAmount(amountToProduce);
                }
            }
        }
    }
                }
                                             
