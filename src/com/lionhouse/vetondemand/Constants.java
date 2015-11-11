package com.lionhouse.vetondemand;

/**
 * Created by Mikhail on 09.11.15.
 */
public class Constants {

    // The package of app,for which screenshots should be made
//    public final static String TARGET_PACKAGE = "com.example.admin.moviesapp";

    public final static String TARGET_PACKAGE = "com.lionhouse.vetondemand";

    //region Paths

    // Path for SDK
    public static final String BASE_SDK_PATH = "/Users/Mikhail/Library/Android/sdk";
    // Relative to sdk root path for adb
    public static final String ADB_PATH_RELATIVE_TO_SDK_ROOT = "/platform-tools/adb";
    // Relative to sdk roo path for aapt
    public static final String AAPT_PATH_RELATIVE_TO_SDK_ROOT = "/build-tools/22.0.1/aapt";
    // Path for saving apk of app
    public final static String PULLED_APK_PATH = "/Users/Mikhail/screenTest";
    // Path for saving screens of app
    public final static String PULLED_SCREENS_PATH = "/Users/Mikhail/screens";
    //endregion

    //region Commands

    public static final String GET_LIST_OF_PACKAGES_CMD = "shell pm list packages -f";
    public static final String PULL_APK_CMD = "pull";
    public static final String START_ACTIVITY_CMD = "shell am start -n";
    public static final String MAKE_SCREENSHOT_CMD = "shell screencap /sdcard/";
    public static final String PULL_SCREENSHOT_CMD = "pull /sdcard/";



    //endregion

    //region Others constants
    public static final String MANIFEST = "AndroidManifest.xml";
    public static final String DUMP_XMLTREE = "dump xmltree";
    //endregion
}
