package com.skyminions.commands;

import org.bukkit.command.CommandSender;
import java.util.List;

public interface MinionSubCommand {
    void execute(CommandSender sender, String[] args);
    List<String> tabComplete(CommandSender sender, String[] args);
}
