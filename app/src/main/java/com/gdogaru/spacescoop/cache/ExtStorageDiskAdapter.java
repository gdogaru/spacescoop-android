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

public class ExtStorageDiskAdapter implements DiskAdapter {
    private static final String CACHE_FOLDER = "news_img";

    Context appContext;

    public ExtStorageDiskAdapter(Context context) {
        this.appContext = context.getApplicationContext();
    }

    @Override
    public boolean contains(String path) {
        return getFile(path).exists();
    }

    @Override
    public File getFile(String path) {
        File externalFilesDir = appContext.getExternalFilesDir(CACHE_FOLDER);
        if (!externalFilesDir.exists()) {
            externalFilesDir.mkdirs();
        }
        return new File(externalFilesDir, path);
    }

    @Override
    public byte[] read(String path) {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(getFile(path));
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
            File file = getFile(path);
            file.getParentFile().mkdirs();
            file.createNewFile();
            stream = new FileOutputStream(file);
            IOUtils.write(data, stream);
            stream.flush();
        } catch (Exception e) {
            Timber.e(e, "Error writing file");
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    public void append(byte[] data, String path) {
        FileOutputStream stream = null;
        try {
            File file = getFile(path);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            stream = new FileOutputStream(file, true);
            IOUtils.write(data, stream);
            stream.flush();
        } catch (Exception e) {
            Timber.e(e, "Error reading file");
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    @Override
    public void removeFile(String path) {
        File file = getFile(path);
        if (file.exists()) {
            file.delete();
        }
    }


}
