package com.skyminions.models.state;

import com.skyminions.models.Minion;

public class MinionStateMachine {

    private final Minion minion;
    private MinionState currentState = MinionState.IDLE;

    public MinionStateMachine(Minion minion) {
        this.minion = minion;
    }

    public MinionState getCurrentState() {
        return currentState;
    }

    public void setState(MinionState newState) {
        if (this.currentState == newState) return;
        this.currentState = newState;
    }

    public void evaluateState() {
        if (minion.getStorage() != null && minion.getStorage().isFull()) {
            setState(MinionState.STORAGE_FULL);
            return;
        }

        if (currentState == MinionState.STORAGE_FULL && !minion.getStorage().isFull()) {
            setState(MinionState.IDLE);
        }
    }
            }
