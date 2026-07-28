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
                case 10 -> {
                    if (targetMinion.hasSmelter()) {
                        targetMinion.setHasSmelter(false);
                        player.getInventory().addItem(new ItemStack(Material.FURNACE, 1));
                        player.sendMessage(Component.text("Removed Auto-Smelter!", NamedTextColor.YELLOW));
                    } else {
                        if (player.getInventory().containsAtLeast(new ItemStack(Material.FURNACE), 1)) {
                            player.getInventory().removeItem(new ItemStack(Material.FURNACE, 1));
                            targetMinion.setHasSmelter(true);
                            player.sendMessage(Component.text("Inserted Auto-Smelter!", NamedTextColor.GREEN));
                        } else {
                            player.sendMessage(Component.text("You need a Furnace in inventory to use Auto-Smelter!", NamedTextColor.RED));
                        }
                    }
                    plugin.getGuiManager().openMainMenu(player, targetMinion);
                }

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

                case 28 -> {
                    if (targetMinion.hasCompactor()) {
                        targetMinion.setHasCompactor(false);
                        player.getInventory().addItem(new ItemStack(Material.PISTON, 1));
                        player.sendMessage(Component.text("Removed Auto-Compactor!", NamedTextColor.YELLOW));
                    } else {
                        if (player.getInventory().containsAtLeast(new ItemStack(Material.PISTON), 1)) {
                            player.getInventory().removeItem(new ItemStack(Material.PISTON, 1));
                            targetMinion.setHasCompactor(true);
                            player.sendMessage(Component.text("Inserted Auto-Compactor!", NamedTextColor.GREEN));
                        } else {
                            player.sendMessage(Component.text("You need a Piston in inventory to use Compactor!", NamedTextColor.RED));
                        }
                    }
                    plugin.getGuiManager().openMainMenu(player, targetMinion);
                }

                case 48 -> {
                    if (targetMinion.getStoredAmount() > 0) {
                        MinionConfig config = plugin.getConfigManager().getMinionConfig(targetMinion.getType());
                        Material baseMat = config != null ? config.getResourceMaterial() : Material.COBBLESTONE;
                        int stored = targetMinion.getStoredAmount();

                        Material giveMat = baseMat;
                        int giveAmount = stored;

                        if (targetMinion.hasSmelter()) {
                            if (baseMat == Material.COBBLESTONE) giveMat = Material.STONE;
                            else if (baseMat == Material.RAW_IRON) giveMat = Material.IRON_INGOT;
                            else if (baseMat == Material.RAW_GOLD) giveMat = Material.GOLD_INGOT;
                        }

                        if (targetMinion.hasCompactor() && stored >= 9) {
                            giveAmount = stored / 9;
                            if (giveMat == Material.STONE) giveMat = Material.STONE_BRICKS;
                            else if (giveMat == Material.WHEAT) giveMat = Material.HAY_BLOCK;
                            else if (giveMat == Material.COAL) giveMat = Material.COAL_BLOCK;
                            else if (giveMat == Material.REDSTONE) giveMat = Material.REDSTONE_BLOCK;
                            else if (giveMat == Material.DIAMOND) giveMat = Material.DIAMOND_BLOCK;

                            targetMinion.setStoredAmount(stored % 9);
                        } else {
                            targetMinion.setStoredAmount(0);
                        }

                        player.getInventory().addItem(new ItemStack(giveMat, giveAmount));
                        player.sendMessage(Component.text("Collected " + giveAmount + "x " + giveMat.name() + "!", NamedTextColor.GREEN));
                        plugin.getGuiManager().openMainMenu(player, targetMinion);
                    } else {
                        player.sendMessage(Component.text("No items to collect!", NamedTextColor.RED));
                    }
                }

                case 50 -> upgradeMinion(player, targetMinion);

                // Pickup Minion (Bedrock) -> Returns Minion Head Item
                case 52 -> {
                    player.closeInventory();
                    for (Entity entity : player.getNearbyEntities(4, 4, 4)) {
                        if (entity instanceof ArmorStand stand && stand.getCustomName() != null) {
                            stand.remove();
                            break;
                        }
                    }

                    // Return Minion Item back to Player Inventory
                    ItemStack minionHead = ItemUtil.createMinionItem(targetMinion.getType(), targetMinion.getLevel());
                    player.getInventory().addItem(minionHead);

                    plugin.getMinionManager().removeMinion(targetMinion.getMinionId());
                    player.sendMessage(Component.text("Picked up " + targetMinion.getType() + " Minion Lv." + targetMinion.getLevel() + "!", NamedTextColor.YELLOW));
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

            for (Entity entity : player.getNearbyEntities(4, 4, 4)) {
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
                            
