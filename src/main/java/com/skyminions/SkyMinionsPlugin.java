package com.skyminions;

import com.skyminions.commands.MinionCommand;
import com.skyminions.config.MinionConfigManager;
import com.skyminions.gui.GUIManager;
import com.skyminions.managers.MinionManager;
import com.skyminions.tasks.MinionTickerTask;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyMinionsPlugin extends JavaPlugin {

    private static SkyMinionsPlugin instance;
    private MinionConfigManager configManager;
    private MinionManager minionManager;
    private GUIManager guiManager;

    @Override
    public void onEnable() {
        instance = this;

        // Configurations & Managers
        this.configManager = new MinionConfigManager(this);
        this.configManager.loadConfigs();

        this.minionManager = new MinionManager(this);
        this.guiManager = new GUIManager(this);

        // Register Command Executor & Tab Completer
        MinionCommand minionCommand = new MinionCommand(this);
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
            minionManager.saveMinions();
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

    public GUIManager getGuiManager() {
        return guiManager;
    }
}
