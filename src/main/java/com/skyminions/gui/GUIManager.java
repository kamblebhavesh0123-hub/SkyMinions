package com.skyminions.gui;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.models.Minion;
import com.skyminions.models.MinionConfig;
import com.skyminions.util.ItemUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GUIManager {

    private final SkyMinionsPlugin plugin;

    public GUIManager(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
    }

    public void openShopMenu(Player player) {
        Component title = Component.text("Minion Shop", NamedTextColor.DARK_GRAY, TextDecoration.BOLD);
        Inventory gui = Bukkit.createInventory(null, 27, title);

        ItemStack border = createGuiItem(Material.GRAY_STAINED_GLASS_PANE, Component.text(" ", NamedTextColor.GRAY));
        for (int i = 0; i < 27; i++) {
            gui.setItem(i, border);
        }

        // Dynamically add all configured minions into the shop
        for (MinionConfig minionConfig : plugin.getConfigManager().getAllConfigs().values()) {
            if (minionConfig.getShopSlot() >= 0 && minionConfig.getShopSlot() < 27) {
                gui.setItem(minionConfig.getShopSlot(), ItemUtil.createMinionItem(minionConfig.getType(), 1));
            }
        }

        player.openInventory(gui);
    }

    public void openMainMenu(Player player, Minion minion) {
        MinionConfig config = plugin.getConfigManager().getMinionConfig(minion.getType());
        String romanLevel = toRoman(minion.getLevel());
        Component title = Component.text(minion.getType() + " Minion " + romanLevel, NamedTextColor.DARK_GRAY);
        Inventory gui = Bukkit.createInventory(null, 54, title);

        ItemStack border = createGuiItem(Material.GRAY_STAINED_GLASS_PANE, Component.text(" ", NamedTextColor.GRAY));
        for (int i = 0; i < 54; i++) {
            gui.setItem(i, border);
        }

        gui.setItem(4, createGuiItem(Material.REDSTONE_TORCH, 
                Component.text("Minion Info", NamedTextColor.RED, TextDecoration.BOLD),
                Component.text("Speed: ", NamedTextColor.GRAY).append(Component.text(minion.hasFuel() ? "7s (2x Fuel Boost)" : "14s", NamedTextColor.GREEN)),
                Component.text("Stored: ", NamedTextColor.GRAY).append(Component.text(minion.getStoredAmount() + " items", NamedTextColor.YELLOW))));

        gui.setItem(5, createGuiItem(Material.PLAYER_HEAD, 
                Component.text(minion.getType() + " Minion", NamedTextColor.AQUA, TextDecoration.BOLD)));

        gui.setItem(10, createGuiItem(Material.LIME_STAINED_GLASS_PANE, Component.text("Auto Sell Slot", NamedTextColor.GREEN, TextDecoration.BOLD)));

        if (minion.hasFuel()) {
            gui.setItem(19, createGuiItem(Material.COAL, 
                    Component.text("Active Fuel: Coal (+100% Speed)", NamedTextColor.GOLD, TextDecoration.BOLD),
                    Component.text("Click to remove fuel.", NamedTextColor.GRAY)));
        } else {
            gui.setItem(19, createGuiItem(Material.ORANGE_STAINED_GLASS_PANE, 
                    Component.text("Fuel Slot", NamedTextColor.GOLD, TextDecoration.BOLD),
                    Component.text("Click with Coal in inventory to insert fuel!", NamedTextColor.GRAY)));
        }

        gui.setItem(28, createGuiItem(Material.BLUE_STAINED_GLASS_PANE, Component.text("Compactor Slot", NamedTextColor.BLUE, TextDecoration.BOLD)));
        gui.setItem(37, createGuiItem(Material.YELLOW_STAINED_GLASS_PANE, Component.text("Upgrade Slot", NamedTextColor.YELLOW, TextDecoration.BOLD)));

        int[] storageSlots = {21, 22, 23, 24, 25, 30, 31, 32, 33, 34, 39, 40, 41, 42, 43};
        int remainingItems = minion.getStoredAmount();
        Material resourceMat = config != null ? config.getResourceMaterial() : Material.COBBLESTONE;

        for (int slot : storageSlots) {
            if (remainingItems > 0) {
                int stackAmount = Math.min(remainingItems, 64);
                gui.setItem(slot, new ItemStack(resourceMat, stackAmount));
                remainingItems -= stackAmount;
            } else {
                gui.setItem(slot, createGuiItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, Component.text("Empty Storage Slot", NamedTextColor.GRAY)));
            }
        }

        gui.setItem(48, createGuiItem(Material.CHEST, Component.text("Collect All", NamedTextColor.GREEN, TextDecoration.BOLD)));
        gui.setItem(50, createGuiItem(Material.DIAMOND, Component.text("Next Tier Upgrade", NamedTextColor.AQUA, TextDecoration.BOLD)));
        gui.setItem(52, createGuiItem(Material.BEDROCK, Component.text("Pickup Minion", NamedTextColor.RED, TextDecoration.BOLD)));

        player.openInventory(gui);
    }

    private ItemStack createGuiItem(Material material, Component name, Component... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(name);
            if (lore.length > 0) meta.lore(List.of(lore));
            item.setItemMeta(meta);
        }
        return item;
    }

    private static String toRoman(int number) {
        return switch (number) {
            case 1 -> "I"; case 2 -> "II"; case 3 -> "III"; case 4 -> "IV"; case 5 -> "V";
            default -> String.valueOf(number);
        };
    }
                                      }
                               
