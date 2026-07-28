package com.skyminions.models;

import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;

public class MinionStorage {
    private int capacity;
    private int storedAmount;
    private final List<ItemStack> items = new ArrayList<>();

    public MinionStorage(int capacity) {
        this.capacity = capacity;
    }

    public boolean isFull() {
        return storedAmount >= capacity;
    }

    public boolean addItems(ItemStack stack) {
        if (isFull()) return false;
        this.storedAmount += stack.getAmount();
        this.items.add(stack);
        return true;
    }

    public void clear() {
        this.items.clear();
        this.storedAmount = 0;
    }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public int getStoredAmount() { return storedAmount; }
    public void setStoredAmount(int amount) { this.storedAmount = amount; }
          }
          
