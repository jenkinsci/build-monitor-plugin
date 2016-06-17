package com.smartcodeltd.jenkinsci.plugin.assetbundler;

public class OsSpecific {
    private OsSpecific(){}

    public static boolean isWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }

    public static boolean isNotWindows() {
        return ! isWindows();
    }
}