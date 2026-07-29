package com.skyminions.tasks;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.api.events.MinionGenerateEvent;
import com.skyminions.minions.MinionEntity;
import com.skyminions.models.Minion;
import com.skyminions.models.MinionConfig;
import com.skyminions.models.state.MinionState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MinionTickerTask extends BukkitRunnable {

    private final SkyMinionsPlugin plugin;

    public MinionTickerTask(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Minion minion : plugin.getMinionManager().getAllMinions()) {
            Location loc = minion.getLocation();
            if (loc == null || loc.getWorld() == null || !loc.getChunk().isLoaded()) continue;

            // Evaluate Minion State Machine
            minion.getStateMachine().evaluateState();

            ArmorStand stand = null;
            for (Entity entity : loc.getWorld().getNearbyEntities(loc, 1.5, 1.5, 1.5)) {
                if (entity instanceof ArmorStand armorStand) {
                    stand = armorStand;
                    break;
                }
            }

            // If storage is full, play particle feedback and throttle processing
            if (minion.getState() == MinionState.STORAGE_FULL) {
                MinionEntity.playParticleFeedback(loc, false);
                if (stand != null) {
                    MinionEntity.updateNameTag(minion, stand);
                }
                continue;
            }

            MinionConfig config = plugin.getConfigManager().getMinionConfig(minion.getType());
            Material targetBlockMat = config != null ? config.getResourceMaterial() : Material.COBBLESTONE;

            Block targetBlock = loc.clone().add(loc.getDirection().multiply(1.2)).getBlock();

            if (targetBlock.getType() == Material.AIR || targetBlock.getType() == Material.GRASS_BLOCK || targetBlock.getType() == Material.TALL_GRASS) {
                minion.getStateMachine().setState(MinionState.WORKING);
                targetBlock.setType(targetBlockMat);
                loc.getWorld().playSound(targetBlock.getLocation(), Sound.BLOCK_STONE_PLACE, 0.5f, 1.0f);
            } else if (targetBlock.getType() == targetBlockMat) {
                minion.getStateMachine().setState(MinionState.COLLECTING);
                loc.getWorld().spawnParticle(Particle.BLOCK, targetBlock.getLocation().add(0.5, 0.5, 0.5), 10, targetBlockMat.createBlockData());
                loc.getWorld().playSound(targetBlock.getLocation(), Sound.BLOCK_STONE_BREAK, 0.8f, 1.2f);
                targetBlock.setType(Material.AIR);

                int amountToProduce = minion.hasFuel() ? 2 : 1;
                List<ItemStack> drops = new ArrayList<>(Collections.singletonList(new ItemStack(targetBlockMat, amountToProduce)));

                MinionGenerateEvent generateEvent = new MinionGenerateEvent(minion, drops);
                Bukkit.getPluginManager().callEvent(generateEvent);

                if (!generateEvent.isCancelled()) {
                    for (ItemStack item : generateEvent.getGeneratedItems()) {
                        minion.addStoredAmount(item.getAmount());
                        minion.incrementItemsGenerated(item.getAmount());
                    }

                    if (stand != null) {
                        MinionEntity.playCollectAnimation(plugin, stand);
                        MinionEntity.updateNameTag(minion, stand);
                    }
                    MinionEntity.playParticleFeedback(loc, true);
                }
            } else {
                minion.getStateMachine().setState(MinionState.IDLE);
            }
        }
    }
                    }
                    
