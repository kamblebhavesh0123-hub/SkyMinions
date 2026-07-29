package com.skyminions.gui;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.models.Minion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MinionGUI {

    private final SkyMinionsPlugin plugin;

    public MinionGUI(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
    }

    public void openMainMenu(Player player, Minion minion) {
        String title = "§8Minion Overview - Tier " + minion.getLevel();
        Inventory gui = Bukkit.createInventory(null, 54, title);

        // Fill background
        ItemStack filler = createItem(Material.GRAY_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < 54; i++) {
            gui.setItem(i, filler);
        }

        // Info Item
        List<String> infoLore = new ArrayList<>();
        infoLore.add("§7Status: §a" + minion.getState().name());
        infoLore.add("§7Level: §a" + minion.getLevel());
        infoLore.add("§7Stored Items: §e" + minion.getStoredAmount() + "/" + minion.getStorage().getCapacity());
        infoLore.add("");
        infoLore.add("§eClick to collect items!");
        gui.setItem(13, createItem(Material.CHEST, "§e" + minion.getType() + " Minion", infoLore));

        // Upgrade Item
        List<String> upgradeLore = new ArrayList<>();
        upgradeLore.add("§7Next Tier: §aTier " + (minion.getLevel() + 1));
        upgradeLore.add("§7Cost: §6$" + (minion.getLevel() * 500));
        upgradeLore.add("");
        upgradeLore.add("§eClick to upgrade minion!");
        gui.setItem(29, createItem(Material.ANVIL, "§aUpgrade Minion", upgradeLore));

        // Fuel Item
        List<String> fuelLore = new ArrayList<>();
        fuelLore.add("§7Fuel Active: " + (minion.hasFuel() ? "§aYes" : "§cNo"));
        fuelLore.add("§7Speed Bonus: §a+" + (int)((minion.getSpeedMultiplier() - 1.0) * 100) + "%");
        fuelLore.add("");
        fuelLore.add("§eClick to manage fuel!");
        gui.setItem(31, createItem(Material.BLAZE_POWDER, "§cMinion Fuel", fuelLore));

        // Close Item
        gui.setItem(49, createItem(Material.BARRIER, "§cClose Menu"));

        player.openInventory(gui);
    }

    private ItemStack createItem(Material mat, String name, List<String> lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore != null) meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack createItem(Material mat, String name) {
        return createItem(mat, name, null);
    }
  }
  
