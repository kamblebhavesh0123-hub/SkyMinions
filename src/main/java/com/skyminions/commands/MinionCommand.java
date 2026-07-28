package com.skyminions.commands;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.minions.MinionEntity;
import com.skyminions.models.Minion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MinionCommand implements CommandExecutor, TabCompleter {

    private final SkyMinionsPlugin plugin;

    public MinionCommand(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // If player types just /minion with no arguments, display the help menu automatically
        if (args.length == 0) {
            sendHelpMenu(sender);
            return true;
        }

        // Subcommand: /minion spawn <type>
        if (args[0].equalsIgnoreCase("spawn")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only players can spawn minions!");
                return true;
            }

            if (!player.hasPermission("skyminions.admin")) {
                player.sendMessage(Component.text("You do not have permission to spawn minions!", NamedTextColor.RED));
                return true;
            }

            String type = args.length > 1 ? args[1].toUpperCase() : "COBBLESTONE";
            Minion minion = new Minion(UUID.randomUUID(), player.getUniqueId(), type, 1, player.getLocation());

            plugin.getMinionManager().addMinion(minion);
            MinionEntity.spawnMinionStand(minion);

            player.sendMessage(Component.text("Successfully spawned a ", NamedTextColor.GREEN)
                    .append(Component.text(type, NamedTextColor.AQUA))
                    .append(Component.text(" minion!", NamedTextColor.GREEN)));
            return true;
        }

        // Subcommand: /minion reload
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("skyminions.admin")) {
                sender.sendMessage(Component.text("You do not have permission to reload SkyMinions!", NamedTextColor.RED));
                return true;
            }

            plugin.reloadConfig();
            sender.sendMessage(Component.text("SkyMinions configuration reloaded successfully!", NamedTextColor.GREEN));
            return true;
        }

        // Subcommand: /minion list
        if (args[0].equalsIgnoreCase("list")) {
            int count = plugin.getMinionManager().getAllMinions().size();
            sender.sendMessage(Component.text("Active Minions on Server: ", NamedTextColor.YELLOW)
                    .append(Component.text(count, NamedTextColor.GREEN)));
            return true;
        }

        // If unknown argument, show help menu
        sendHelpMenu(sender);
        return true;
    }

    // Formatted Help Menu displayed on /minion
    private void sendHelpMenu(CommandSender sender) {
        sender.sendMessage(Component.text("------------ [ SkyMinions Help ] ------------", NamedTextColor.AQUA, TextDecoration.BOLD));
        sender.sendMessage(Component.text("/minion ", NamedTextColor.YELLOW)
                .append(Component.text("- Display this help menu", NamedTextColor.GRAY)));
        sender.sendMessage(Component.text("/minion spawn <type> ", NamedTextColor.YELLOW)
                .append(Component.text("- Spawn a minion at your location", NamedTextColor.GRAY)));
        sender.sendMessage(Component.text("/minion list ", NamedTextColor.YELLOW)
                .append(Component.text("- View total active minions on server", NamedTextColor.GRAY)));
        
        if (sender.hasPermission("skyminions.admin")) {
            sender.sendMessage(Component.text("/minion reload ", NamedTextColor.YELLOW)
                    .append(Component.text("- Reload plugin configuration", NamedTextColor.GRAY)));
        }
        sender.sendMessage(Component.text("--------------------------------------------", NamedTextColor.AQUA, TextDecoration.BOLD));
    }

    // Auto-completion for in-game command typing tab completion
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("spawn");
            completions.add("list");
            if (sender.hasPermission("skyminions.admin")) {
                completions.add("reload");
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("spawn")) {
            completions.add("COBBLESTONE");
            completions.add("WHEAT");
            completions.add("OAK");
            completions.add("ZOMBIE");
        }

        return completions;
    }
            }
                                              
