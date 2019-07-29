package com.gdogaru.spacescoop.controllers;

import android.widget.ImageView;

/**
 * Created by Gabriel on 12/26/2014.
 */
public interface ImageDownloader {

    void display(String url, ImageView newsImage);

    void displayThumb(String url, ImageView newsImage);

    void downloadAndPrepare(String url);
}
