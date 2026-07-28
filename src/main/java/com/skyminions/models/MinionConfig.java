package com.skyminions.models;

import org.bukkit.Color;
import org.bukkit.Material;

public class MinionConfig {

    private final String type;
    private final String displayName;
    private final Material resourceMaterial;
    private final Material toolMaterial;
    private final String headTexture;
    private final Color armorColor;
    private final int shopSlot;

    public MinionConfig(String type, String displayName, Material resourceMaterial, Material toolMaterial, String headTexture, Color armorColor, int shopSlot) {
        this.type = type;
        this.displayName = displayName;
        this.resourceMaterial = resourceMaterial;
        this.toolMaterial = toolMaterial;
        this.headTexture = headTexture;
        this.armorColor = armorColor;
        this.shopSlot = shopSlot;
    }

    public String getType() { return type; }
    public String getDisplayName() { return displayName; }
    public Material getResourceMaterial() { return resourceMaterial; }
    public Material getToolMaterial() { return toolMaterial; }
    public String getHeadTexture() { return headTexture; }
    public Color getArmorColor() { return armorColor; }
    public int getShopSlot() { return shopSlot; }
  }
