#ifndef ALL_TESTS_H
#define ALL_TESTS_H

#include "gtest/gtest.h"
#include <iostream>
int main(int argc, char **argv) {
    ::testing::InitGoogleTest(&argc, argv);
      return RUN_ALL_TESTS();
}
#endif



