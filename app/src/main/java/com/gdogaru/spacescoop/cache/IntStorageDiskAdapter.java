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

package com.gdogaru.spacescoop.cache;

import android.content.Context;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import timber.log.Timber;

/**
 * @author Gabriel Dogaru (gdogaru@gmail.com)
 */
public class IntStorageDiskAdapter implements DiskAdapter {
    Context appContext;

    public IntStorageDiskAdapter(Context appContext) {
        this.appContext = appContext;
    }

    private String createLocalPath(String path) {
        return path;
    }

    @Override
    public boolean contains(String path) {
        FileInputStream fi = null;
        try {
            fi = appContext.openFileInput(createLocalPath(path));
            return fi.available() > 0;
        } catch (Exception e) {
            return false;
        } finally {
            IOUtils.closeQuietly(fi);
        }
    }

    @Override
    public File getFile(String path) {
        return new File(createLocalPath(path));
    }

    @Override
    public byte[] read(String path) {
        FileInputStream stream = null;
        try {
            stream = appContext.openFileInput(createLocalPath(path));
            return IOUtils.toByteArray(stream);
        } catch (Exception e) {
            Timber.e(e, "Error reading file");
            return null;
        } finally {
            IOUtils.closeQuietly(stream);
        }

    }

    @Override
    public void write(byte[] data, String path) {
        FileOutputStream stream = null;
        try {
            stream = appContext.openFileOutput(createLocalPath(path), Context.MODE_PRIVATE);
            IOUtils.write(data, stream);

        } catch (Exception e) {
            Timber.e(e, "Error reading file");
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    @Override
    public void removeFile(String path) {
        appContext.deleteFile(createLocalPath(path));
    }
}
