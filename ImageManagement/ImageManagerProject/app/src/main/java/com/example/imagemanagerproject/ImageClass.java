package com.example.imagemanagerproject;

public class ImageClass {
    private byte[] image;
    private String time;
    private String album;

    public ImageClass()
    {}

    public ImageClass(String time, String album, byte[] image) {
        this.image = image;
        this.time = time;
        this.album = album;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
