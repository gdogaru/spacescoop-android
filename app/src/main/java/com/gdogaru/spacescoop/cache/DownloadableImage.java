package com.gdogaru.spacescoop.cache;


public class DownloadableImage {
    private final String url;
    private final String key;

    public DownloadableImage(String url) {
        this.url = url;
        key = ImageUtil.fileName(url);
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DownloadableImage that = (DownloadableImage) o;

        return key != null ? key.equals(that.key) : that.key == null;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }
}
