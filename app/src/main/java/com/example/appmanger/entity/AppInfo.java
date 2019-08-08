package com.example.appmanger.entity;
// entity class: Info of App

import android.graphics.drawable.Drawable;

import com.example.appmanger.util.Utils;

public class AppInfo {
    /** package name*/
    public  String packageName;
    /** version Name*/
    public  String versionName;
    /** version Code*/
    public  int versionCode;
    /** The first install time*/
    public  long insTime;
    /** Last update time*/
    public  long upTime;
    /** App name*/
    public  String appName;
    /** Picture*/
    public Drawable icon;
    /** Byte Size*/
    public  long byteSize;
    /** Size */
    public  String Size;

    @Override
    public String toString() {
        return "AppInfo{" +
                "packageName='" + packageName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                ", insTime=" + Utils.getTime(insTime) +
                ", upTime=" + Utils.getTime(upTime) +
                ", appName='" + appName + '\'' +
                ", icon=" + icon +
                ", byteSize=" + byteSize +
                ", Size='" + Size + '\'' +
                '}';
    }
}
