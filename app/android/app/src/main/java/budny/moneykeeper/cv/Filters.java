package budny.moneykeeper.cv;

import android.util.Log;

import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Filters {
    private static final String TAG = Filters.class.getSimpleName();
    private static final String MSG_SHOULD_NOT_BE_NULL = "Input arguments should not be null";
    private static final String MSG_NATIVE_ERROR = "Caught error during native method invocation";

    static {
        if (!CVManager.init()) {
            Log.e(TAG, "Unable to load OpenCV library");
        }
    }
    
    public static void basic(Mat src, Mat dst) {
        if (src == null || dst == null) {
            throw new NullPointerException(MSG_SHOULD_NOT_BE_NULL);
        }
        if (!nativeBasic(src.getNativeObjAddr(), dst.getNativeObjAddr())) {
            throw new CvException(MSG_NATIVE_ERROR);
        }
    }

    public static void basic2(Mat src, Mat dst) {
        if (src == null || dst == null) {
            throw new NullPointerException(MSG_SHOULD_NOT_BE_NULL);
        }
        if (!nativeBasic2(src.getNativeObjAddr(), dst.getNativeObjAddr())) {
            throw new CvException(MSG_NATIVE_ERROR);
        }
    }

    public static void highlight(Mat src, Mat dst) {
        if (src == null || dst == null) {
            throw new NullPointerException(MSG_SHOULD_NOT_BE_NULL);
        }
        if (!nativeHighlight(src.getNativeObjAddr(), dst.getNativeObjAddr(), 100, 400)) {
            throw new CvException(MSG_NATIVE_ERROR);
        }
    }

    public static native boolean nativeBasic(long pSrcMat, long pDstMat);

    public static native boolean nativeBasic2(long pSrcMat, long pDstMat);

    public static native boolean nativeHighlight(long pSrcMat, long pDstMat, int width, int height);
}
