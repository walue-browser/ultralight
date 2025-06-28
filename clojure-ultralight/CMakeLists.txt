cmake_minimum_required(VERSION 3.10)
project(clojure-ultralight)

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Find JNI
find_package(JNI REQUIRED)

# Find Ultralight SDK
# Assuming the SDK is in the parent directory or set ULTRALIGHT_SDK_PATH
if(NOT DEFINED ULTRALIGHT_SDK_PATH)
    set(ULTRALIGHT_SDK_PATH "${CMAKE_CURRENT_SOURCE_DIR}/../ultralight-sdk")
endif()

# Check if we have the SDK
if(NOT EXISTS "${ULTRALIGHT_SDK_PATH}")
    message(FATAL_ERROR "Ultralight SDK not found at ${ULTRALIGHT_SDK_PATH}. Please set ULTRALIGHT_SDK_PATH or place the SDK in the expected location.")
endif()

# Include directories
include_directories(${JNI_INCLUDE_DIRS})
include_directories("${ULTRALIGHT_SDK_PATH}/include")

# Source files
set(SOURCES
    src/native/ultralight_jni.cpp
)

# Create the shared library
add_library(ultralight-jni SHARED ${SOURCES})

# Link libraries
if(WIN32)
    target_link_libraries(ultralight-jni 
        "${ULTRALIGHT_SDK_PATH}/lib/Ultralight.lib"
        "${ULTRALIGHT_SDK_PATH}/lib/UltralightCore.lib"
        "${ULTRALIGHT_SDK_PATH}/lib/WebCore.lib"
        "${ULTRALIGHT_SDK_PATH}/lib/AppCore.lib"
    )
elseif(APPLE)
    target_link_libraries(ultralight-jni 
        "${ULTRALIGHT_SDK_PATH}/lib/libUltralight.dylib"
        "${ULTRALIGHT_SDK_PATH}/lib/libUltralightCore.dylib"
        "${ULTRALIGHT_SDK_PATH}/lib/libWebCore.dylib"
        "${ULTRALIGHT_SDK_PATH}/lib/libAppCore.dylib"
    )
else() # Linux
    target_link_libraries(ultralight-jni 
        "${ULTRALIGHT_SDK_PATH}/lib/libUltralight.so"
        "${ULTRALIGHT_SDK_PATH}/lib/libUltralightCore.so"
        "${ULTRALIGHT_SDK_PATH}/lib/libWebCore.so"
        "${ULTRALIGHT_SDK_PATH}/lib/libAppCore.so"
    )
endif()

# Set output directory
set_target_properties(ultralight-jni PROPERTIES
    LIBRARY_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/native"
    RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/native"
)

# Copy Ultralight libraries to native directory for runtime
if(WIN32)
    add_custom_command(TARGET ultralight-jni POST_BUILD
        COMMAND ${CMAKE_COMMAND} -E copy_if_different
        "${ULTRALIGHT_SDK_PATH}/bin/Ultralight.dll"
        "${ULTRALIGHT_SDK_PATH}/bin/UltralightCore.dll"
        "${ULTRALIGHT_SDK_PATH}/bin/WebCore.dll"
        "${ULTRALIGHT_SDK_PATH}/bin/AppCore.dll"
        "${CMAKE_CURRENT_SOURCE_DIR}/native/"
    )
elseif(APPLE)
    add_custom_command(TARGET ultralight-jni POST_BUILD
        COMMAND ${CMAKE_COMMAND} -E copy_if_different
        "${ULTRALIGHT_SDK_PATH}/lib/libUltralight.dylib"
        "${ULTRALIGHT_SDK_PATH}/lib/libUltralightCore.dylib"
        "${ULTRALIGHT_SDK_PATH}/lib/libWebCore.dylib"
        "${ULTRALIGHT_SDK_PATH}/lib/libAppCore.dylib"
        "${CMAKE_CURRENT_SOURCE_DIR}/native/"
    )
else() # Linux
    add_custom_command(TARGET ultralight-jni POST_BUILD
        COMMAND ${CMAKE_COMMAND} -E copy_if_different
        "${ULTRALIGHT_SDK_PATH}/lib/libUltralight.so"
        "${ULTRALIGHT_SDK_PATH}/lib/libUltralightCore.so"
        "${ULTRALIGHT_SDK_PATH}/lib/libWebCore.so"
        "${ULTRALIGHT_SDK_PATH}/lib/libAppCore.so"
        "${CMAKE_CURRENT_SOURCE_DIR}/native/"
    )
endif()

# Copy resources
add_custom_command(TARGET ultralight-jni POST_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy_directory
    "${ULTRALIGHT_SDK_PATH}/resources"
    "${CMAKE_CURRENT_SOURCE_DIR}/native/resources"
)