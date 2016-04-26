#!/bin/sh
find ../libs/armeabi-v7a/* -exec adb push {} /data/local/tmp \;
#push files 
adb shell LD_LIBRARY_PATH="/data/local/tmp:/vendor/lib:/system/lib" /data/local/tmp/image_extractor_tests


