/*
 * Copyright (c) 2019 Gabriel Dogaru - gdogaru@gmail.com
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

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
