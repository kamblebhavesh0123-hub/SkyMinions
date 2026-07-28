package com.skyminions.gui;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.models.Minion;
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

    public void openMainMenu(Player player, Minion minion) {
        Component title = Component.text(minion.getType() + " Minion [Lv." + minion.getLevel() + "]", NamedTextColor.DARK_GRAY);
        Inventory gui = Bukkit.createInventory(null, 54, title);

        // Fill background with glass panes
        ItemStack filler = createGuiItem(Material.GRAY_STAINED_GLASS_PANE, Component.text(" ", NamedTextColor.GRAY));
        for (int i = 0; i < 54; i++) {
            gui.setItem(i, filler);
        }

        // Action Slots
        gui.setItem(21, createGuiItem(Material.CHEST, 
                Component.text("Collect Items", NamedTextColor.GREEN, TextDecoration.BOLD),
                Component.text("Click to collect stored resources.", NamedTextColor.GRAY)));

        gui.setItem(23, createGuiItem(Material.ANVIL, 
                Component.text("Upgrade Minion", NamedTextColor.GOLD, TextDecoration.BOLD),
                Component.text("Current Level: ", NamedTextColor.GRAY).append(Component.text(minion.getLevel(), NamedTextColor.YELLOW)),
                Component.text("Click to upgrade speed & storage.", NamedTextColor.GRAY)));

        gui.setItem(30, createGuiItem(Material.BLAZE_POWDER, 
                Component.text("Fuel Slot", NamedTextColor.RED, TextDecoration.BOLD),
                Component.text("Add fuel to increase minion speed.", NamedTextColor.GRAY)));

        gui.setItem(32, createGuiItem(Material.DIAMOND_PICKAXE, 
                Component.text("Equipment Slot", NamedTextColor.AQUA, TextDecoration.BOLD),
                Component.text("Attach compactors or auto-smelters.", NamedTextColor.GRAY)));

        gui.setItem(49, createGuiItem(Material.REDSTONE_BLOCK, 
                Component.text("Pick Up Minion", NamedTextColor.RED, TextDecoration.BOLD),
                Component.text("Reclaim this minion into your inventory.", NamedTextColor.GRAY)));

        player.openInventory(gui);
    }

    private ItemStack createGuiItem(Material material, Component name, Component... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(name);
            if (lore.length > 0) {
                meta.lore(List.of(lore));
            }
            item.setItemMeta(meta);
        }
        return item;
    }
}
