package com.skyminions.commands.sub;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.commands.MinionSubCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ReloadSubCommand implements MinionSubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("skyminions.admin.reload")) {
            sender.sendMessage("§cYou don't have permission to reload the plugin.");
            return;
        }
        SkyMinionsPlugin.getInstance().getConfigManager().loadConfigs();
        sender.sendMessage("§a[SkyMinions] Configurations reloaded successfully!");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
