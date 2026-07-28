package com.skyminions.minions;

import com.skyminions.models.Minion;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class MinionEntity {

    public static ArmorStand spawnMinionStand(Minion minion) {
        Location loc = minion.getLocation();
        if (loc.getWorld() == null) return null;

        ArmorStand stand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        stand.setGravity(false);
        stand.setArms(true);
        stand.setBasePlate(false);
        stand.setSmall(true);
        stand.setCustomNameVisible(true);
        stand.setCustomName("§b" + minion.getType() + " Minion §7[Lv." + minion.getLevel() + "]");

        return stand;
    }
}
