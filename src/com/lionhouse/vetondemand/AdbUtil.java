package com.lionhouse.vetondemand;

/**
 * Created by Mikhail on 09.11.15.
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        System.out.println("Created pull cmd: " + pullCommand);

        Process process = Runtime.getRuntime().exec(pullCommand);
        //process.waitFor();
    }

    /**
     * Returns xmlTree of AndroidManigest.xlm
     */
    public static String getXmlTree(String apkName) throws IOException, InterruptedException{
        String getXmlTreeCommand = aaptFullPath_.concat(" ")
                .concat(Constants.DUMP_XMLTREE)
                .concat(" ")
                .concat(createPulledApkLocation(apkName))
                .concat(" ")
                .concat(Constants.MANIFEST);

        System.out.println("Created XML tree cmd: " + getXmlTreeCommand);

        String fullXmlTree = executeTerminalCommand(getXmlTreeCommand);
        return fullXmlTree;
    }

    /**
     * Creates full path of pulled apk
     */
    private static String createPulledApkLocation(String apkName){
        String pulledApkLocation = Constants.PULLED_APK_PATH.concat("/").concat(apkName);
        return pulledApkLocation;
    }

    /**
     * Returns list of all activities from AndroidManifest.xml
     */
    public static List<String> getAllActivities(String xmlTree){
        List<String> activities = new ArrayList<>();
        Scanner scanner = new Scanner(xmlTree);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            if (line.contains("Activity")){
                if (line.contains(Constants.TARGET_PACKAGE)) {
                    String activityName = getActivityNameWithoutPackage(getFullActivityName(line));
                    activities.add(activityName);
                }
            }
        }

        return activities;
    }

    /**
     * Call activity
     * @param activityName - activity name without package (like .activities.MainActivity)
     */
    public static void startActivity(String activityName) throws IOException, InterruptedException{
        executeTerminalCommand(createStartActivityCommand(activityName));

    }

    private static String createStartActivityCommand(String activityName){
        String fullActivityName = Constants.TARGET_PACKAGE.concat("/")
                .concat(activityName);

        String startActivityCommand = adbFullPath_.concat(" ").concat(Constants.START_ACTIVITY_CMD).concat(" ")
                .concat(fullActivityName);

        System.out.println("START" + startActivityCommand);

        return startActivityCommand;
    }


    public static void makeScreenshot(String activityName) throws IOException, InterruptedException{
        String makeScreenshot = createScreenshotCommand(activityName);
        executeVoidOperation(makeScreenshot);

        String pullScrenshot = createPullScreenshotCommand(activityName);
        executeVoidOperation(pullScrenshot);

    }

    private static void executeVoidOperation(String cmd) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();
    }

    /**
     * Makes full command to create screenshot on the device
     */
    private static String createScreenshotCommand(String activityName){
        String screenshotCommand = adbFullPath_.concat(" ")
                .concat(Constants.MAKE_SCREENSHOT_CMD)
                .concat(activityName)
                .concat(".png");

        System.out.println(screenshotCommand);

        return screenshotCommand;
    }

    /**
     * Makes full command to pull screenshot to the PC
     */
    private static String createPullScreenshotCommand(String activityName){
        String pullScreenshotCommand = adbFullPath_.concat(" ")
                .concat(Constants.PULL_SCREENSHOT_CMD)
                .concat(activityName)
                .concat(".png")
                .concat(" ")
                .concat(Constants.PULLED_SCREENS_PATH)
                .concat("/")
                .concat(activityName.replace(".activities.",""))
                .concat(".png");

        System.out.println(pullScreenshotCommand);

        return pullScreenshotCommand;
    }


    /**
     * Returns full name of each activity
     */
    private static String getFullActivityName(String line){
        Pattern pattern = Pattern.compile("\"(.*?)\"");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()){
            return  matcher.group(0).replace("\"","");
        }
        return "";
    }

    /**
     * Returns activity name without package (like .activities.MainActivity)
     */
    private static String getActivityNameWithoutPackage(String fullActivityName){
        int packageLength = Constants.TARGET_PACKAGE.length();
        String clearName = fullActivityName.substring(packageLength);
        return clearName;
    }

    // TODO ADD ERROR HANDLING
    private static String executeTerminalCommand(String terminalCommand) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(terminalCommand);
        process.waitFor();

        Reader in = new InputStreamReader(process.getInputStream());
        in = new BufferedReader(in);
        char[] buffer = new char[20000];
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
