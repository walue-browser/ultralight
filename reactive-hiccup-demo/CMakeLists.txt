cmake_minimum_required(VERSION 3.3.2)
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

project(ReactiveHiccupDemo)

# Set paths to Ultralight SDK
set(ULTRALIGHT_SDK_PATH "${CMAKE_SOURCE_DIR}/SDK")
set(ULTRALIGHT_INCLUDE_DIR "${ULTRALIGHT_SDK_PATH}/include")
set(ULTRALIGHT_BINARY_DIR "${ULTRALIGHT_SDK_PATH}/bin")
set(ULTRALIGHT_LIBRARY_DIR "${ULTRALIGHT_SDK_PATH}/bin")  # Libraries are in bin directory
set(ULTRALIGHT_RESOURCES_DIR "${ULTRALIGHT_SDK_PATH}/resources")
set(ULTRALIGHT_INSPECTOR_DIR "${ULTRALIGHT_SDK_PATH}/inspector")

# Include directories
include_directories(${ULTRALIGHT_INCLUDE_DIR})

# Add executable
add_executable(${PROJECT_NAME} main.cpp)

# Set RPATH to find shared libraries in the same directory as the executable
if (UNIX AND NOT APPLE)
    set_target_properties(${PROJECT_NAME} PROPERTIES
        INSTALL_RPATH "$ORIGIN"
        BUILD_WITH_INSTALL_RPATH TRUE)
endif()

# Link libraries
if (UNIX)
    target_link_libraries(${PROJECT_NAME} 
        ${ULTRALIGHT_LIBRARY_DIR}/libUltralightCore.so
        ${ULTRALIGHT_LIBRARY_DIR}/libUltralight.so
        ${ULTRALIGHT_LIBRARY_DIR}/libWebCore.so
        ${ULTRALIGHT_LIBRARY_DIR}/libAppCore.so)
else ()
    target_link_libraries(${PROJECT_NAME} UltralightCore Ultralight WebCore AppCore)
endif ()

if (APPLE)
    set_target_properties(${PROJECT_NAME} PROPERTIES
        MACOSX_BUNDLE TRUE
    )
endif ()

# Copy assets to output directory
file(COPY ${CMAKE_CURRENT_SOURCE_DIR}/assets DESTINATION ${CMAKE_CURRENT_BINARY_DIR})

# Copy Ultralight resources to output directory
add_custom_command(TARGET ${PROJECT_NAME} POST_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy_directory
    ${ULTRALIGHT_RESOURCES_DIR} ${CMAKE_CURRENT_BINARY_DIR}/resources)

# Copy Ultralight inspector to output directory
add_custom_command(TARGET ${PROJECT_NAME} POST_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy_directory
    ${ULTRALIGHT_INSPECTOR_DIR} ${CMAKE_CURRENT_BINARY_DIR}/inspector)

# Copy Ultralight shared libraries to output directory
add_custom_command(TARGET ${PROJECT_NAME} POST_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy
    ${ULTRALIGHT_LIBRARY_DIR}/libUltralightCore.so ${CMAKE_CURRENT_BINARY_DIR}/libUltralightCore.so)
add_custom_command(TARGET ${PROJECT_NAME} POST_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy
    ${ULTRALIGHT_LIBRARY_DIR}/libUltralight.so ${CMAKE_CURRENT_BINARY_DIR}/libUltralight.so)
add_custom_command(TARGET ${PROJECT_NAME} POST_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy
    ${ULTRALIGHT_LIBRARY_DIR}/libWebCore.so ${CMAKE_CURRENT_BINARY_DIR}/libWebCore.so)
add_custom_command(TARGET ${PROJECT_NAME} POST_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy
    ${ULTRALIGHT_LIBRARY_DIR}/libAppCore.so ${CMAKE_CURRENT_BINARY_DIR}/libAppCore.so)

# Create a release package
add_custom_target(package
    COMMAND ${CMAKE_COMMAND} -E make_directory ${CMAKE_BINARY_DIR}/package/reactive-hiccup-demo
    COMMAND ${CMAKE_COMMAND} -E copy $<TARGET_FILE:${PROJECT_NAME}> ${CMAKE_BINARY_DIR}/package/reactive-hiccup-demo/
    COMMAND ${CMAKE_COMMAND} -E copy_directory ${CMAKE_CURRENT_BINARY_DIR}/assets ${CMAKE_BINARY_DIR}/package/reactive-hiccup-demo/assets
    COMMAND ${CMAKE_COMMAND} -E copy_directory ${CMAKE_CURRENT_BINARY_DIR}/resources ${CMAKE_BINARY_DIR}/package/reactive-hiccup-demo/resources
    COMMAND ${CMAKE_COMMAND} -E copy_directory ${CMAKE_CURRENT_BINARY_DIR}/inspector ${CMAKE_BINARY_DIR}/package/reactive-hiccup-demo/inspector
    COMMAND ${CMAKE_COMMAND} -E copy ${CMAKE_CURRENT_BINARY_DIR}/libUltralightCore.so ${CMAKE_BINARY_DIR}/package/reactive-hiccup-demo/
    COMMAND ${CMAKE_COMMAND} -E copy ${CMAKE_CURRENT_BINARY_DIR}/libUltralight.so ${CMAKE_BINARY_DIR}/package/reactive-hiccup-demo/
    COMMAND ${CMAKE_COMMAND} -E copy ${CMAKE_CURRENT_BINARY_DIR}/libWebCore.so ${CMAKE_BINARY_DIR}/package/reactive-hiccup-demo/
    COMMAND ${CMAKE_COMMAND} -E copy ${CMAKE_CURRENT_BINARY_DIR}/libAppCore.so ${CMAKE_BINARY_DIR}/package/reactive-hiccup-demo/
    COMMAND ${CMAKE_COMMAND} -E tar "cfz" "${CMAKE_BINARY_DIR}/ultralight-reactive-hiccup-demo-linux-x64.tar.gz" --format=gnutar -C ${CMAKE_BINARY_DIR}/package "reactive-hiccup-demo"
    DEPENDS ${PROJECT_NAME}
    COMMENT "Creating release package")