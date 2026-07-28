package com.skyminions;

import com.skyminions.commands.MinionCommand;
import com.skyminions.events.MinionListener;
import com.skyminions.managers.MinionManager;
import com.skyminions.tasks.MinionTask;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkyMinionsPlugin extends JavaPlugin {

    private static SkyMinionsPlugin instance;
    private MinionManager minionManager;
    private MinionTask minionTask;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.minionManager = new MinionManager(this);

        // Start background work task (runs every 3 seconds)
        this.minionTask = new MinionTask(this);
        this.minionTask.runTaskTimer(this, 60L, 60L);

        // Register Events
        getServer().getPluginManager().registerEvents(new MinionListener(this), this);

        // Register Command Executor and Tab Completer
        MinionCommand minionCommand = new MinionCommand(this);
        if (getCommand("minion") != null) {
            getCommand("minion").setExecutor(minionCommand);
            getCommand("minion").setTabCompleter(minionCommand);
        }

        getLogger().info("SkyMinions 1.21 full core, events, and commands loaded!");
    }

    @Override
    public void onDisable() {
        if (minionTask != null) {
            minionTask.cancel();
        }
        if (minionManager != null) {
            minionManager.saveMinions();
        }
        getLogger().info("SkyMinions data saved and disabled gracefully.");
    }

    public static SkyMinionsPlugin getInstance() {
        return instance;
    }

    public MinionManager getMinionManager() {
        return minionManager;
    }
            }
    
