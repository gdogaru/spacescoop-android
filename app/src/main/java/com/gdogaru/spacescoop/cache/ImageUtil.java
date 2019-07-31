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
