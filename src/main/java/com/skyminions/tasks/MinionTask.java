package com.skyminions.tasks;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.minions.MinionEntity;
import com.skyminions.models.Minion;
import com.skyminions.models.MinionConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
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

            // Target block 1.2 units ahead of minion
            Block targetBlock = loc.clone().add(loc.getDirection().multiply(1.2)).getBlock();

            // Find matching ArmorStand nearby for visual animations
            ArmorStand stand = null;
            for (Entity entity : loc.getWorld().getNearbyEntities(loc, 1.5, 1.5, 1.5)) {
                if (entity instanceof ArmorStand armorStand) {
                    stand = armorStand;
                    break;
                }
            }

            if (minion.getStorage().isFull()) {
                // Storage Full -> Spawn Smoke particles & update tag
                MinionEntity.playParticleFeedback(loc, false);
                if (stand != null) {
                    MinionEntity.updateNameTag(minion, stand);
                }
                continue;
            }

            // Step A: Place block if empty or grass
            if (targetBlock.getType() == Material.AIR || targetBlock.getType() == Material.GRASS_BLOCK || targetBlock.getType() == Material.TALL_GRASS) {
                targetBlock.setType(targetBlockMat);
                loc.getWorld().playSound(targetBlock.getLocation(), Sound.BLOCK_STONE_PLACE, 0.5f, 1.0f);
            } 
            // Step B: Mine block, trigger arm swing & collect item
            else if (targetBlock.getType() == targetBlockMat) {
                loc.getWorld().spawnParticle(Particle.BLOCK, targetBlock.getLocation().add(0.5, 0.5, 0.5), 10, targetBlockMat.createBlockData());
                loc.getWorld().playSound(targetBlock.getLocation(), Sound.BLOCK_STONE_BREAK, 0.8f, 1.2f);
                targetBlock.setType(Material.AIR);

                int amountToProduce = minion.hasFuel() ? 2 : 1;
                minion.addStoredAmount(amountToProduce);

                // Trigger Visual Feedback & Live Name Tag Update
                if (stand != null) {
                    MinionEntity.playCollectAnimation(plugin, stand);
                    MinionEntity.updateNameTag(minion, stand);
                }
                MinionEntity.playParticleFeedback(loc, true);
            }
        }
    }
}
