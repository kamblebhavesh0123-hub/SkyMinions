package com.skyminions.managers;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.api.events.MinionUpgradeEvent;
import com.skyminions.models.Minion;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class UpgradeManager {

    private final SkyMinionsPlugin plugin;
    private Economy vaultEconomy;

    public UpgradeManager(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
        setupVault();
    }

    private void setupVault() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) return;
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            this.vaultEconomy = rsp.getProvider();
        }
    }

    public boolean tryUpgradeMinion(Player player, Minion minion) {
        int currentLevel = minion.getLevel();
        int nextLevel = currentLevel + 1;

        double cost = nextLevel * 500.0;

        // Vault check
        if (vaultEconomy != null) {
            double balance = vaultEconomy.getBalance(player);
            if (balance < cost) {
                player.sendMessage("§cYou don't have enough money! Required: §6$" + cost);
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                return false;
            }
            vaultEconomy.withdrawPlayer(player, cost);
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
