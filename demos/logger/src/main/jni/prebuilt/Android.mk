LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := libnativelogger
LOCAL_SRC_FILES := $(NATIVE_LOGGER_PATH)/libs/$(TARGET_ARCH_ABI)/libnativelogger.so
include $(PREBUILT_SHARED_LIBRARY)