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

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MemoryCache {
    private Map<DownloadableImage, SoftReference<Bitmap>> cache = Collections.synchronizedMap(new HashMap<DownloadableImage, SoftReference<Bitmap>>());

    public Bitmap get(DownloadableImage img) {
        if (!cache.containsKey(img)) {
            return null;
        }
        SoftReference<Bitmap> ref = cache.get(img);
        return ref.get();
    }

    public void put(DownloadableImage img, Bitmap bitmap) {
        cache.put(img, new SoftReference<Bitmap>(bitmap));
    }

    public void clear() {
        cache.clear();
    }
}