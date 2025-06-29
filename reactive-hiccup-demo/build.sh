#!/bin/bash

# Navigate to the project root
cd "$(dirname "$0")/.."

# Create build directory if it doesn't exist
mkdir -p build
cd build

# Configure and build
cmake .. -DCMAKE_BUILD_TYPE=Release
cmake --build . --config Release

# Copy assets if needed
if [ -d "reactive-hiccup-demo" ]; then
  echo "Copying assets to build directory..."
  cp -r ../reactive-hiccup-demo/assets reactive-hiccup-demo/
fi

echo "Build completed. The executable is in build/reactive-hiccup-demo/"