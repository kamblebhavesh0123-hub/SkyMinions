package com.skyminions.equipment;

import com.skyminions.api.events.MinionGenerateEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CompactorEquipment extends MinionEquipment {

    public CompactorEquipment() {
        super("compactor", "Compactor");
    }

    @Override
    public void onGenerate(MinionGenerateEvent event) {
        List<ItemStack> newItems = new ArrayList<>();

        for (ItemStack item : event.getGeneratedItems()) {
            Material compactedType = getCompactedMaterial(item.getType());
            if (compactedType != null && item.getAmount() >= 9) {
                int blocks = item.getAmount() / 9;
                int remainder = item.getAmount() % 9;

                newItems.add(new ItemStack(compactedType, blocks));
                if (remainder > 0) {
                    newItems.add(new ItemStack(item.getType(), remainder));
                }
            } else {
                newItems.add(item);
            }
        }

        event.setGeneratedItems(newItems);
    }

    private Material getCompactedMaterial(Material raw) {
        return switch (raw) {
            case COAL -> Material.COAL_BLOCK;
            case IRON_INGOT -> Material.IRON_BLOCK;
            case GOLD_INGOT -> Material.GOLD_BLOCK;
            case DIAMOND -> Material.DIAMOND_BLOCK;
            case EMERALD -> Material.EMERALD_BLOCK;
            case REDSTONE -> Material.REDSTONE_BLOCK;
            case LAPIS_LAZULI -> Material.LAPIS_BLOCK;
            case COPPER_INGOT -> Material.COPPER_BLOCK;
            case SLIME_BALL -> Material.SLIME_BLOCK;
            case WHEAT -> Material.HAY_BLOCK;
            default -> null;
        };
    }
            }
            
