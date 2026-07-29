package com.skyminions.commands;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.commands.sub.BenchmarkSubCommand;
import com.skyminions.commands.sub.GiveSubCommand;
import com.skyminions.commands.sub.ReloadSubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinionCommand implements CommandExecutor, TabCompleter {

    private final SkyMinionsPlugin plugin;
    private final Map<String, MinionSubCommand> subCommands = new HashMap<>();

    public MinionCommand(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
        subCommands.put("give", new GiveSubCommand());
        subCommands.put("reload", new ReloadSubCommand());
        subCommands.put("benchmark", new BenchmarkSubCommand());
    }

    public MinionCommand() {
        this(SkyMinionsPlugin.getInstance());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§eSkyMinions v1.0.0 by Bhavesh - Type /minion help");
            return true;
        }

        MinionSubCommand sub = subCommands.get(args[0].toLowerCase());
        if (sub != null) {
            sub.execute(sender, args);
        } else {
            sender.sendMessage("§cUnknown subcommand! Use /minion give, /minion reload, or /minion benchmark.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(subCommands.keySet());
        }
        MinionSubCommand sub = subCommands.get(args[0].toLowerCase());
        if (sub != null) {
            return sub.tabComplete(sender, args);
        }
        return Collections.emptyList();
    }
                }
