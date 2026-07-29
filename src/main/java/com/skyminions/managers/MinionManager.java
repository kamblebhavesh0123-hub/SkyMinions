package com.skyminions.managers;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.models.Minion;
import com.skyminions.storage.StorageRepository;
import com.skyminions.storage.YAMLStorageRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MinionManager {

    private final SkyMinionsPlugin plugin;
    private final Map<UUID, Minion> activeMinions = new HashMap<>();
    private final StorageRepository storageRepository;

    public MinionManager(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
        this.storageRepository = new YAMLStorageRepository(plugin);
        this.storageRepository.init();
        loadMinions();
    }

    public void registerMinion(Minion minion) {
        activeMinions.put(minion.getMinionId(), minion);
        storageRepository.saveMinion(minion);
    }

    public void removeMinion(UUID minionId) {
        activeMinions.remove(minionId);
        storageRepository.deleteMinion(minionId);
    }

    public Minion getMinion(UUID minionId) {
        return activeMinions.get(minionId);
    }

    public Collection<Minion> getAllMinions() {
        return activeMinions.values();
    }

    public void loadMinions() {
        activeMinions.clear();
        for (Minion minion : storageRepository.loadAllMinions()) {
            activeMinions.put(minion.getMinionId(), minion);
        }
    }

    public void saveMinions() {
        storageRepository.saveAllMinions(activeMinions.values());
    }

    public StorageRepository getStorageRepository() {
        return storageRepository;
    }
}
