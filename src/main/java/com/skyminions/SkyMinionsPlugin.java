package com.skyminions;

import com.skyminions.commands.MinionCommand;
import com.skyminions.events.MinionListener;
import com.skyminions.gui.GUIListener;
import com.skyminions.gui.GUIManager;
import com.skyminions.hooks.PluginHooks;
import com.skyminions.managers.MinionManager;
import com.skyminions.tasks.MinionTask;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkyMinionsPlugin extends JavaPlugin {

    private static SkyMinionsPlugin instance;
    private MinionManager minionManager;
    private GUIManager guiManager;
    private PluginHooks pluginHooks;
    private MinionTask minionTask;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.minionManager = new MinionManager(this);
        this.guiManager = new GUIManager(this);
        this.pluginHooks = new PluginHooks(this);

        // Start background work task (runs every 3 seconds)
        this.minionTask = new MinionTask(this);
        this.minionTask.runTaskTimer(this, 60L, 60L);

        // Register Events & Listeners
        getServer().getPluginManager().registerEvents(new MinionListener(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);

        // Register Commands
        MinionCommand minionCommand = new MinionCommand(this);
        if (getCommand("minion") != null) {
            getCommand("minion").setExecutor(minionCommand);
            getCommand("minion").setTabCompleter(minionCommand);
        }

        getLogger().info("SkyMinions 1.21 full release build ready!");
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

    public GUIManager getGuiManager() {
        return guiManager;
    }

    public PluginHooks getPluginHooks() {
        return pluginHooks;
    }
            }
        
