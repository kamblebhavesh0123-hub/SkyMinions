package com.skyminions.storage;

import com.skyminions.models.Minion;

import java.util.Collection;
import java.util.UUID;

public interface StorageRepository {

    void init();

    void saveMinion(Minion minion);

    void saveAllMinions(Collection<Minion> minions);

    Minion loadMinion(UUID minionId);

    Collection<Minion> loadAllMinions();

    void deleteMinion(UUID minionId);

    void close();
}
