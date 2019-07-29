package com.gdogaru.spacescoop.cache;

import java.io.File;

/**
 * Created by Gabriel Dogaru (gdogaru@gmail.com)
 */
public interface DiskAdapter {
    byte[] read(String path);

    void write(byte[] data, String path);

    void removeFile(String path);

    boolean contains(String path);

    File getFile(String path);
}
