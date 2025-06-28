# Clojure Ultralight

A Clojure wrapper for the Ultralight SDK that enables building high-performance HTML/CSS/JavaScript applications with native performance.

## Overview

This project provides a Clojure interface to the Ultralight SDK, allowing you to:

- Create native desktop applications using HTML, CSS, and JavaScript
- Generate dynamic HTML content from Clojure data structures using Hiccup
- Execute JavaScript from Clojure and get results back
- Build cross-platform applications with native performance
- Leverage the full power of modern web technologies in desktop apps

## Features

- **Native Performance**: Uses the Ultralight SDK for GPU-accelerated rendering
- **Clojure Integration**: Generate HTML from Clojure data structures using Hiccup
- **JavaScript Interop**: Execute JavaScript and get results back in Clojure
- **Real-time Updates**: Update content dynamically from Clojure
- **Cross-platform**: Works on Linux, macOS, and Windows
- **Easy to Use**: Simple API inspired by Sample 2 Basic App

## Requirements

- Java 8 or higher
- Leiningen
- CMake 3.10 or higher
- C++ compiler (GCC, Clang, or MSVC)
- 7z (for extracting the Ultralight SDK)

### Platform-specific Requirements

**Linux:**
- X11 development libraries
- OpenGL libraries

**macOS:**
- Xcode command line tools

**Windows:**
- Visual Studio 2017 or higher

## Installation

1. Clone this repository:
```bash
git clone https://github.com/walue-browser/ultralight.git
cd ultralight/clojure-ultralight
```

2. Run the build script:
```bash
./build.sh
```

This will:
- Download the appropriate Ultralight SDK for your platform
- Build the JNI native library
- Compile the Clojure code

## Quick Start

### Basic Demo

Run the basic demo to see dynamic content generation:

```bash
lein run basic
```

This creates a window with content generated from Clojure data structures.

### Task Manager Demo

Run the task manager demo (similar to Sample 2 Basic App):

```bash
lein run tasks
```

This creates a beautiful task management interface with data from Clojure.

### Using the REPL

Start a REPL for interactive development:

```bash
lein repl
```

Then try:

```clojure
(require '[clojure-ultralight.core :as ul])

;; Create an app
(def app (ul/create-app 800 600 "My Clojure App"))

;; Load some data
(ul/load-data-as-html app {:title "Hello from REPL"
                           :message "This is interactive!"
                           :items ["REPL-driven development"
                                   "Live coding"
                                   "Instant feedback"]})

;; Run the app (this will block until the window is closed)
(ul/run-app app)

;; Clean up
(.destroy app)
```

## API Reference

### Core Functions

#### `create-app [width height title]`
Creates a new Ultralight application window.

#### `load-data-as-html [app data]`
Generates HTML from a Clojure data map and loads it into the app.

#### `load-tasks-as-html [app tasks]`
Loads a task list as HTML (task manager interface).

#### `run-app [app]`
Starts the application main loop (blocks until window is closed).

#### `execute-js [app script]`
Executes JavaScript in the application.

#### `execute-js-with-result [app script]`
Executes JavaScript and returns the result as a string.

#### `set-title [app title]`
Sets the window title.

### Data Structure Format

The `load-data-as-html` function expects a map with these optional keys:

```clojure
{:title "Window Title"
 :subtitle "Subtitle text"
 :message "Main message"
 :items ["List" "of" "items"]
 :stats {"Label" "Value" "pairs"}}
```

### Task Format

The `load-tasks-as-html` function expects a vector of task maps:

```clojure
[{:name "Task name"
  :category "Category"
  :completed true/false}]
```

## Examples

### Dynamic Content Updates

```clojure
(require '[clojure-ultralight.core :as ul])

(defn real-time-demo []
  (let [app (ul/create-app 900 600 "Real-time Demo")]
    ;; Initial content
    (ul/load-data-as-html app {:title "Real-time Updates"
                               :message "Watch this change!"})
    
    ;; Update every 2 seconds
    (future
      (loop [counter 0]
        (Thread/sleep 2000)
        (ul/update-content app {:title "Real-time Updates"
                                :message (str "Updated " counter " times!")
                                :stats {"Counter" counter
                                        "Time" (str (java.util.Date.))}})
        (recur (inc counter))))
    
    (ul/run-app app)
    (.destroy app)))
```

### JavaScript Interaction

```clojure
(require '[clojure-ultralight.core :as ul])

(let [app (ul/create-app 800 600 "JS Demo")]
  (ul/load-data-as-html app {:title "JavaScript Demo"})
  
  ;; Execute JavaScript
  (ul/execute-js app "document.body.style.backgroundColor = 'lightblue';")
  
  ;; Get data from JavaScript
  (let [title (ul/execute-js-with-result app "document.title")]
    (println "Current title:" title))
  
  (ul/run-app app)
  (.destroy app))
```

### Custom HTML Generation

```clojure
(require '[clojure-ultralight.core :as ul]
         '[hiccup.core :as hiccup])

(defn custom-html [data]
  (hiccup/html
    [:html
     [:head [:title (:title data)]]
     [:body
      [:h1 (:title data)]
      [:div
       (for [item (:items data)]
         [:p item])]]]))

(let [app (ul/create-app 800 600 "Custom HTML")]
  (.loadHTMLContent app (custom-html {:title "Custom Layout"
                                      :items ["Item 1" "Item 2" "Item 3"]}))
  (ul/run-app app)
  (.destroy app))
```

## Architecture

The project consists of three main components:

1. **Java JNI Wrapper** (`UltralightApp.java`): Provides the Java interface to the native Ultralight SDK
2. **C++ JNI Implementation** (`ultralight_jni.cpp`): Implements the native methods using the Ultralight SDK
3. **Clojure API** (`core.clj`): Provides a Clojure-friendly API with HTML generation capabilities

## Building from Source

If you need to modify the native code:

1. Make changes to `src/native/ultralight_jni.cpp`
2. Rebuild with CMake:
```bash
cd build
make
```

If you modify the Java interface:

1. Update `src/java/com/ultralight/UltralightApp.java`
2. Regenerate JNI headers:
```bash
javac -cp "$(lein classpath)" -d target/classes src/java/com/ultralight/UltralightApp.java
javah -cp target/classes -d src/native com.ultralight.UltralightApp
```

## Troubleshooting

### Library Loading Issues

If you get `UnsatisfiedLinkError`, ensure:
- The native library was built successfully
- The Ultralight SDK libraries are in the `native/` directory
- Your system can find the required dependencies

### Display Issues on Linux

If the window doesn't appear:
- Ensure X11 is running
- Check that OpenGL drivers are installed
- Try setting `DISPLAY` environment variable if using SSH

### Performance Issues

For better performance:
- Use hardware acceleration when available
- Minimize DOM updates
- Use CSS animations instead of JavaScript animations

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is released under the terms of the Ultralight license. See the Ultralight website for details.

## Related Projects

- [Ultralight SDK](https://ultralig.ht/) - The underlying rendering engine
- [Hiccup](https://github.com/weavejester/hiccup) - HTML generation library for Clojure
- [Sample 2 Basic App](../Sample%202%20-%20Basic%20App/) - The C++ example this project is based on

## Support

For issues related to:
- This Clojure wrapper: Open an issue in this repository
- Ultralight SDK: Check the [Ultralight documentation](https://docs.ultralig.ht/)
- Clojure: Visit [Clojure community resources](https://clojure.org/community/resources)