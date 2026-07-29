package com.skyminions.gui;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.events.GUIListener;
import com.skyminions.models.Minion;
import org.bukkit.entity.Player;

public class GUIManager {

    private final SkyMinionsPlugin plugin;
    private final MinionGUI minionGUI;

    public GUIManager(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
        this.minionGUI = new MinionGUI(plugin);
        
        // Register GUI click listener
        plugin.getServer().getPluginManager().registerEvents(new GUIListener(plugin), plugin);
    }

    public void openMainMenu(Player player, Minion minion) {
        minionGUI.openMainMenu(player, minion);
    }

    public MinionGUI getMinionGUI() {
        return minionGUI;
    }
}
