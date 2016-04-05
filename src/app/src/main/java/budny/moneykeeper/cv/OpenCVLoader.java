package budny.moneykeeper.cv;

import android.util.Log;

public class OpenCVLoader {
    private static final String TAG = OpenCVLoader.class.getSimpleName();

    static {
        System.loadLibrary("opencv_java3");
    }

    public static boolean init() {
        try {
            Log.i(TAG, "Loading OpenCV library");
            OpenCVLoader loader = Holder.INSTANCE;
        } catch (SecurityException | UnsatisfiedLinkError e) {
            Log.e(TAG, "Unable to load OpenCV library");
            return false;
        }
        return true;
    }

    private OpenCVLoader() {
        // to do not be instantiated by mistake
    }

    private static class Holder {
        private static final OpenCVLoader INSTANCE = new OpenCVLoader();
    }
}
