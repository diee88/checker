package com.spe.checker;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by AnasBayu on 01/08/2017.
 */

public class DeviceHelper {

    private boolean loggingEnabled = true;

    public Boolean isDeviceRooted(Context context){
        boolean isRooted = checkSuperUser() || findBinary("su")|| checkBuildInfo() || checkRootMethod3() || checkForRootNative();
        return isRooted;
    }

    private boolean checkSuperUser() {
        File file = new File("/system/app/Superuser.apk");
        if (file.exists()) {
            return true;
        }
        return false;
    }

    private boolean isAbleToExecCommand() {
        return canExecuteCommand("/system/xbin/which su")
                || canExecuteCommand("/system/bin/which su")
                || canExecuteCommand("which su");
    }

    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] { "/system/xbin/which", "su" });
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) return true;
            return false;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }

    private boolean canExecuteCommand(String command) {
        boolean executedSuccesfully;
        try {
            Runtime.getRuntime().exec(command);
            executedSuccesfully = true;
        } catch (Exception e) {
            executedSuccesfully = false;
        }

        return executedSuccesfully;
    }

    private boolean findBinary(String binaryName) {
        boolean found = false;
        if (!found) {
            String[] places = {
                    "/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/",
                    "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/",
                    "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su",
                    "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                    "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
            for (String where : places) {
                if ( new File( where + binaryName ).exists() ) {
                    found = true;
                    break;
                }
            }

            String[] pathsArray = places;
            for (String path : pathsArray) {
                String completePath = path + binaryName;
                File f = new File(completePath);
                boolean fileExists = f.exists();
                if (fileExists) {
                    found = true;
                    break;
                }
            }

        }
        return found;
    }

    private boolean checkBuildInfo(){
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }else{
            return false;
        }
    }

    public boolean checkForRootNative() {

        if (!canLoadNativeLibrary()){
            QLog.e("We could not load the native library to test for root");
            return false;
        }

        String binaryName = "su";
        String[] paths = new String[Const.suPaths.length];
        for (int i = 0; i < paths.length; i++) {
            paths[i] = Const.suPaths[i]+binaryName;
        }

        RootBeerNative rootBeerNative = new RootBeerNative();
        try {
            rootBeerNative.setLogDebugMessages(loggingEnabled);
            return rootBeerNative.checkForRoot(paths) > 0;
        } catch (UnsatisfiedLinkError e) {
            return false;
        }
    }

    public boolean canLoadNativeLibrary(){
        return new RootBeerNative().wasNativeLibraryLoaded();
    }

}
