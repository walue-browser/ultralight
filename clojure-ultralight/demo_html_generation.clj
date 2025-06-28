#!/usr/bin/env lein exec

;; Demo script to show HTML generation capabilities
;; This can run without the native Ultralight SDK

(require '[clojure-ultralight.core :as ul]
         '[clojure.java.io :as io])

(defn save-html-demo
  "Generate and save HTML demos to files"
  []
  (println "Generating HTML demos...")
  
  ;; Create output directory
  (.mkdirs (io/file "demo-output"))
  
  ;; Generate basic demo HTML
  (let [basic-data (ul/example-data)
        basic-html (ul/create-html-from-data basic-data)]
    (spit "demo-output/basic-demo.html" basic-html)
    (println "Generated: demo-output/basic-demo.html"))
  
  ;; Generate task manager HTML
  (let [tasks (ul/example-tasks)
        task-html (ul/create-task-manager-html tasks)]
    (spit "demo-output/task-manager.html" task-html)
    (println "Generated: demo-output/task-manager.html"))
  
  ;; Generate custom data demo
  (let [custom-data {:title "Clojure Data Visualization"
                     :subtitle "Real-time dashboard powered by Clojure"
                     :message "This demonstrates how Clojure data structures can be transformed into beautiful web interfaces."
                     :items ["Live data binding"
                             "Functional programming paradigms"
                             "Immutable data structures"
                             "Reactive updates"
                             "Cross-platform deployment"]
                     :stats {"Active Users" 1247
                             "Data Points" 89432
                             "Uptime" "99.9%"
                             "Response Time" "12ms"
                             "Memory Usage" "45MB"
                             "CPU Usage" "8%"}}
        custom-html (ul/create-html-from-data custom-data)]
    (spit "demo-output/custom-demo.html" custom-html)
    (println "Generated: demo-output/custom-demo.html"))
  
  ;; Generate development tasks
  (let [dev-tasks [{:name "Set up Clojure development environment" :category "Setup" :completed true}
                   {:name "Learn Hiccup HTML generation" :category "Learning" :completed true}
                   {:name "Implement JNI bridge to Ultralight" :category "Development" :completed true}
                   {:name "Create data-driven UI components" :category "Development" :completed true}
                   {:name "Add real-time update capabilities" :category "Features" :completed false}
                   {:name "Implement JavaScript interop" :category "Features" :completed false}
                   {:name "Write comprehensive documentation" :category "Documentation" :completed false}
                   {:name "Create example applications" :category "Examples" :completed true}
                   {:name "Set up automated testing" :category "Testing" :completed true}
                   {:name "Package for distribution" :category "Release" :completed false}
                   {:name "Deploy to production" :category "Release" :completed false}]
        dev-html (ul/create-task-manager-html dev-tasks)]
    (spit "demo-output/development-tasks.html" dev-html)
    (println "Generated: demo-output/development-tasks.html"))
  
  (println "\nDemo HTML files generated successfully!")
  (println "Open the files in demo-output/ with a web browser to see the results.")
  (println "\nFiles generated:")
  (println "- basic-demo.html: Basic data visualization")
  (println "- task-manager.html: Task management interface")
  (println "- custom-demo.html: Custom dashboard example")
  (println "- development-tasks.html: Development progress tracker"))

(defn print-data-examples
  "Print examples of the data structures used"
  []
  (println "\n=== Clojure Data Structure Examples ===\n")
  
  (println "Basic Data Map:")
  (clojure.pprint/pprint (ul/example-data))
  
  (println "\nTask List:")
  (clojure.pprint/pprint (take 3 (ul/example-tasks)))
  
  (println "\nCustom Data Example:")
  (clojure.pprint/pprint {:title "My App"
                          :items ["Feature 1" "Feature 2"]
                          :stats {"Users" 100 "Uptime" "99%"}}))

(defn main []
  (println "Clojure Ultralight HTML Generation Demo")
  (println "======================================")
  
  (print-data-examples)
  (save-html-demo)
  
  (println "\nThis demo shows how Clojure data structures are transformed into HTML.")
  (println "The generated HTML files can be opened in any web browser.")
  (println "In a full Clojure Ultralight application, this HTML would be rendered")
  (println "in a native desktop window with GPU acceleration."))

;; Run the demo
(main)