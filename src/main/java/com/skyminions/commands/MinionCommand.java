package com.skyminions.commands;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.util.ItemUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MinionCommand implements CommandExecutor, TabCompleter {

    private final SkyMinionsPlugin plugin;

    public MinionCommand(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelpMenu(sender);
            return true;
        }

        // Subcommand: /minion give <player> <type> [level]
        if (args[0].equalsIgnoreCase("give")) {
            if (!sender.hasPermission("skyminions.admin")) {
                sender.sendMessage(Component.text("No permission!", NamedTextColor.RED));
                return true;
            }

            if (args.length < 3) {
                sender.sendMessage(Component.text("Usage: /minion give <player> <type> [level]", NamedTextColor.RED));
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(Component.text("Player not found!", NamedTextColor.RED));
                return true;
            }

            String type = args[2].toUpperCase();
            int level = args.length > 3 ? Integer.parseInt(args[3]) : 1;

            target.getInventory().addItem(ItemUtil.createMinionItem(type, level));
            sender.sendMessage(Component.text("Gave " + type + " Minion Lv." + level + " to " + target.getName(), NamedTextColor.GREEN));
            target.sendMessage(Component.text("You received a " + type + " Minion!", NamedTextColor.AQUA));
            return true;
        }

        // Subcommand: /minion shop
        if (args[0].equalsIgnoreCase("shop")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only players can open the shop!");
                return true;
            }

            plugin.getGuiManager().openShopMenu(player);
            return true;
        }

        // Subcommand: /minion reload
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("skyminions.admin")) {
                sender.sendMessage(Component.text("No permission!", NamedTextColor.RED));
                return true;
            }

            plugin.reloadConfig();
            sender.sendMessage(Component.text("SkyMinions config reloaded!", NamedTextColor.GREEN));
            return true;
        }

        sendHelpMenu(sender);
        return true;
    }

    private void sendHelpMenu(CommandSender sender) {
        sender.sendMessage(Component.text("------------ [ SkyMinions Help ] ------------", NamedTextColor.AQUA, TextDecoration.BOLD));
        sender.sendMessage(Component.text("/minion shop ", NamedTextColor.YELLOW)
                .append(Component.text("- Open the Minion Shop GUI", NamedTextColor.GRAY)));
        
        if (sender.hasPermission("skyminions.admin")) {
            sender.sendMessage(Component.text("/minion give <player> <type> [level] ", NamedTextColor.YELLOW)
                    .append(Component.text("- Give a minion item", NamedTextColor.GRAY)));
            sender.sendMessage(Component.text("/minion reload ", NamedTextColor.YELLOW)
                    .append(Component.text("- Reload plugin configs", NamedTextColor.GRAY)));
        }
        sender.sendMessage(Component.text("--------------------------------------------", NamedTextColor.AQUA, TextDecoration.BOLD));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("shop");
            if (sender.hasPermission("skyminions.admin")) {
                completions.add("give");
                completions.add("reload");
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            completions.add("COBBLESTONE");
            completions.add("WHEAT");
            completions.add("OAK");
            completions.add("ZOMBIE");
        }

        return completions;
    }
                    }
    
