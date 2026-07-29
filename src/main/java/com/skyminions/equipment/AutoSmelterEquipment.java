package com.skyminions.equipment;

import com.skyminions.api.events.MinionGenerateEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AutoSmelterEquipment extends MinionEquipment {

    public AutoSmelterEquipment() {
        super("auto_smelter", "Auto Smelter");
    }

    @Override
    public void onGenerate(MinionGenerateEvent event) {
        List<ItemStack> newItems = new ArrayList<>();

        for (ItemStack item : event.getGeneratedItems()) {
            Material smeltedType = getSmeltedMaterial(item.getType());
            if (smeltedType != null) {
                newItems.add(new ItemStack(smeltedType, item.getAmount()));
            } else {
                newItems.add(item);
            }
        }

        event.setGeneratedItems(newItems);
    }

    private Material getSmeltedMaterial(Material raw) {
        return switch (raw) {
            case RAW_IRON, IRON_ORE, DEEPSLATE_IRON_ORE -> Material.IRON_INGOT;
            case RAW_GOLD, GOLD_ORE, DEEPSLATE_GOLD_ORE -> Material.GOLD_INGOT;
            case RAW_COPPER, COPPER_ORE, DEEPSLATE_COPPER_ORE -> Material.COPPER_INGOT;
            case COBBLESTONE -> Material.STONE;
            case SAND -> Material.GLASS;
            case OAK_LOG, BIRCH_LOG, SPRUCE_LOG, JUNGLE_LOG, ACACIA_LOG, DARK_OAK_LOG -> Material.CHARCOAL;
            default -> null;
        };
    }
    }
  
