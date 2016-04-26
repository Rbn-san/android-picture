TOP_LOCAL_PATH := $(call my-dir)
include $(call all-subdir-makefiles)
#Restore LOCAL_PATH, $(CLEAR_VARS) not clear the variable LOCAL_PATH
LOCAL_PATH := $(TOP_LOCAL_PATH)
include $(CLEAR_VARS)
#Activate to use tests
TESTS:=off

OPENCV_CAMERA_MODULES := on
OPENCV_INSTALL_MODULES := on
OPENCV_LIB_TYPE:=SHARED
OPENCV_PATH:=/home/carlos/Desktop/tools/OpenCV-2.4.9-android-sdk/sdk/native/jni/OpenCV.mk
#OPENCV_PATH:=../OpenCV-2.4.9-android-sdk/sdk/native/jni/OpenCV.mk
$(info $(LOCAL_SRC_FILES))
include $(OPENCV_PATH)


ifeq ($(TESTS),on)
	#Specify to use gtest library
	LOCAL_STATIC_LIBRARIES := gtest
	LOCAL_SRC_FILES := all_tests.cpp ImageClassifier_unittest.cpp
	BINARY=$(BUILD_EXECUTABLE)
	LOCAL_MODULE:= image_extractor_tests
else
	BINARY=$(BUILD_SHARED_LIBRARY)
	LOCAL_MODULE := image_extractor
endif


LOCAL_SRC_FILES  += ImageExtractor_jni.cpp ImageClassifier.cpp
$(info $(LOCAL_SRC_FILES))

LOCAL_C_INCLUDES += $(LOCAL_PATH) jni/googletest/googletest/include jni/ .
LOCAL_LDLIBS     += -llog -ldl
LOCAL_LDLIBS    += -landroid


include $(BINARY)
