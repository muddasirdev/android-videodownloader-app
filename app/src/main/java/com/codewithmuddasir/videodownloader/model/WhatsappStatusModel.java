package com.codewithmuddasir.videodownloader.model;

import android.net.Uri;

public class WhatsappStatusModel {
    private String name;
    private Uri uri;
    private String path;
    private String filename;

    public WhatsappStatusModel(String name, Uri uri, String path, String filename) {
        this.name = name;
        this.uri = uri;
        this.path = path;
        this.filename = filename;
    }

    public String getName() {
        return name;
    }

    public Uri getUri() {
        return uri;
    }

    public String getPath() {
        return path;
    }

    public String getFilename() {
        return filename;
    }
}
