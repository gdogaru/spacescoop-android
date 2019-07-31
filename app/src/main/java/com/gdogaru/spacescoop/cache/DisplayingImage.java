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

import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * @author Gabriel Dogaru (gdogaru@gmail.com)
 */
public class DisplayingImage {
    private final DownloadableImage dimage;
    private final ImageSize size;

    private final WeakReference<ImageView> imageViewWeakReference;

    public DisplayingImage(DownloadableImage dimage, ImageSize size, ImageView imageView) {
        this.dimage = dimage;
        this.size = size;
        this.imageViewWeakReference = new WeakReference<ImageView>(imageView);
    }

    public DownloadableImage getImage() {
        return dimage;
    }

    public ImageView getImageView() {
        return imageViewWeakReference.get();
    }

    public ImageSize getSize() {
        return size;
    }

    public boolean isMemoryCacheable() {
        return size == ImageSize.THUMB;
    }
}
