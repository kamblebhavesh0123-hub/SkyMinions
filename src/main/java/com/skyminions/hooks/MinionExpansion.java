package com.skyminions.hooks;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.models.Minion;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class MinionExpansion extends PlaceholderExpansion {

    private final SkyMinionsPlugin plugin;

    public MinionExpansion(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "skyminions";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Bhavesh";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return "";

        // %skyminions_total_minions%
        if (params.equalsIgnoreCase("total_minions")) {
            long count = plugin.getMinionManager().getAllMinions().stream()
                    .filter(m -> m.getOwnerId().equals(player.getUniqueId()))
                    .count();
            return String.valueOf(count);
        }

        // %skyminions_items_generated%
        if (params.equalsIgnoreCase("items_generated")) {
            long total = plugin.getMinionManager().getAllMinions().stream()
                    .filter(m -> m.getOwnerId().equals(player.getUniqueId()))
                    .mapToLong(Minion::getTotalItemsGenerated)
                    .sum();
            return String.valueOf(total);
        }

        return null;
    }
  }
