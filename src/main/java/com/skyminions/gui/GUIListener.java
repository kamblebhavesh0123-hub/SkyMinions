package com.skyminions.gui;

import com.skyminions.SkyMinionsPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListener implements Listener {

    private final SkyMinionsPlugin plugin;

    public GUIListener(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        // Check if viewing a SkyMinion menu
        Component title = event.getView().title();
        String plainTitle = title.toString();

        if (plainTitle.contains("Minion")) {
            event.setCancelled(true); // Stop items from being taken out of GUI

            if (event.getCurrentItem() == null) return;

            int slot = event.getSlot();
            switch (slot) {
                case 21 -> player.sendMessage(Component.text("Collected all minion resources!", NamedTextColor.GREEN));
                case 23 -> player.sendMessage(Component.text("Opening Minion Upgrades...", NamedTextColor.GOLD));
                case 30 -> player.sendMessage(Component.text("Insert fuel in this slot.", NamedTextColor.RED));
                case 32 -> player.sendMessage(Component.text("Opening Equipment Settings...", NamedTextColor.AQUA));
                case 49 -> {
                    player.closeInventory();
                    player.sendMessage(Component.text("Minion picked up!", NamedTextColor.YELLOW));
                }
            }
        }
    }
    }
  
