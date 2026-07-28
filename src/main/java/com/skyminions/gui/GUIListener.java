package com.skyminions.gui;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.util.ItemUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
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
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Component title = event.getView().title();
        String plainTitle = title.toString();

        // 1. Minion Shop Click Handler
        if (plainTitle.contains("Minion Shop")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;

            int slot = event.getSlot();
            switch (slot) {
                case 11 -> buyMinion(player, "COBBLESTONE");
                case 13 -> buyMinion(player, "WHEAT");
                case 15 -> buyMinion(player, "OAK");
            }
            return;
        }

        // 2. Main Minion Menu Click Handler
        if (plainTitle.contains("Minion")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;

            int slot = event.getSlot();
            switch (slot) {
                case 48 -> player.sendMessage(Component.text("Collected all minion resources!", NamedTextColor.GREEN));
                case 50 -> player.sendMessage(Component.text("Opening Minion Upgrades...", NamedTextColor.GOLD));
                case 52 -> {
                    player.closeInventory();
                    for (Entity entity : player.getNearbyEntities(3, 3, 3)) {
                        if (entity instanceof ArmorStand stand && stand.getCustomName() != null) {
                            stand.remove();
                            break;
                        }
                    }
                    player.sendMessage(Component.text("Minion picked up successfully!", NamedTextColor.YELLOW));
                }
            }
        }
    }

    private void buyMinion(Player player, String type) {
        ItemStack minionItem = ItemUtil.createMinionItem(type, 1);
        player.getInventory().addItem(minionItem);
        player.sendMessage(Component.text("Purchased " + type + " Minion Lv.1!", NamedTextColor.GREEN));
        player.closeInventory();
    }
                                     }
                            
