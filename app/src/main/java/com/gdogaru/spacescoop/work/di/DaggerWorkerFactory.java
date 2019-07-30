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

package com.gdogaru.spacescoop.work.di;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.ListenableWorker;
import androidx.work.WorkerFactory;
import androidx.work.WorkerParameters;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class DaggerWorkerFactory extends WorkerFactory {

    private final Map<String, Provider<ChildWorkerFactory>> creators;

    @Inject
    public DaggerWorkerFactory(Map<Class<? extends ListenableWorker>, Provider<ChildWorkerFactory>> creators) {
        this.creators = new HashMap<>();
        for (Map.Entry<Class<? extends ListenableWorker>, Provider<ChildWorkerFactory>> entry : creators.entrySet()) {
            this.creators.put(entry.getKey().getName(), entry.getValue());
        }
    }

    @Nullable
    @Override
    public ListenableWorker createWorker(@NonNull Context appContext, @NonNull String workerClassName, @NonNull WorkerParameters workerParameters) {
        Provider<ChildWorkerFactory> creator = creators.get(workerClassName);
        if (creator == null) {
            throw new IllegalArgumentException("unknown model class " + workerClassName);
        }
        try {
            return creator.get().create(appContext, workerParameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
