LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := libslflogger
LOCAL_SRC_FILES := $(LOCAL_PATH)/../libslflogger/$(TARGET_ARCH_ABI)/libslflogger.a
include $(PREBUILT_STATIC_LIBRARY)