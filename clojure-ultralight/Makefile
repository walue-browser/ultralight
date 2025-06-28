# Makefile for Clojure Ultralight

.PHONY: all build clean run-basic run-tasks run-interactive deps help

# Default target
all: build

# Build everything
build:
	@echo "Building Clojure Ultralight..."
	./build.sh

# Install dependencies
deps:
	@echo "Installing dependencies..."
	lein deps

# Clean build artifacts
clean:
	@echo "Cleaning build artifacts..."
	rm -rf build/
	rm -rf target/
	rm -rf native/
	lein clean

# Run demos
run-basic: build
	@echo "Running basic demo..."
	lein run basic

run-tasks: build
	@echo "Running task manager demo..."
	lein run tasks

run-interactive: build
	@echo "Running interactive demo..."
	lein run examples/simple_demo.clj interactive

# Development targets
repl: build
	@echo "Starting REPL..."
	lein repl

compile: build
	@echo "Compiling Clojure code..."
	lein compile

# Testing
test: build
	@echo "Running tests..."
	lein test

# Package for distribution
package: build
	@echo "Creating distribution package..."
	lein uberjar

# Help
help:
	@echo "Available targets:"
	@echo "  all          - Build everything (default)"
	@echo "  build        - Build the project"
	@echo "  deps         - Install dependencies"
	@echo "  clean        - Clean build artifacts"
	@echo "  run-basic    - Run basic demo"
	@echo "  run-tasks    - Run task manager demo"
	@echo "  run-interactive - Run interactive demo"
	@echo "  repl         - Start REPL"
	@echo "  compile      - Compile Clojure code"
	@echo "  test         - Run tests"
	@echo "  package      - Create distribution package"
	@echo "  help         - Show this help"