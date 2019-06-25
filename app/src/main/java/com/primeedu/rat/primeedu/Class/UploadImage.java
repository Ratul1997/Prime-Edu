package com.primeedu.rat.primeedu.Class;

public class UploadImage {
    private String  imagename;
    private String imageurl;

    public UploadImage(){

    }
    public UploadImage(String imagename, String imageurl) {
        this.imagename = imagename;
        this.imageurl = imageurl;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
