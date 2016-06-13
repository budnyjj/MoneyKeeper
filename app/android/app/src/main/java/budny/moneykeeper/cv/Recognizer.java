package budny.moneykeeper.cv;

import android.content.res.AssetManager;
import android.util.Log;

import org.opencv.core.Mat;

public class Recognizer {
    private static final String TAG = Recognizer.class.getSimpleName();
    private static final String MSG_SHOULD_NOT_BE_NULL = "Input arguments should not be null";
    private static final String MODEL_FILENAME = "";

    static {
        if (!CVManager.init()) {
            Log.e(TAG, "Unable to load OpenCV library");
        }
    }

    // pointer to native recognizer object
    private final long mNativeInstance;

    public Recognizer(AssetManager manager) {
        mNativeInstance = nativeInitialize(manager, MODEL_FILENAME);
    }

    public void recognize(Mat src, Mat dst) {
        if (src == null || dst == null) {
            throw new NullPointerException(MSG_SHOULD_NOT_BE_NULL);
        }
        nativeRecognize(mNativeInstance, src.getNativeObjAddr(), dst.getNativeObjAddr());
    }

    public void dispose() {
        nativeDispose(mNativeInstance);
    }

    private native long nativeInitialize(AssetManager manager, String modelFilename);
    private native void nativeDispose(long pNativeInstance);
    private native void nativeRecognize(long pNativeInstance, long pSrcMat, long pDstMat);
}
