package com.skyminions.events;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.minions.MinionEntity;
import com.skyminions.models.Minion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MinionListener implements Listener {

    private final SkyMinionsPlugin plugin;

    public MinionListener(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMinionPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (!item.hasItemMeta() || event.getItemInHand().getItemMeta().displayName() == null) return;

        String displayName = event.getItemInHand().getItemMeta().displayName().toString();

        if (displayName.contains("Minion")) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            Location loc = event.getBlockPlaced().getLocation().add(0.5, 0, 0.5);

            Minion minion = new Minion(UUID.randomUUID(), player.getUniqueId(), "COBBLESTONE", 1, loc);
            plugin.getMinionManager().addMinion(minion);
            MinionEntity.spawnMinionStand(minion);

            item.setAmount(item.getAmount() - 1);
            player.sendMessage(Component.text("Minion placed successfully!", NamedTextColor.GREEN));
        }
    }

    @EventHandler
    public void onMinionInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof ArmorStand stand)) return;

        Player player = event.getPlayer();
        for (Minion minion : plugin.getMinionManager().getAllMinions()) {
            if (minion.getLocation().distanceSquared(stand.getLocation()) < 1.0) {
                event.setCancelled(true);
                plugin.getGuiManager().openMainMenu(player, minion);
                return;
            }
        }
    }

    @EventHandler
    public void onMinionDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof ArmorStand stand)) return;
        if (!(event.getDamager() instanceof Player player)) return;

        for (Minion minion : plugin.getMinionManager().getAllMinions()) {
            if (minion.getLocation().distanceSquared(stand.getLocation()) < 1.0) {
                event.setCancelled(true);
                if (minion.getOwnerUUID().equals(player.getUniqueId()) || player.hasPermission("skyminions.admin")) {
                    stand.remove();
                    plugin.getMinionManager().removeMinion(minion.getMinionId());
                    player.sendMessage(Component.text("Minion retrieved successfully!", NamedTextColor.YELLOW));
                } else {
                    player.sendMessage(Component.text("You do not own this minion!", NamedTextColor.RED));
                }
                return;
            }
        }
    }
                                                }
                    
