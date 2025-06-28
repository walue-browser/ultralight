#!/bin/bash

# Ultralight FPS Test Installation Script

echo "Installing Ultralight FPS Test..."

# Create installation directory
INSTALL_DIR="/usr/local/ultralight-fpstest"
sudo mkdir -p $INSTALL_DIR

# Copy files
sudo cp -r fpstest/* $INSTALL_DIR

# Create symlink
sudo ln -sf $INSTALL_DIR/fpstest /usr/local/bin/ultralight-fpstest

echo "Installation complete!"
echo "Run the application with: ultralight-fpstest"