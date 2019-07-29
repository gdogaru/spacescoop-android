package com.gdogaru.spacescoop.cache;

import android.content.Context;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import timber.log.Timber;

/**
 * Created by Gabriel Dogaru (gdogaru@gmail.com)
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
