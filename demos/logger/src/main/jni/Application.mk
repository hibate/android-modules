# Uncomment this if you're using STL in your project
# See CPLUSPLUS-SUPPORT.html in the NDK documentation for more information

ifeq (,$(PROJECT_PATH))

# 项目路径(绝对路径): jni 上一级目录路径(使用 AS 则为 module 路径)
PROJECT_PATH := $(call find-project-dir, ., .)
NATIVE_LOGGER_PATH = $(PROJECT_PATH)/../../commons/logger/nativelogger/src/main

else

# PROJECT_PATH 由 build.bat 脚本传递: jni 目录
NATIVE_LOGGER_PATH = $(PROJECT_PATH)/../../../../commons/logger/nativelogger/src/main

endif

APP_STL := stlport_static

APP_OPTIM := release
# APP_ABI := arm64-v8a armeabi armeabi-v7a mips mips64 x86 x86_64
APP_ABI := all