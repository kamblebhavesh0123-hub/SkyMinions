package com.skyminions.api.events;

import com.skyminions.models.Minion;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MinionUpgradeEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Minion minion;
    private final int oldLevel;
    private final int newLevel;
    private boolean cancelled;

    public MinionUpgradeEvent(Minion minion, int oldLevel, int newLevel) {
        this.minion = minion;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    public Minion getMinion() {
        return minion;
    }

    public int getOldLevel() {
        return oldLevel;
    }

    public int getNewLevel() {
        return newLevel;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
                              }
