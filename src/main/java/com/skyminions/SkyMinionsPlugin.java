package com.skyminions;

import com.skyminions.commands.MinionCommand;
import com.skyminions.config.MinionConfigManager;
import com.skyminions.managers.MinionManager;
import com.skyminions.tasks.MinionTickerTask;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyMinionsPlugin extends JavaPlugin {

    private static SkyMinionsPlugin instance;
    private MinionConfigManager configManager;
    private MinionManager minionManager;

    @Override
    public void onEnable() {
        instance = this;

        // Configurations & Managers
        this.configManager = new MinionConfigManager(this);
        this.configManager.loadConfigs();

        this.minionManager = new MinionManager(this);

        // Register Command Executor & Tab Completer
        MinionCommand minionCommand = new MinionCommand();
        if (getCommand("minion") != null) {
            getCommand("minion").setExecutor(minionCommand);
            getCommand("minion").setTabCompleter(minionCommand);
        }

        // Start Central Scheduler (Runs every 20 ticks = 1 second)
        new MinionTickerTask(this).runTaskTimer(this, 20L, 20L);

        getLogger().info("SkyMinions updated successfully!");
    }

    @Override
    public void onDisable() {
        if (minionManager != null) {
            minionManager.saveAllMinions();
        }
    }

    public static SkyMinionsPlugin getInstance() { 
        return instance; 
    }

    public MinionConfigManager getConfigManager() { 
        return configManager; 
    }

    public MinionManager getMinionManager() { 
        return minionManager; 
    }
}
