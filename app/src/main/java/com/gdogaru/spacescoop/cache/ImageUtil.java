package com.gdogaru.spacescoop.cache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 *
 */
public class ImageUtil {

    public static DownloadableImage toThumb(DownloadableImage dimg) {
        String url = dimg.getUrl();
        int idx2 = url.lastIndexOf("/");
        int idx1 = url.substring(0, idx2).lastIndexOf("/");
        String url2 = url.substring(0, idx1) + "/thumb_android" + url.substring(idx2);
        return new DownloadableImage(url2);
    }

    @NonNull
    public static String fileName(@Nullable String filePath) {
        if (filePath == null) return "";
        int idx2 = filePath.lastIndexOf("/");
        return filePath.substring(idx2);
    }

}
