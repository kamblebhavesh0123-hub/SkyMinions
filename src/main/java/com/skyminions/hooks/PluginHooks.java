package com.skyminions.hooks;

import com.skyminions.SkyMinionsPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PluginHooks {

    private final SkyMinionsPlugin plugin;
    private Economy economy = null;
    private boolean placeholderApiEnabled = false;

    public PluginHooks(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
        setupEconomy();
        setupPlaceholderAPI();
    }

    private void setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) return;
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            economy = rsp.getProvider();
            plugin.getLogger().info("Hooked into Vault Economy!");
        }
    }

    private void setupPlaceholderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholderApiEnabled = true;
            plugin.getLogger().info("Hooked into PlaceholderAPI!");
        }
    }

    public Economy getEconomy() {
        return economy;
    }

    public boolean isPlaceholderApiEnabled() {
        return placeholderApiEnabled;
    }
          }
            
