package budny.moneykeeper.cv;

import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Filters {
    public static final String TAG = Filters.class.getSimpleName();

    static {
        if (!OpenCVLoader.init()) {
            Log.e(TAG, "Unable to load OpenCV library");
        }
    }

    public static Mat basicFilter(Mat input) {
        Mat output = new Mat();
        input.convertTo(output, -1, 1, 0);
        Imgproc.GaussianBlur(output, output, new Size(3, 3), 0, 0);
        Imgproc.cvtColor(output, output, Imgproc.COLOR_BGR2GRAY);
        Imgproc.adaptiveThreshold(output, output,
                255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                Imgproc.THRESH_BINARY, 11, 5);
        return output;
    }
}
