package budny.moneykeeper.cv;

import android.util.Log;

import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Operations {
    public static final String TAG = Filters.class.getSimpleName();
    private static final String MSG_SHOULD_BE_POSITIVE = "Input arguments should be positive";
    private static final String MSG_SHOULD_NOT_BE_NULL = "Input arguments should not be null";
    private static final String MSG_NATIVE_ERROR = "Caught error during native method invocation";
    private static final int DEFAULT_DARKEN = -100;

    static {
        if (!CVManager.init()) {
            Log.e(TAG, "Unable to load OpenCV library");
        }
    }

    public static void  sliceCentered(Mat src, Mat dst, int height, int width) {
        if (src == null || dst == null) {
            throw new NullPointerException(MSG_SHOULD_NOT_BE_NULL);
        }
        if (height <= 0 || width <= 0) {
            throw new NullPointerException(MSG_SHOULD_BE_POSITIVE);
        }

        if (!nativeSliceCentered(src.getNativeObjAddr(), dst.getNativeObjAddr(), height, width)) {
            throw new CvException(MSG_NATIVE_ERROR);
        }
    }

    public static void mergeCentered(Mat srcBottom, Mat srcTop, Mat dst) {
        if (srcBottom == null || srcTop == null || dst == null) {
            throw new NullPointerException(MSG_SHOULD_NOT_BE_NULL);
        }
        if (!nativeMergeCentered(
                srcBottom.getNativeObjAddr(), srcTop.getNativeObjAddr(), dst.getNativeObjAddr())) {
            throw new CvException(MSG_NATIVE_ERROR);
        }
    }

    public static void darken(Mat src, Mat dst) {
        if (src == null || dst == null) {
            throw new NullPointerException(MSG_SHOULD_NOT_BE_NULL);
        }
        src.convertTo(dst, -1, 1, DEFAULT_DARKEN);
    }

    private static native boolean nativeSliceCentered(
            long pSrcMat, long pDstMat, int height, int width);
    private static native boolean nativeMergeCentered(
            long pSrcBottomMat, long pSrcTopMat, long pDstMat);
}
