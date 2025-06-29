# Reactive Hiccup Demo for Ultralight

This project demonstrates how to create reactive HTML interfaces using Clojure's Hiccup library and integrate them with the Ultralight browser engine for real-time updates.

## Features

- Real-time editing of Clojure Hiccup code
- Live preview of generated HTML
- Reactive updates as you type
- Native performance with Ultralight browser engine
- Split-window interface with editor and preview

## How It Works

1. The left window contains a code editor with Clojure Hiccup syntax highlighting
2. As you edit the Hiccup code, it's evaluated in real-time
3. The generated HTML is sent to the preview window on the right
4. The preview window updates instantly to show your changes

## Building the Project

```bash
# Navigate to the project directory
cd reactive-hiccup-demo

# Run the build script
./build.sh
```

## Running the Demo

After building, you can run the demo with:

```bash
cd ../build/reactive-hiccup-demo
./ReactiveHiccupDemo
```

## Hiccup Syntax

Hiccup is a Clojure library for representing HTML as Clojure data structures. The basic syntax is:

```clojure
[:tag-name {:attribute "value"} "content"]
```

For example:

```clojure
[:div.container
 [:h1 "Hello World"]
 [:p "This is a paragraph"]]
```

Generates:

```html
<div class="container">
  <h1>Hello World</h1>
  <p>This is a paragraph</p>
</div>
```

## Project Structure

- `main.cpp` - C++ code for the Ultralight application
- `assets/editor.html` - HTML for the Hiccup editor
- `assets/preview.html` - HTML for the preview window
- `assets/hiccup_evaluator.clj` - Clojure code for evaluating Hiccup

## License

This project is licensed under the same terms as the Ultralight SDK.