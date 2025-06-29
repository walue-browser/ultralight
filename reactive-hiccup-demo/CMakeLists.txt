cmake_minimum_required(VERSION 3.3.2)
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

project(ReactiveHiccupDemo)

add_executable(${PROJECT_NAME} main.cpp)

if (UNIX)
    target_link_libraries(${PROJECT_NAME} UltralightCore Ultralight WebCore AppCore)
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