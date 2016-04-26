#include "ImageClassifier.h"
#include <android/asset_manager.h>
#include "opencv2/features2d/features2d.hpp"
#include <fstream>
#include <dirent.h>
#include <iostream>
#include <cassert>
#include <android/log.h>

#define LOG_TAG "ImageDetection/ImageClassifier"
#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__))


ImageClassifier::ImageClassifier(int num_min_features, int number_of_images_in_db, std::string path_model) {

	this->num_min_features = num_min_features;
	this->number_of_images_in_db = number_of_images_in_db;
	this->path_model = path_model;

	m_detector = new cv::ORB(NUMBER_OF_FEATURES, SCALE_FACTOR, NUMBER_OF_LEVELS,
			EDGE_THRESHOLD, FIRST_LEVEL, WTA_K, SCORE_TYPE, PATCH_SIZE); //new cv::SURF(100);
	m_descriptor = new cv::ORB(NUMBER_OF_FEATURES, SCALE_FACTOR,
			NUMBER_OF_LEVELS, EDGE_THRESHOLD, FIRST_LEVEL, WTA_K, SCORE_TYPE,
			PATCH_SIZE); //new cv::SURF(100);  //ORB(400,2.4f,8,31,0,2,0,9);
	//m_detector = new cv::BRISK();
	//m_descriptor = new cv::BRISK();

	m_vectorToComputeMode = new uint16_t[this->number_of_images_in_db];

	m_vectorOfKeyPointsSplitted.resize(this->number_of_images_in_db);

	// Create the matcher and do the match between the input image and the database of features vectors
	//cv::BFMatcher matcher(cv::Hamming::normType, false);
	cv::Ptr < cv::flann::LshIndexParams > index = new cv::flann::LshIndexParams(
			6, 12, 1);
	cv::Ptr < cv::flann::SearchParams > search = new cv::flann::SearchParams(
			10);
	//m_Matcher = new cv::FlannBasedMatcher(index, search );
	m_Matcher = new cv::BFMatcher(cv::NORM_HAMMING);

	cv::FileStorage file(this->path_model,cv::FileStorage::READ);
	cv::read(file["KEYPOINTS_SPLITTED"], m_vectorOfKeyPointsSplitted);
	file["DESCRIPTORS"] >> m_featureVectorsDB;
	file["LABELS"] >> m_labelsDB;
	file["FILENAMES"] >> m_fileNamesDB;
	file["MEAN"] >> m_mean;
	file["STDDEV"] >> m_stddev;
	file.release();
	std::vector < cv::Mat > vmat;
	vmat.insert(vmat.end(), m_featureVectorsDB);
	LOGD("Size m_featureVectorsDB (%d, %d), %d",m_featureVectorsDB.rows,m_featureVectorsDB.cols,m_featureVectorsDB.type());
	if (m_featureVectorsDB.rows == 0 && m_featureVectorsDB.cols == 0 ) {
		assert("It was not loaded the data model information");
	}
	m_Matcher->add(vmat);
	m_Matcher->train();

}

ImageClassifier::~ImageClassifier() {
	delete m_detector;
	delete m_descriptor;
	delete[] m_vectorToComputeMode;
	delete m_Matcher;
}

void ImageClassifier::PreProcessImageInput(cv::Mat &p_Image,
		cv::Mat &p_Result) {
	//TODO CHECK WHY IT IS NECESSARY COPY
	p_Image.copyTo(p_Result);
	cv::cvtColor(p_Image, p_Result, CV_BGR2GRAY);

}

int ImageClassifier::Detection(cv::Mat & p_Image, double threshold, double threshold_votes) {
	LOGD("Size p_image (%d, %d)",p_Image.rows,p_Image.cols);


	int idAcquisition = 33;
	// Clean the mode vector
	for (int i = 0; i < (this->number_of_images_in_db); i++)
		m_vectorToComputeMode[i] = 0;
	std::vector < cv::KeyPoint > vectorOfKeyPoints;
	cv::Mat featureVectors;

	// Do some process to the input image
	cv::Mat inputProcessed;
	PreProcessImageInput(p_Image, inputProcessed);
	srand(0);
	// Calculate the processing time
	// Detect the keypoints and compute their descriptors
	m_detector->detect(inputProcessed, vectorOfKeyPoints);

	m_descriptor->compute(inputProcessed, vectorOfKeyPoints, featureVectors);
	// Normalize the features vectors in range -1,1 (normalization by feature)
	std::vector < std::vector<cv::DMatch> > matches;
	m_Matcher->knnMatch(featureVectors, matches, 2);
	double max_dist = 0;
	int min_dist_idx = 0, min_dist_idx2 = 0;
	double min_dist = 10000;
	if (featureVectors.rows < this->num_min_features)
		return -1;
	int step = 0;

	for (int i = 0; i < featureVectors.rows; i++) //featureVectors
			{
		for (int j = 0; j < 1; j++) {
			//TODO add threshold
			/*if(matches[i][j].distance < threshold)
			{*/
				//std::cout << "Distance " <<  matches[i][j].distance << std::endl;
				m_vectorToComputeMode[m_labelsDB.at<int>(matches[i][j].trainIdx)]++;
			//}
		}
	}

	int maxMode = 0;
	int maxModeIdx = -1;

	for (int i = 0; i < this->number_of_images_in_db; i++) {
		if (m_vectorToComputeMode[i] > maxMode) {
			maxMode = m_vectorToComputeMode[i];
			maxModeIdx = i;
			LOGD(" Class %d maxMode %d \n",i + 1,maxMode);
			std::cout <<" Class "<<i + 1<<"  maxMode "<<maxMode<< std::endl;
		}
		LOGD("FOR: Class %d maxMode %d \n",i + 1,maxMode);
	}
	return maxModeIdx;
}

// Function that generates the Database given a set of models
void ImageClassifier::CreateDatabase(std::string path_models) {
	/* Read files. */
	DIR *dir;
	struct dirent *ent;
	//check if it is working.
	if ((dir = opendir(path_models.c_str())) == NULL) {
		/* could not open directory. */
		//std::cout  << "I could not open the directory path:  " << path_models << std::endl;
		return;
	}

	// Initialise detector
	cv::FeatureDetector *detector = new cv::ORB(NUMBER_OF_FEATURES_DB,
			SCALE_FACTOR, NUMBER_OF_LEVELS, EDGE_THRESHOLD, FIRST_LEVEL, WTA_K,
			SCORE_TYPE, PATCH_SIZE);
	cv::DescriptorExtractor *descriptor = new cv::ORB(NUMBER_OF_FEATURES_DB,
			SCALE_FACTOR, NUMBER_OF_LEVELS, EDGE_THRESHOLD, FIRST_LEVEL, WTA_K,
			SCORE_TYPE, PATCH_SIZE);

	int id = 0;
	cv::vector < cv::KeyPoint > vectorOfKeyPointsSplitted;
	cv::Mat featureVectorsDB;
	cv::Mat labelsDB;
	cv::vector < std::string > fileNamesDB;

	cv::Mat image;

	while ((ent = readdir(dir)) != NULL) {
		/* preparing image */
		//Checking condition for the file
		if ( ! hasEnding(std::string(ent->d_name),".bmp") &&
		! hasEnding(std::string(ent->d_name),".png") &&
		! hasEnding(std::string(ent->d_name),".jpg") ) {
			continue;
		}
		fileNamesDB.push_back(std::string(ent->d_name));
		image = cv::imread(
				std::string(path_models) + std::string("/") + std::string(ent->d_name));
		std::vector < cv::KeyPoint > vectorOfKeyPoints;
		cv::Mat featureVectors;
		detector->detect(image, vectorOfKeyPoints);


		descriptor->compute(image, vectorOfKeyPoints, featureVectors);


		vectorOfKeyPointsSplitted.insert(vectorOfKeyPointsSplitted.end(),
				vectorOfKeyPoints.begin(), vectorOfKeyPoints.end());


		cv::Mat labels = cv::Mat::ones(featureVectors.rows, 1, CV_32S);
		labels = labels * id;

		if (id > 0) {
			cv::vconcat(featureVectorsDB, featureVectors, featureVectorsDB);
			cv::vconcat(labelsDB, labels, labelsDB);

		} else {
			featureVectors.copyTo(featureVectorsDB);
			labels.copyTo(labelsDB); //= labels;
		}

		id++;

	}
	closedir(dir);

	cv::Mat normalizeVector;
	cv::Mat mean64 = cv::Mat::zeros(cv::Size(featureVectorsDB.cols, 1), CV_64F);
	cv::Mat stddev64 = cv::Mat::zeros(cv::Size(featureVectorsDB.cols, 1),
			CV_64F);
	cv::Mat mean32;
	cv::Mat stddev32;

	for (int i = 0; i < featureVectorsDB.cols; i++) {
		cv::meanStdDev(featureVectorsDB.col(i), mean64.col(i), stddev64.col(i));
	}

	mean64.convertTo(mean32, CV_32F);
	stddev64.convertTo(stddev32, CV_32F);

	cv::FileStorage file(this->path_model, cv::FileStorage::WRITE);
	file << "KEYPOINTS_SPLITTED" << vectorOfKeyPointsSplitted;
	file << "DESCRIPTORS" << featureVectorsDB;
	file << "LABELS" << labelsDB;
	file << "FILENAMES" << fileNamesDB;
	file << "MEAN" << mean32;
	file << "STDDEV" << stddev32;
	file.release();
}
