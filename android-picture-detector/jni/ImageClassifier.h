#ifndef IMAGE_CLASSIFIER
#define IMAGE_CLASSIFIER

#include <opencv2/opencv.hpp>
#include <queue>
#include <string>
#include "stdint.h"


#define M_PI				3.14159265358979323846

#define NUMBER_OF_FEATURES	500
#define NUMBER_OF_FEATURES_DB 500 //500
#define SCALE_FACTOR		1.2f
#define NUMBER_OF_LEVELS	16
#define EDGE_THRESHOLD		31
#define FIRST_LEVEL			0
#define WTA_K				2
#define SCORE_TYPE			0
#define PATCH_SIZE			31

class ImageClassifier
{
		int num_min_features;
		int number_of_images_in_db;
		std::string path_model;

		uint16_t *m_vectorToComputeMode;
		
		cv::FeatureDetector *m_detector;
		cv::DescriptorExtractor *m_descriptor;
		//cv::FREAK * m_detector;
		//cv::FREAK * m_descriptor;

		cv::vector<cv::KeyPoint> m_vectorOfKeyPointsSplitted;
		cv::Mat m_featureVectorsDB;
		cv::Mat m_labelsDB;
		//cv::vector<std::string> m_fileNamesDB;
		cv::Mat m_mean;
		cv::Mat m_stddev;
		//cv::FlannBasedMatcher *m_Matcher;
		cv::BFMatcher *m_Matcher;
		cv::vector<std::string> m_fileNamesDB;


		std::deque<int> vectorOfVotes;
		std::vector<cv::Mat> vectorOfHomographys;
		std::vector<double> vectorOfR;
		std::vector<int> vectorOfC;



public:
		int Detection(cv::Mat & p_Image, double threshold, double threshold_votes);
		static void PreProcessImageInput(cv::Mat &p_Image,cv::Mat &p_Result);
		void CreateDatabase(std::string path_models);
		ImageClassifier(int num_min_features, int number_of_images_in_db, std::string path_model);
		~ImageClassifier();

		inline bool hasEnding(std::string const &fullString, std::string const &ending) {
			if (fullString.length() >= ending.length()) {
				return (0
						== fullString.compare(fullString.length() - ending.length(),
								ending.length(), ending));
			} else {
				return false;
			}
		};
};



#endif
