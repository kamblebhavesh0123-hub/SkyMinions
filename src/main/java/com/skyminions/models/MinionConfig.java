package com.skyminions.models;

import org.bukkit.Color;
import org.bukkit.Material;

public class MinionConfig {
    private String type;
    private String displayName;
    private Material resourceMaterial;
    private Material toolMaterial;
    private Material heldItemFallback;
    private String headTexture;
    private Color chestplateColor;
    private int shopSlot;

    public MinionConfig(String type, String displayName, Material resourceMaterial, Material toolMaterial, 
                        Material heldItemFallback, String headTexture, Color chestplateColor, int shopSlot) {
        this.type = type;
        this.displayName = displayName;
        this.resourceMaterial = resourceMaterial;
        this.toolMaterial = toolMaterial;
        this.heldItemFallback = heldItemFallback;
        this.headTexture = headTexture;
        this.chestplateColor = chestplateColor;
        this.shopSlot = shopSlot;
    }

    public String getType() { return type; }
    public String getDisplayName() { return displayName; }
    public Material getResourceMaterial() { return resourceMaterial; }
    public Material getToolMaterial() { return toolMaterial; }
    public Material getHeldItemFallback() { return heldItemFallback; }
    public String getHeadTexture() { return headTexture; }
    public Color getChestplateColor() { return chestplateColor; }
    public int getShopSlot() { return shopSlot; }
}
