(ns simple-demo
  (:require [clojure-ultralight.core :as ul]))

(defn simple-demo
  "A simple demonstration of the Clojure Ultralight wrapper"
  []
  (println "Starting simple Clojure Ultralight demo...")
  
  ;; Create the application
  (let [app (ul/create-app 800 600 "Simple Clojure Demo")]
    
    ;; Define some data to display
    (let [demo-data {:title "Welcome to Clojure Ultralight!"
                     :subtitle "Building desktop apps with Clojure and web technologies"
                     :message "This application was created using Clojure data structures and rendered with the Ultralight SDK."
                     :items ["Fast native performance"
                             "Modern web technologies"
                             "Clojure data structures"
                             "Cross-platform compatibility"
                             "Real-time updates"]
                     :stats {"Language" "Clojure"
                             "Renderer" "Ultralight"
                             "Platform" "Cross-platform"
                             "Performance" "Native"}}]
      
      ;; Load the data as HTML
      (ul/load-data-as-html app demo-data)
      
      ;; Start a background thread for real-time updates
      (future
        (Thread/sleep 3000) ; Wait 3 seconds
        (println "Updating content...")
        
        ;; Update with new data
        (ul/update-content app (assoc demo-data
                                      :message "Content updated from Clojure!"
                                      :stats (assoc (:stats demo-data)
                                                    "Updates" "Real-time"
                                                    "Time" (.format (java.text.SimpleDateFormat. "HH:mm:ss")
                                                                     (java.util.Date.)))))
        
        ;; Execute some JavaScript
        (ul/execute-js app "document.body.style.animation = 'fadeIn 1s ease-in';")
        
        ;; Get information from JavaScript
        (let [title (ul/execute-js-with-result app "document.title")]
          (println "Current page title:" title)))
      
      ;; Run the application (this blocks until the window is closed)
      (println "Application window opened. Close it to exit.")
      (ul/run-app app)
      
      ;; Clean up
      (.destroy app)
      (println "Demo completed."))))

(defn task-demo
  "Demonstration of the task manager interface"
  []
  (println "Starting task manager demo...")
  
  (let [app (ul/create-app 900 600 "Clojure Task Manager")
        tasks [{:name "Learn Clojure basics" :category "Learning" :completed true}
               {:name "Set up development environment" :category "Setup" :completed true}
               {:name "Create Ultralight wrapper" :category "Development" :completed true}
               {:name "Implement HTML generation" :category "Development" :completed true}
               {:name "Add JavaScript interop" :category "Development" :completed false}
               {:name "Write comprehensive tests" :category "Testing" :completed false}
               {:name "Create documentation" :category "Documentation" :completed false}
               {:name "Package for distribution" :category "Release" :completed false}
               {:name "Celebrate success!" :category "Fun" :completed false}]]
    
    ;; Load the tasks
    (ul/load-tasks-as-html app tasks)
    
    ;; Run the application
    (println "Task manager opened. Click tasks to toggle completion.")
    (ul/run-app app)
    
    ;; Clean up
    (.destroy app)
    (println "Task manager demo completed.")))

(defn interactive-demo
  "Interactive demo with JavaScript communication"
  []
  (println "Starting interactive demo...")
  
  (let [app (ul/create-app 800 600 "Interactive Clojure Demo")]
    
    ;; Load initial content
    (ul/load-data-as-html app {:title "Interactive Demo"
                               :subtitle "Clojure ↔ JavaScript Communication"
                               :message "This demo shows bidirectional communication between Clojure and JavaScript."
                               :items ["Click buttons to see JavaScript in action"
                                       "Clojure updates content in real-time"
                                       "JavaScript can call back to the native layer"
                                       "Perfect for building interactive applications"]})
    
    ;; Start interactive updates
    (future
      (loop [counter 0]
        (Thread/sleep 5000) ; Update every 5 seconds
        
        ;; Update from Clojure
        (ul/execute-js app (str "document.querySelector('.subtitle').textContent = 'Updated from Clojure #" counter "';"))
        
        ;; Change background color
        (let [colors ["linear-gradient(135deg, #667eea 0%, #764ba2 100%)"
                      "linear-gradient(135deg, #f093fb 0%, #f5576c 100%)"
                      "linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)"
                      "linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)"]]
          (ul/execute-js app (str "document.body.style.background = '" 
                                  (nth colors (mod counter (count colors))) "';")))
        
        ;; Get current time from JavaScript
        (let [js-time (ul/execute-js-with-result app "new Date().toLocaleTimeString()")]
          (println "JavaScript reports time as:" js-time))
        
        (recur (inc counter))))
    
    ;; Run the application
    (println "Interactive demo running. Watch for automatic updates!")
    (ul/run-app app)
    
    ;; Clean up
    (.destroy app)
    (println "Interactive demo completed.")))

;; Main function to run demos
(defn -main [& args]
  (let [demo-type (or (first args) "simple")]
    (case demo-type
      "simple" (simple-demo)
      "tasks" (task-demo)
      "interactive" (interactive-demo)
      (do
        (println "Available demos:")
        (println "  simple      - Basic data rendering")
        (println "  tasks       - Task manager interface")
        (println "  interactive - JavaScript communication")
        (simple-demo)))))

;; For REPL usage
(comment
  ;; Run different demos from the REPL
  (simple-demo)
  (task-demo)
  (interactive-demo)
  
  ;; Create a custom app
  (let [app (ul/create-app 600 400 "Custom App")]
    (ul/load-data-as-html app {:title "Custom" :message "Hello from REPL!"})
    (ul/run-app app)
    (.destroy app)))