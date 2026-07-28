package com.skyminions.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemUtil {

    public static ItemStack createMinionItem(String type, int level) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(Component.text(type + " Minion " + toRoman(level), NamedTextColor.GREEN, TextDecoration.BOLD));
            meta.lore(List.of(
                    Component.text("Place this minion down to start", NamedTextColor.GRAY),
                    Component.text("generating resources automatically!", NamedTextColor.GRAY),
                    Component.text("Level: ", NamedTextColor.GRAY).append(Component.text(level, NamedTextColor.YELLOW))
            ));
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
      
