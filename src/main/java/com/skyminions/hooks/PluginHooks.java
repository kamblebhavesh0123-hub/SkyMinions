package com.skyminions.hooks;

import com.skyminions.SkyMinionsPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PluginHooks {

    private final SkyMinionsPlugin plugin;
    private Economy vaultEconomy;
    private boolean placeholderApiEnabled;

    public PluginHooks(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
        setupVault();
        setupPlaceholderAPI();
    }

    private void setupVault() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().info("Vault not found. Economy features will be disabled.");
            return;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            vaultEconomy = rsp.getProvider();
            plugin.getLogger().info("Successfully hooked into Vault Economy!");
        }
    }

    private void setupPlaceholderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholderApiEnabled = true;
            new MinionExpansion(plugin).register();
            plugin.getLogger().info("Successfully hooked into PlaceholderAPI!");
        }
    }

    public Economy getVaultEconomy() {
        return vaultEconomy;
    }

    public boolean hasVault() {
        return vaultEconomy != null;
    }

    public boolean isPlaceholderApiEnabled() {
        return placeholderApiEnabled;
    }
}
