package budny.moneykeeper.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

import budny.moneykeeper.R;
import budny.moneykeeper.cv.OpenCVLoader;

public class FragmentCamera extends Fragment
        implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = FragmentCamera.class.getSimpleName();

    private CameraBridgeViewBase mCameraView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        mCameraView = (CameraBridgeViewBase) view.findViewById(R.id.view_camera);
        mCameraView.setVisibility(SurfaceView.VISIBLE);
        mCameraView.setCvCameraViewListener(this);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (OpenCVLoader.init()) {
            mCameraView.enableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
    }

    @Override
    public void onCameraViewStopped() {
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return inputFrame.rgba();
    }
}
