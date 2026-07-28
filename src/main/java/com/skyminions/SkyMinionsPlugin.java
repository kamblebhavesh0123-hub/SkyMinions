package com.skyminions;

import com.skyminions.managers.MinionManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkyMinionsPlugin extends JavaPlugin {

    private static SkyMinionsPlugin instance;
    private MinionManager minionManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.minionManager = new MinionManager(this);

        getLogger().info("SkyMinions 1.21 core managers initialized successfully!");
    }

    @Override
    public void onDisable() {
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
