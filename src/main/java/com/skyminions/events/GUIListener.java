package com.skyminions.events;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.models.Minion;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GUIListener implements Listener {

    private final SkyMinionsPlugin plugin;

    public GUIListener(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle() == null || !event.getView().getTitle().contains("Minion Overview")) {
            return;
        }

        event.setCancelled(true); // Prevent players from stealing GUI items

        if (!(event.getWhoClicked() instanceof Player player)) return;
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null || currentItem.getType() == Material.AIR) return;

        int slot = event.getSlot();

        // 13: Collect Stored Items
        if (slot == 13) {
            // Logic for collecting stored minion items
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
            player.sendMessage("§aCollected all items from minion!");
        } 
        // 29: Upgrade Minion
        else if (slot == 29) {
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 0.8f, 1.2f);
            player.sendMessage("§eAttempting to upgrade minion...");
        } 
        // 31: Fuel Management
        else if (slot == 31) {
            player.playSound(player.getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, 1.0f, 1.0f);
            player.sendMessage("§cFuel menu coming soon!");
        } 
        // 49: Close Menu
        else if (slot == 49) {
            player.closeInventory();
        }
    }
    }
          
