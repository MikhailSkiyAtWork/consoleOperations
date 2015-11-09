package com.lionhouse.vetondemand;

/**
 * Created by Mikhail on 09.11.15.
 */

import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Helps to work with adb via terminal
 */
public class AdbUtil {

    public enum Commands{
        GET_PACKAGES,
        PULL

    }

    private static String adbFullPath_;
    private static String aaptFullPath_;

    public AdbUtil(String baseSdkPath){
        adbFullPath_ = getFullAdbPath(baseSdkPath);
        aaptFullPath_ = getFullAaptPath(baseSdkPath);
    }


    /**
     * Returns full path of apk location
     */
    public static String getApkLocation(String packageName) throws IOException, InterruptedException {
        // Creates full command
        String fullCommand = adbFullPath_.concat(" ").concat(Constants.GET_LIST_OF_PACKAGES_CMD);

        String output = executeTerminalCommand(fullCommand);
        String fullPackageName = getFullPAckageName(output,packageName);

        int packageNameLenth = packageName.length();
        int fullPackageNameLength = fullPackageName.length();
        String apkLocation = fullPackageName.substring(8, fullPackageNameLength-packageNameLenth-1);
        return apkLocation;
    }

    public static void saveApkInFolder(String apkLocationOnDevice, String toFolderPath) throws IOException, InterruptedException {
        String pullCommand = adbFullPath_.concat(" ")
                .concat(Constants.PULL_APK_CMD).concat(" ")
                .concat(apkLocationOnDevice)
                .concat(" ")
                .concat(toFolderPath);

        Process process = Runtime.getRuntime().exec(pullCommand);
        process.waitFor();
    }

    public static String getXmlTree(String apkName) throws IOException, InterruptedException{
        String getXmlTreeCommand = aaptFullPath_.concat(" ")
                .concat(Constants.DUMP_XMLTREE)
                .concat(" ")
                .concat(createPulledApkLocation(apkName))
                .concat(" ")
                .concat(Constants.MANIFEST);

        String fullXmlTree = executeTerminalCommand(getXmlTreeCommand);
        return fullXmlTree;
    }

    private static String createPulledApkLocation(String apkName){
        String pulledApkLocation = Constants.PULLED_APK_PATH.concat("/").concat(apkName);
        return pulledApkLocation;
    }

    public static List<String> getAllActivities(String xmlTree){
        List<String> activities = new ArrayList<>();
        Scanner scanner = new Scanner(xmlTree);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            if (line.contains("Activity")){
                String activityName = line;
                activities.add(activityName);
            }
        }

        return activities;

    }

    // TODO ADD ERROR HANDLING
    private static String executeTerminalCommand(String terminalCommand) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(terminalCommand);
        process.waitFor();

        Reader in = new InputStreamReader(process.getInputStream());
        in = new BufferedReader(in);
        char[] buffer = new char[5024];
        int len = in.read(buffer);
        String s = new String(buffer, 0, len);
        return s;
    }

    public static String getFullAdbPath(String sdkPath){
        String fullAdbPath = sdkPath.concat(Constants.ADB_PATH_RELATIVE_TO_SDK_ROOT);
        return fullAdbPath;
    }

    private static String getFullAaptPath(String aaptPath){
        String fullAaptPath = aaptPath.concat(Constants.AAPT_PATH_RELATIVE_TO_SDK_ROOT);
        return fullAaptPath;
    }

    public static String getApkName(String apkLocation) {
        String[] folders = apkLocation.split("/");
        String apkName = folders[folders.length-1];
        return apkName;
    }

    private static String getFullPAckageName(String buffer,String packageName){
        String targetPackage="";
        Scanner scanner = new Scanner(buffer);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            if (line.contains(packageName)){
                targetPackage = line;
                return  targetPackage;
            }
        }
        return targetPackage;
    }



}
