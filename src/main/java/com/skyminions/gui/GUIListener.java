package com.skyminions.gui;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.models.Minion;
import com.skyminions.models.MinionConfig;
import com.skyminions.util.ItemUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
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

        if (plainTitle.contains("Minion Shop")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;

            int slot = event.getSlot();
            for (MinionConfig config : plugin.getConfigManager().getAllConfigs().values()) {
                if (config.getShopSlot() == slot) {
                    buyMinion(player, config.getType());
                    return;
                }
            }
            return;
        }

        if (plainTitle.contains("Minion")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;

            Minion targetMinion = null;
            for (Minion minion : plugin.getMinionManager().getAllMinions()) {
                if (minion.getLocation() != null && player.getLocation().distanceSquared(minion.getLocation()) < 25) {
                    targetMinion = minion;
                    break;
                }
            }

            if (targetMinion == null) return;

            int slot = event.getSlot();
            switch (slot) {
                case 19 -> {
                    if (targetMinion.hasFuel()) {
                        targetMinion.setHasFuel(false);
                        player.getInventory().addItem(new ItemStack(Material.COAL, 1));
                        player.sendMessage(Component.text("Removed fuel from minion!", NamedTextColor.YELLOW));
                    } else {
                        if (player.getInventory().containsAtLeast(new ItemStack(Material.COAL), 1)) {
                            player.getInventory().removeItem(new ItemStack(Material.COAL, 1));
                            targetMinion.setHasFuel(true);
                            player.sendMessage(Component.text("Inserted Coal into fuel slot (+100% Speed)!", NamedTextColor.GREEN));
                        } else {
                            player.sendMessage(Component.text("You need Coal in your inventory to use as fuel!", NamedTextColor.RED));
                        }
                    }
                    plugin.getGuiManager().openMainMenu(player, targetMinion);
                }
                case 48 -> {
                    if (targetMinion.getStoredAmount() > 0) {
                        MinionConfig config = plugin.getConfigManager().getMinionConfig(targetMinion.getType());
                        Material resourceMat = config != null ? config.getResourceMaterial() : Material.COBBLESTONE;
                        int amount = targetMinion.getStoredAmount();
                        player.getInventory().addItem(new ItemStack(resourceMat, amount));
                        targetMinion.setStoredAmount(0);
                        player.sendMessage(Component.text("Collected " + amount + " resources!", NamedTextColor.GREEN));
                        plugin.getGuiManager().openMainMenu(player, targetMinion);
                    } else {
                        player.sendMessage(Component.text("No items to collect!", NamedTextColor.RED));
                    }
                }
                case 50 -> upgradeMinion(player, targetMinion);
                case 52 -> {
                    player.closeInventory();
                    for (Entity entity : player.getNearbyEntities(3, 3, 3)) {
                        if (entity instanceof ArmorStand stand && stand.getCustomName() != null) {
                            stand.remove();
                            break;
                        }
                    }
                    plugin.getMinionManager().removeMinion(targetMinion.getMinionId());
                    player.sendMessage(Component.text("Minion picked up successfully!", NamedTextColor.YELLOW));
                }
            }
        }
    }

    private void upgradeMinion(Player player, Minion minion) {
        if (minion.getLevel() >= 11) {
            player.sendMessage(Component.text("Minion is already at max level (Lv.11)!", NamedTextColor.RED));
            return;
        }

        MinionConfig config = plugin.getConfigManager().getMinionConfig(minion.getType());
        Material requiredMat = config != null ? config.getResourceMaterial() : Material.COBBLESTONE;
        int requiredAmount = minion.getLevel() * 64;

        if (player.getInventory().containsAtLeast(new ItemStack(requiredMat), requiredAmount)) {
            player.getInventory().removeItem(new ItemStack(requiredMat, requiredAmount));
            minion.setLevel(minion.getLevel() + 1);

            for (Entity entity : player.getNearbyEntities(3, 3, 3)) {
                if (entity instanceof ArmorStand stand) {
                    Component customName = Component.text(minion.getType() + " Minion ", NamedTextColor.AQUA)
                            .append(Component.text("[Lv." + minion.getLevel() + "]", NamedTextColor.GRAY));
                    stand.customName(customName);
                    break;
                }
            }

            player.sendMessage(Component.text("Upgraded " + minion.getType() + " Minion to Level " + minion.getLevel() + "!", NamedTextColor.GREEN));
            plugin.getGuiManager().openMainMenu(player, minion);
        } else {
            player.sendMessage(Component.text("You need " + requiredAmount + "x " + requiredMat.name() + " to upgrade!", NamedTextColor.RED));
        }
    }

    private void buyMinion(Player player, String type) {
        ItemStack minionItem = ItemUtil.createMinionItem(type, 1);
        player.getInventory().addItem(minionItem);
        player.sendMessage(Component.text("Purchased " + type + " Minion Lv.1!", NamedTextColor.GREEN));
        player.closeInventory();
    }
                            }
                            
