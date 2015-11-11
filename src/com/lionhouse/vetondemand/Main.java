package com.lionhouse.vetondemand;


import java.io.*;
import java.util.List;


public class Main {

    private final String getPackages = "/Users/Mikhail/Library/Android/sdk/platform-tools/adb -s TA10201G7S shell pm list packages";
    private final String getListOfDevices = "/Users/Mikhail/Library/Android/sdk/platform-tools/adb devices";

    public static void main(String[] args) throws IOException, InterruptedException {
        // Change directory
       //// Process p = Runtime.getRuntime().exec("cd /Users/Mikhail/Library/Android/sdk/platform-tools");
       // p = Runtime.getRuntime().exec("/Users/Mikhail/Library/Android/sdk/platform-tools/adb devices");
     // //  p = Runtime.getRuntime().exec("/Users/Mikhail/Library/Android/sdk/platform-tools/adb shell pm list packages -f");

       // p = Runtime.getRuntime().exec("/Users/Mikhail/Library/Android/sdk/platform-tools/adb pull /data/app/com.example.admin.moviesapp-1/base.apk /Users/Mikhail/screenTest");
       // p = Runtime.getRuntime().exec("/Users/Mikhail/Library/Android/sdk/platform-tools/adb -s TA10201G7S shell pm list packages");

        // p = Runtime.getRuntime().exec("pwd");

        // Process p2 = Runtime.getRuntime().exec("adb", ar);
        // Process p2 = Runtime.getRuntime().exec("adb");
        // Process p2 = Runtime.getRuntime().exec("./ adb devices");

//        p.waitFor();
//
//        Reader in = new InputStreamReader(p.getInputStream());
//        in = new BufferedReader(in);
//
//        // List which contains all packages on the device
//        List<String> packagesList = new ArrayList<>();
//
//        char[] buffer = new char[5024];
//        int len = in.read(buffer);
//        String s = new String(buffer, 0, len);
//
//        String targetPackage="";
//
//        // 1) Find full path of target package
//        Scanner scanner = new Scanner(s);
//        while (scanner.hasNextLine()){
//            String line = scanner.nextLine();
//            if (line.contains(TARGET_PACKAGE)){
//                targetPackage = line;
//            }
//        }
//
//        // Get path of target package
//        int packageNameLenth = TARGET_PACKAGE.length();
        AdbUtil helper = new AdbUtil(Constants.BASE_SDK_PATH);
        String apkLocation = AdbUtil.getApkLocation(Constants.TARGET_PACKAGE);
        System.out.println("Found path");
        System.out.println(apkLocation);

        String apkName = AdbUtil.getApkName(apkLocation);

        AdbUtil.saveApkInFolder(apkLocation,Constants.PULLED_APK_PATH);
        String xmlTree = AdbUtil.getXmlTree(apkName);

        // Get list of all activities
        List<String> acs = AdbUtil.getAllActivities(xmlTree);



        //Print for test
       // System.out.println(fullPath);
        System.out.println("Found xmlTree");
        System.out.println(xmlTree);


        System.out.println("Found activities");
//        for (int i =0; i<acs.size();i++){
        try {
                          //1000 milliseconds is one second.

        for (int i =0; i<5;i++){
            System.out.println(acs.get(i));
            AdbUtil.startActivity(acs.get(i));
            AdbUtil.makeScreenshot(acs.get(i));
            Thread.sleep(10000);
        }

        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
