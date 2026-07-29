package com.skyminions.equipment;

import com.skyminions.api.events.MinionGenerateEvent;

public abstract class MinionEquipment {

    private final String id;
    private final String name;

    public MinionEquipment(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    public abstract void onGenerate(MinionGenerateEvent event);
      }
