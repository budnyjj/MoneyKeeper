package budny.moneykeeper.cv;

import android.util.Log;

public class CVManager {
    private static final String TAG = CVManager.class.getSimpleName();

    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("opencv_java3");
        System.loadLibrary("cv");
    }

    public static boolean init() {
        try {
            Log.i(TAG, "Loading OpenCV library");
            CVManager loader = Holder.INSTANCE;
        } catch (SecurityException | UnsatisfiedLinkError e) {
            Log.e(TAG, "Unable to load OpenCV library");
            return false;
        }
        return true;
    }

    private CVManager() {
        // to do not be instantiated by mistake
    }

    private static class Holder {
        private static final CVManager INSTANCE = new CVManager();
    }
}
