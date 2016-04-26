#include "ImageClassifier.h"
#include "gtest/gtest.h"
#include <opencv2/opencv.hpp>
#include <iostream>

// In this example, we test the MyString class (a simple string).

//// Tests the default c'tor.
//TEST(ImageClassifier, DefaultConstructor) {
 // const ImageClassifier classifier();
//  EXPECT_STREQ(NULL, s.c_string());
//  EXPECT_EQ(0u, s.Length());
//}
// Create new model
//TEST(ImageClassifier, createDatabase) {
//	ImageClassifier classifier;
//	classifier.CreateDatabase(std::string("/sdcard/model_images"));
//}

TEST(ImageClassifier, detectImage) {
	ImageClassifier classifier(5,21,std::string("/sdcard/DB.matcv"));
	cv::Mat image = cv::imread(ImageClassifier::PATH_MODEL+std::string("test1.bmp"));
	int value = classifier.Detection(image,10.0,50.0);
	std::cout << "value: " << value << std::endl;
}


