package com.skyminions;

import com.skyminions.commands.MinionCommand;
import com.skyminions.config.MinionConfigManager;
import com.skyminions.events.MinionListener;
import com.skyminions.gui.GUIListener;
import com.skyminions.gui.GUIManager;
import com.skyminions.minions.MinionManager;
import com.skyminions.tasks.MinionTask;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyMinionsPlugin extends JavaPlugin {

    private MinionManager minionManager;
    private GUIManager guiManager;
    private MinionConfigManager configManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.configManager = new MinionConfigManager(this);
        this.minionManager = new MinionManager();
        this.guiManager = new GUIManager(this);

        if (getCommand("minion") != null) {
            MinionCommand minionCommand = new MinionCommand(this);
            getCommand("minion").setExecutor(minionCommand);
            getCommand("minion").setTabCompleter(minionCommand);
        }

        getServer().getPluginManager().registerEvents(new MinionListener(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);

        new MinionTask(this).runTaskTimer(this, 60L, 60L);

        getLogger().info("SkyMinions Plugin Enabled Successfully!");
    }

    public MinionManager getMinionManager() { return minionManager; }
    public GUIManager getGuiManager() { return guiManager; }
    public MinionConfigManager getConfigManager() { return configManager; }
}
