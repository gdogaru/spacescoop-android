package com.gdogaru.spacescoop.cache;

/**
 * Created by Gabriel Dogaru (gdogaru@gmail.com)
 */
public enum ImageSize {
    THUMB("thumb"),
    FULL("full");

    public final String type;

    ImageSize(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
