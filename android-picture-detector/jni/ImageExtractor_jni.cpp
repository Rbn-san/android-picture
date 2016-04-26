#include <ImageExtractor_jni.h>
#include <opencv2/core/core.hpp>
#include  "ImageClassifier.h"
//#include <opencv2/opencv.hpp>


#include <string>
#include <vector>

#include <android/log.h>

#define LOG_TAG "ImageDetection/ImageExtractor"
#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__))

using namespace std;
using namespace cv;

JNIEXPORT int JNICALL Java_edu_cvc_uab_imagedetect_ImageExtractor_nativeDetect
(JNIEnv * jenv, jclass, jlong image, jdouble threshold, jdouble threshold_votes,
		jint num_min_features, jint num_targets, jstring path_model)
{



	char const* char_path_model;
	char_path_model = jenv->GetStringUTFChars(path_model , NULL ) ;
	std::string str_path_model(char_path_model);

    LOGD("Java_edu_cvc_uab_imagedetect_ImageExtractor_nativeDetect enter");
    LOGD("Threshold to apply %f",threshold);
    LOGD("Threshold votes to apply %f",threshold_votes);
    LOGD("Num targets to apply %d",num_targets);
    LOGD("Num min features to apply %d",num_min_features);
    LOGD("Path model to get database %s",str_path_model.c_str());



    Mat *pImage = ((Mat *)image);
    ImageClassifier classifier = ImageClassifier((int)num_min_features,(int)num_targets,str_path_model);
	int value = classifier.Detection(*pImage,(double)threshold,(double)threshold_votes);
	LOGD("Element number of the model = %d",value);
	return value;

}

JNIEXPORT int JNICALL Java_edu_cvc_uab_imagedetect_ImageExtractor_nativeBuildDatabase
(JNIEnv * jenv, jclass, jint num_min_features, jint num_targets, jstring path_model, jstring path_files)
{
	LOGD("Java_edu_cvc_uab_imagedetect_ImageExtractor_nativeBuildDatabase enter");

	char const* char_path_files;
	char_path_files = jenv->GetStringUTFChars(path_files , NULL ) ;
	std::string str_path_files(char_path_files);

	char const* char_path_model;
	char_path_model = jenv->GetStringUTFChars(path_model , NULL ) ;
	std::string str_path_model(char_path_model);

 	LOGD("Num targets to apply %d",num_targets);
    	LOGD("Num min features to apply %d",num_min_features);
    	LOGD("Path model to create database %s",str_path_model.c_str());
    	LOGD("Path of files to use for the database %s",str_path_files.c_str());


	ImageClassifier classifier = ImageClassifier((int)num_min_features,(int)num_targets,str_path_model);
	classifier.CreateDatabase(str_path_files);
	LOGD("All elements are classified");

}
