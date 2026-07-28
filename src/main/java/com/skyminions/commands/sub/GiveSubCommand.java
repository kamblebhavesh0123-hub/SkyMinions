package com.skyminions.commands.sub;

import com.skyminions.commands.MinionSubCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class GiveSubCommand implements MinionSubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("skyminions.admin.give")) {
            sender.sendMessage("§cYou don't have permission to use this command.");
            return;
        }
        sender.sendMessage("§a[SkyMinions] Minion give command executed!");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
