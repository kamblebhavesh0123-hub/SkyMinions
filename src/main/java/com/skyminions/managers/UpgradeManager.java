package com.skyminions.managers;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.api.events.MinionUpgradeEvent;
import com.skyminions.models.Minion;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class UpgradeManager {

    private final SkyMinionsPlugin plugin;

    public UpgradeManager(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean tryUpgradeMinion(Player player, Minion minion) {
        int currentLevel = minion.getLevel();
        int nextLevel = currentLevel + 1;

        double cost = nextLevel * 500.0;

        // Vault Economy Integration check
        if (plugin.getPluginHooks() != null && plugin.getPluginHooks().hasVault()) {
            double balance = plugin.getPluginHooks().getVaultEconomy().getBalance(player);
            if (balance < cost) {
                player.sendMessage("§cYou don't have enough money! Required: §6$" + cost);
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                return false;
            }
            plugin.getPluginHooks().getVaultEconomy().withdrawPlayer(player, cost);
        }

        // Call event
        MinionUpgradeEvent upgradeEvent = new MinionUpgradeEvent(minion, currentLevel, nextLevel);
        Bukkit.getPluginManager().callEvent(upgradeEvent);

        if (upgradeEvent.isCancelled()) return false;

        // Apply Upgrade
        minion.setLevel(nextLevel);
        minion.setSpeedMultiplier(1.0 + (nextLevel * 0.15));
        plugin.getMinionManager().saveMinions();

        player.sendMessage("§a§lSUCCESS! §7Upgraded " + minion.getType() + " Minion to §eTier " + nextLevel + "§7!");
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);

        return true;
    }
                                 }
          
