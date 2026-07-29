package com.skyminions.events;

import com.skyminions.api.events.MinionGenerateEvent;
import com.skyminions.equipment.AutoSmelterEquipment;
import com.skyminions.equipment.CompactorEquipment;
import com.skyminions.models.Minion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EquipmentListener implements Listener {

    private final AutoSmelterEquipment autoSmelter = new AutoSmelterEquipment();
    private final CompactorEquipment compactor = new CompactorEquipment();

    @EventHandler
    public void onMinionGenerate(MinionGenerateEvent event) {
        Minion minion = event.getMinion();

        if (minion.hasSmelter()) {
            autoSmelter.onGenerate(event);
        }

        if (minion.hasCompactor()) {
            compactor.onGenerate(event);
        }
    }
}
