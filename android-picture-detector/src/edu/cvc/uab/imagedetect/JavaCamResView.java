package edu.cvc.uab.imagedetect;

import java.util.List;

import org.opencv.android.JavaCameraView;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.util.AttributeSet;
import android.widget.Toast;

public class JavaCamResView extends JavaCameraView implements AutoFocusCallback {

	public JavaCamResView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public List<Camera.Size> getResolutionList() {
		return mCamera.getParameters().getSupportedPreviewSizes();
	}

	public void setResolution(Camera.Size resolution) {
		disconnectCamera();
		connectCamera((int) resolution.width, (int) resolution.height);
	}

	public void setFocusMode(Context item, int type) {

		Camera.Parameters params = mCamera.getParameters();

		List<String> FocusModes = params.getSupportedFocusModes();

		switch (type) {
		case 0:
			if (FocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
				params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
			else
				Toast.makeText(item, "Auto Mode not supported", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			if (FocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO))
				params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
			else
				Toast.makeText(item, "Continuous Mode not supported", Toast.LENGTH_SHORT).show();
			break;
		case 2:
			if (FocusModes.contains(Camera.Parameters.FOCUS_MODE_EDOF))
				params.setFocusMode(Camera.Parameters.FOCUS_MODE_EDOF);
			else
				Toast.makeText(item, "EDOF Mode not supported", Toast.LENGTH_SHORT).show();
			break;
		case 3:
			if (FocusModes.contains(Camera.Parameters.FOCUS_MODE_FIXED))
				params.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
			else
				Toast.makeText(item, "Fixed Mode not supported", Toast.LENGTH_SHORT).show();
			break;
		case 4:
			if (FocusModes.contains(Camera.Parameters.FOCUS_MODE_INFINITY))
				params.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
			else
				Toast.makeText(item, "Infinity Mode not supported", Toast.LENGTH_SHORT).show();
			break;
		case 5:
			if (FocusModes.contains(Camera.Parameters.FOCUS_MODE_MACRO))
				params.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
			else
				Toast.makeText(item, "Macro Mode not supported", Toast.LENGTH_SHORT).show();
			break;
		}

		mCamera.setParameters(params);

	}

	public Camera.Size getResolution() {

		Camera.Parameters params = mCamera.getParameters();

		Camera.Size s = params.getPreviewSize();
		return s;
	}

	public void autoFocus() {
		mCamera.autoFocus(this);

	}

	@Override
	public void onAutoFocus(boolean success, Camera camera) {
		// TODO Auto-generated method stub

	}
}