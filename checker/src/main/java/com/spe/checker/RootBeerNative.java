package com.spe.checker;

/**
 * Created by Docotel on 12/03/2018.
 */

public class RootBeerNative {
    static boolean libraryLoaded = false;

    /**
     * Loads the C/C++ libraries statically
     */
    static {
        try {
            System.loadLibrary("tool-checker");
            libraryLoaded = true;
        }
        catch (UnsatisfiedLinkError e){
            QLog.e(e);
        }
    }

    public boolean wasNativeLibraryLoaded(){
        return libraryLoaded;
    }

    public native int checkForRoot(Object[] pathArray);
    public native int setLogDebugMessages(boolean logDebugMessages);

}
