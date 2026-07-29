package com.skyminions;

import com.skyminions.commands.MinionCommand;
import com.skyminions.events.MinionListener;
import com.skyminions.gui.GUIManager;
import com.skyminions.hooks.PluginHooks;
import com.skyminions.managers.ConfigManager;
import com.skyminions.managers.FuelManager;
import com.skyminions.managers.HologramManager;
import com.skyminions.managers.MinionManager;
import com.skyminions.managers.UpgradeManager;
import com.skyminions.models.Minion;
import com.skyminions.tasks.MinionTickerTask;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyMinionsPlugin extends JavaPlugin {

    private static SkyMinionsPlugin instance;
    private ConfigManager configManager;
    private MinionManager minionManager;
    private GUIManager guiManager;
    private UpgradeManager upgradeManager;
    private FuelManager fuelManager;
    private HologramManager hologramManager;
    private PluginHooks pluginHooks;
    private MinionTickerTask tickerTask;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        // Initialize Config & Managers
        this.configManager = new ConfigManager(this);
        this.pluginHooks = new PluginHooks(this);
        this.minionManager = new MinionManager(this);
        this.guiManager = new GUIManager(this);
        this.upgradeManager = new UpgradeManager(this);
        this.fuelManager = new FuelManager(this);
        this.hologramManager = new HologramManager(this);

        // Calculate Offline Production
        calculateOfflineProduction();

        // Register Commands & Events
        if (getCommand("minion") != null) {
            MinionCommand minionCmd = new MinionCommand(this);
            getCommand("minion").setExecutor(minionCmd);
            getCommand("minion").setTabCompleter(minionCmd);
        }

        getServer().getPluginManager().registerEvents(new MinionListener(this), this);

        // Start High-Performance FSM Ticker Task
        this.tickerTask = new MinionTickerTask(this);
        this.tickerTask.runTaskTimer(this, 20L, 20L);

        getLogger().info("SkyMinions v" + getDescription().getVersion() + " successfully enabled!");
    }

    @Override
    public void onDisable() {
        if (tickerTask != null) {
            tickerTask.cancel();
        }
        if (hologramManager != null) {
            hologramManager.removeAll();
        }
        if (minionManager != null) {
            minionManager.saveMinions();
        }
        getLogger().info("SkyMinions successfully disabled.");
    }

    private void calculateOfflineProduction() {
        long now = System.currentTimeMillis();
        int totalGenerated = 0;

        for (Minion minion : minionManager.getAllMinions()) {
            long lastActive = minion.getLastActiveTimestamp();
            if (lastActive <= 0) continue;

            long elapsedSeconds = (now - lastActive) / 1000;
            if (elapsedSeconds < 10) continue; // Skip short offline windows

            // Calculate offline items generated based on speed
            long interval = (long) (5 / minion.getSpeedMultiplier());
            if (interval < 1) interval = 1;

            int itemsToProduce = (int) (elapsedSeconds / interval);
            if (itemsToProduce > 0) {
                int capacity = minion.getStorage().getCapacity();
                int current = minion.getStoredAmount();
                int maxCanAdd = capacity - current;

                int actualAdd = Math.min(itemsToProduce, maxCanAdd);
                minion.setStoredAmount(current + actualAdd);
                minion.setLastActiveTimestamp(now);
                totalGenerated += actualAdd;
            }
        }

        if (totalGenerated > 0) {
            getLogger().info("Offline Engine: Calculated " + totalGenerated + " offline items across active minions!");
        }
    }

    public static SkyMinionsPlugin getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MinionManager getMinionManager() {
        return minionManager;
    }

    public GUIManager getGuiManager() {
        return guiManager;
    }

    public UpgradeManager getUpgradeManager() {
        return upgradeManager;
    }

    public FuelManager getFuelManager() {
        return fuelManager;
    }

    public HologramManager getHologramManager() {
        return hologramManager;
    }

    public PluginHooks getHooks() {
        return pluginHooks;
    }
}
