package com.gdogaru.spacescoop.work;

import androidx.annotation.NonNull;
import androidx.work.WorkInfo;

public enum StaleState {
    LOADING(false),
    SUCCESS(true),
    ERROR(true);

    private boolean endState;

    StaleState(boolean endState) {
        this.endState = endState;
    }

    public static StaleState from(@NonNull WorkInfo.State state) {
        switch (state) {
            case BLOCKED:
            case ENQUEUED:
            case RUNNING:
                return LOADING;
            case SUCCEEDED:
                return SUCCESS;
            case FAILED:
            case CANCELLED:
                return ERROR;
        }
        throw new IllegalStateException("Unknown state" + state);
    }

    public boolean isEndState() {
        return endState;
    }
}
