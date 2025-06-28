#!/bin/bash

# Build script for Clojure Ultralight JNI wrapper

set -e

echo "Building Clojure Ultralight JNI wrapper..."

# Check if Leiningen is installed
if ! command -v lein &> /dev/null; then
    echo "Leiningen is required but not installed. Please install it first."
    echo "Visit: https://leiningen.org/"
    exit 1
fi

# Check if cmake is installed
if ! command -v cmake &> /dev/null; then
    echo "CMake is required but not installed. Please install it first."
    exit 1
fi

# Download Ultralight SDK if not present
SDK_DIR="ultralight-sdk"
if [ ! -d "$SDK_DIR" ]; then
    echo "Downloading Ultralight SDK..."
    
    # Detect platform
    if [[ "$OSTYPE" == "linux-gnu"* ]]; then
        SDK_URL="https://ul-sdk.nyc3.digitaloceanspaces.com/1.4.0/linux/x64/release/ultralight-free-sdk-1.4.0-linux-x64.7z"
        SDK_FILE="ultralight-free-sdk-1.4.0-linux-x64.7z"
    elif [[ "$OSTYPE" == "darwin"* ]]; then
        SDK_URL="https://ul-sdk.nyc3.digitaloceanspaces.com/1.4.0/mac/x64/release/ultralight-free-sdk-1.4.0-mac-x64.7z"
        SDK_FILE="ultralight-free-sdk-1.4.0-mac-x64.7z"
    else
        echo "Unsupported platform: $OSTYPE"
        exit 1
    fi
    
    # Download SDK
    if command -v wget &> /dev/null; then
        wget -O "$SDK_FILE" "$SDK_URL"
    elif command -v curl &> /dev/null; then
        curl -L -o "$SDK_FILE" "$SDK_URL"
    else
        echo "Neither wget nor curl found. Please download the SDK manually:"
        echo "$SDK_URL"
        exit 1
    fi
    
    # Extract SDK
    if command -v 7z &> /dev/null; then
        7z x "$SDK_FILE"
        mv ultralight-free-sdk-* "$SDK_DIR"
    else
        echo "7z is required to extract the SDK. Please install p7zip-full or similar."
        exit 1
    fi
    
    rm "$SDK_FILE"
    echo "SDK downloaded and extracted to $SDK_DIR"
fi

# Create build directory
mkdir -p build
cd build

# Configure with CMake
echo "Configuring with CMake..."
cmake .. -DULTRALIGHT_SDK_PATH="../$SDK_DIR"

# Build the native library
echo "Building native library..."
make -j$(nproc 2>/dev/null || sysctl -n hw.ncpu 2>/dev/null || echo 4)

cd ..

# Generate JNI headers (if needed)
echo "Generating JNI headers..."
mkdir -p target/classes
javac -cp "$(lein classpath)" -d target/classes src/java/com/ultralight/UltralightApp.java
javah -cp target/classes -d src/native com.ultralight.UltralightApp 2>/dev/null || true

# Compile Clojure code
echo "Compiling Clojure code..."
lein compile

echo "Build completed successfully!"
echo ""
echo "To run the demo:"
echo "  lein run basic    # Basic demo"
echo "  lein run tasks    # Task manager demo"
echo ""
echo "Or use the REPL:"
echo "  lein repl"