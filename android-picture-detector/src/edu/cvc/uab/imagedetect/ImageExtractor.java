package edu.cvc.uab.imagedetect;

import org.opencv.core.Mat;

public class ImageExtractor {

	public int detect(Mat image, double threshold, double thresholdVotes, int numMinFeatures, int numTargets, String pathModel) {
		return nativeDetect(image.getNativeObjAddr(), threshold, thresholdVotes, numMinFeatures, numTargets, pathModel);
	}

	public void buildDatabase(int numMinFeatures, int numTargets, String pathModel, String pathFiles) {
		nativeBuildDatabase(numMinFeatures, numTargets, pathModel, pathFiles);
	}

	private static native int nativeDetect(long inputImage, double threshold, double thresholdVotes, int numMinFeatures, int numTargets, String pathModel);

	private static native void nativeBuildDatabase(int numMinFeatures, int numTargets, String pathModel, String pathFiles);

}
