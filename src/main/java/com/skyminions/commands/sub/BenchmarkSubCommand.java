package com.skyminions.commands.sub;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.commands.MinionSubCommand;
import com.skyminions.models.Minion;
import com.skyminions.models.state.MinionState;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class BenchmarkSubCommand implements MinionSubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("skyminions.admin.benchmark")) {
            sender.sendMessage("§cYou don't have permission to run performance benchmarks.");
            return;
        }

        SkyMinionsPlugin plugin = SkyMinionsPlugin.getInstance();
        List<Minion> minions = plugin.getMinionManager().getAllMinions();

        long workingCount = minions.stream().filter(m -> m.getState() == MinionState.WORKING || m.getState() == MinionState.COLLECTING).count();
        long storageFullCount = minions.stream().filter(m -> m.getState() == MinionState.STORAGE_FULL).count();
        long idleCount = minions.stream().filter(m -> m.getState() == MinionState.IDLE).count();

        long totalMemoryMB = Runtime.getRuntime().totalMemory() / (1024 * 1024);
        long freeMemoryMB = Runtime.getRuntime().freeMemory() / (1024 * 1024);
        long usedMemoryMB = totalMemoryMB - freeMemoryMB;

        sender.sendMessage("§e=== SkyMinions Performance Benchmark ===");
        sender.sendMessage("§fTotal Loaded Minions: §a" + minions.size());
        sender.sendMessage("§f  ↳ Working/Collecting: §e" + workingCount);
        sender.sendMessage("§f  ↳ Storage Full: §c" + storageFullCount);
        sender.sendMessage("§f  ↳ Idle: §7" + idleCount);
        sender.sendMessage("§fJVM Memory Usage: §b" + usedMemoryMB + "MB / " + totalMemoryMB + "MB");
        sender.sendMessage("§e=========================================");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
          }
          
