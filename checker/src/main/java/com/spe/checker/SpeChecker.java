package com.spe.checker;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Debug;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.List;

public class SpeChecker {
    
    private static Context context;
    private static String StrSignature;
    private static ActivityManager manager;
    private static String packageName, signature;

    public SpeChecker(Context context) {
        this.context = context;
    }

    private boolean SpeChecker(Context context, String packageName, String signature) {
        this.context = context;
        this.packageName = packageName;
        this.signature = signature;
        boolean hasil = false;
        manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (new DeviceHelper().isDeviceRooted(context) ||
                appInstalledOrNot("com.devadvance.rootcloak2") ||
                appInstalledOrNot("com.thirdparty.superuser") ||
                appInstalledOrNot("eu.chainfire.supersu") ||
                appInstalledOrNot("com.noshufou.android.su") ||
                appInstalledOrNot("com.koushikdutta.superuser") ||
                appInstalledOrNot("com.zachspong.temprootremovejb") ||
                appInstalledOrNot("com.ramdroid.appquarantine") ||
                appInstalledOrNot("com.topjohnwu.magisk") ||
                appInstalledOrNot("eu.chainfire.supersu") ||
                appInstalledOrNot("com.xposedmodules.app") ||
                isAppInstalled("xposed") ||
                isAppInstalled("root") ||
                appInstalledOrNot("de.robv.android.xposed.installer")) {
            hasil = true;
        } else if (isDeviceRooted()) {
            hasil = true;
        } else if (isHacked(context, packageName)) {
            hasil = true;
        } else if (PreferenceHelper.SIGNATURE.equals(signature)) {
            hasil = true;
        } else {
            // Start service.
            Intent intent = new Intent("RESTART_SERVICE");
            context.sendBroadcast(intent);
            hasil = false;
        }
        return hasil;
    }

    public boolean isHacked(Context context, String myPackageName)
    {
        //Renamed?
        if (context.getPackageName().compareTo(myPackageName) != 0) {
            return true; // BOOM!
        }

        //Relocated?
        String installer = context.getPackageManager().getInstallerPackageName(myPackageName);

        if (installer == null){
            return true; // BOOM!
        }
        return false;
    }

    public static boolean isDeviceRooted() {
        return detectmethods() || checkRootMethod1() || checkRootMethod2() || checkRootMethod3() ||
                antiFrida1() || antiFrida2() || isDebuggable() || detectDebugger() || checkEmulator();
    }

    private static boolean detectmethods() {
        String[] arrayOfString = new String[10];
        arrayOfString[0] = "/system/app/Superuser.apk";
        arrayOfString[1] = "/sbin/su";
        arrayOfString[2] = "/system/bin/su";
        arrayOfString[3] = "/system/xbin/su";
        arrayOfString[4] = "/data/local/xbin/su";
        arrayOfString[5] = "/data/local/bin/su";
        arrayOfString[6] = "/system/sd/xbin/su";
        arrayOfString[7] = "/system/bin/failsafe/su";
        arrayOfString[8] = "/data/local/su";
        arrayOfString[9] = "/su/bin/su";


        int a = arrayOfString.length;
        int b = 0;
        while (a < b) {
            if (new File(arrayOfString[a]).exists()) {
                return true;
            }
            a += 1;
        }
        return false;
    }

    private static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRootMethod2() {
        String[] paths = {"/system/app/Superuser.apk",
                "/sbin/su",
                "/system/bin/su",
                "/system/xbin/su",
                "/data/local/xbin/su",
                "/data/local/bin/su",
                "/system/sd/xbin/su",
                "/system/bin/failsafe/su",
                "/data/local/su"
        };
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) {
                return true;
            }
            return false;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }


    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    public boolean isAppInstalled(String appName) {
        PackageManager pkgMan = context.getPackageManager();
        List<PackageInfo> pkgList = pkgMan.getInstalledPackages(0);
        for (int a = 0; a < pkgList.size(); a++) {
            if (appName.equals(pkgMan.getApplicationLabel(pkgList.get(a).applicationInfo))) {
                return true;
            }
        }
        return false;
    }

    public static boolean antiFrida1() {
        boolean returnValue = false;
        List<ActivityManager.RunningServiceInfo> list = manager.getRunningServices(300);

        String tempName = "";
        if (list != null) {
            for (int i = 0; i < list.size(); ++i) {
                tempName = list.get(i).process;

                if (tempName.contains("fridaserver")) {
                    returnValue = true;
                }
            }
        }
        return returnValue;
    }

    public static boolean antiFrida2() {
        boolean returnValue = false;

        // Get currently running application processes
        List<ActivityManager.RunningServiceInfo> list = manager.getRunningServices(300);

        if (list != null) {
            String tempName;
            for (int i = 0; i < list.size(); ++i) {
                tempName = list.get(i).process;

                if (tempName.contains("supersu") || tempName.contains("superuser")) {
                    returnValue = true;
                }
            }
        }
        return returnValue;
    }

    public static boolean isDebuggable() {
        boolean debugIn = (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        return debugIn;
    }

    public static boolean detectDebugger() {
        return Debug.isDebuggerConnected();
    }

    public static boolean checkEmulator() {

        try {

            boolean goldfish = getSystemProperty("ro.hardware").contains("goldfish");

            boolean emu = getSystemProperty("ro.kernel.qemu").length() > 0;

            boolean sdk = getSystemProperty("ro.product.model").equals("sdk");

            if (emu || goldfish || sdk) {

                return true;

            }

        } catch (Exception e) {

        }

        return false;

    }

    private static String getSystemProperty(String name)

            throws Exception {

        Class systemPropertyClazz = Class

                .forName("android.os.SystemProperties");


        return (String) systemPropertyClazz.getMethod("get", new Class[]{String.class})

                .invoke(systemPropertyClazz, new Object[]{name});
    }

    public static boolean checkAppSignature(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            PackageManager.GET_SIGNATURES);


            for (Signature signature : packageInfo.signatures) {
                byte[] signatureBytes = signature.toByteArray();
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                final String currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT);
//                Log.i("test", currentSignature);
//                Log.i("test2", PreferenceHelper.SIGNATURE);
                //compare signatures
                if (PreferenceHelper.SIGNATURE.equals(currentSignature)) {
                    return true;
                } else
                    return false;
                }

        } catch (Exception e) {
            //assumes an issue in checking signature., but we let the caller decide on what to do.
        }
        return false;
    }
}
