package com.skyminions.api.events;

import com.skyminions.models.Minion;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MinionGenerateEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Minion minion;
    private List<ItemStack> generatedItems;
    private boolean cancelled;

    public MinionGenerateEvent(Minion minion, List<ItemStack> generatedItems) {
        this.minion = minion;
        this.generatedItems = generatedItems;
    }

    public Minion getMinion() { return minion; }
    public List<ItemStack> getGeneratedItems() { return generatedItems; }
    public void setGeneratedItems(List<ItemStack> generatedItems) { this.generatedItems = generatedItems; }

    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
      }
