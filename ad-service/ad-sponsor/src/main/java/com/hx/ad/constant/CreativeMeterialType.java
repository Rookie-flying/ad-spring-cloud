package com.hx.ad.constant;

public enum CreativeMeterialType {

    JPG(1, "jpg"),
    PNG(2, "png"),

    MP4(3, "mp4"),
    AVI(4, "AVI"),

    TXT(5, "txt");

    private int type;
    private String desc;

    CreativeMeterialType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
