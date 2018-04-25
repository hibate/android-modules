LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LIBS_SRC = \
    jniutil.cpp

SRC = \
    $(LIBS_SRC)

INCLUDES = \
    $(LOCAL_PATH)

EXPORT_INCLUDES = \
    $(LOCAL_PATH)

LOCAL_MODULE    := libjniutils
LOCAL_SRC_FILES := $(SRC)
LOCAL_C_INCLUDES := $(INCLUDES)
LOCAL_EXPORT_C_INCLUDES := $(EXPORT_INCLUDES)

include $(BUILD_STATIC_LIBRARY)