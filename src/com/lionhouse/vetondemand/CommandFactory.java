package com.lionhouse.vetondemand;

/**
 * Created by Mikhail on 09.11.15.
 */
public class CommandFactory {

    private static String sdkPath;

    public CommandFactory(String sdkPath){
        sdkPath = AdbUtil.getFullAdbPath(sdkPath);
    }

    public static String createCommand(AdbUtil.Commands command){
        if (command.equals(AdbUtil.Commands.GET_PACKAGES)){
            return sdkPath.concat(" ").concat(Constants.GET_LIST_OF_PACKAGES_CMD);
        }

//        if (command.equals(AdbUtil.Commands.PULL)){
//            return sdkPath.concat(" ").concat()
//        }
        return "";
    }
}
