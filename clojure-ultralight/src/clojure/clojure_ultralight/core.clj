(ns clojure-ultralight.core
  (:require [clojure.data.json :as json]
            [hiccup.core :as hiccup])
  (:import [com.ultralight UltralightApp])
  (:gen-class))

(defn create-html-from-data
  "Generate HTML content from Clojure data structures using Hiccup"
  [data]
  (hiccup/html
    [:html
     [:head
      [:meta {:charset "UTF-8"}]
      [:title (:title data "Clojure Ultralight App")]
      [:style {:type "text/css"}
       "* { 
          margin: 0; 
          padding: 0; 
          box-sizing: border-box;
          font-family: -apple-system, 'Segoe UI', 'Roboto', Arial, sans-serif;
        }
        body { 
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          height: 100vh;
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          color: white;
          text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
        }
        .container {
          background: rgba(255,255,255,0.1);
          padding: 40px;
          border-radius: 20px;
          backdrop-filter: blur(10px);
          border: 1px solid rgba(255,255,255,0.2);
          text-align: center;
          max-width: 600px;
          animation: fadeIn 1s ease-in;
        }
        @keyframes fadeIn {
          from { opacity: 0; transform: translateY(20px); }
          to { opacity: 1; transform: translateY(0); }
        }
        h1 { 
          font-size: 2.5rem; 
          margin-bottom: 20px;
          background: linear-gradient(45deg, #ff6b6b, #4ecdc4);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          background-clip: text;
        }
        .subtitle { 
          font-size: 1.2rem; 
          margin-bottom: 30px;
          opacity: 0.9;
        }
        .data-section {
          background: rgba(0,0,0,0.2);
          padding: 20px;
          border-radius: 10px;
          margin: 20px 0;
        }
        .data-item {
          margin: 10px 0;
          padding: 10px;
          background: rgba(255,255,255,0.1);
          border-radius: 5px;
          border-left: 4px solid #4ecdc4;
        }
        .button {
          background: linear-gradient(45deg, #ff6b6b, #4ecdc4);
          border: none;
          padding: 12px 24px;
          border-radius: 25px;
          color: white;
          font-weight: bold;
          cursor: pointer;
          margin: 10px;
          transition: transform 0.2s ease;
        }
        .button:hover {
          transform: translateY(-2px);
        }
        .stats {
          display: flex;
          justify-content: space-around;
          margin-top: 20px;
        }
        .stat {
          text-align: center;
        }
        .stat-value {
          font-size: 2rem;
          font-weight: bold;
          color: #4ecdc4;
        }
        .stat-label {
          font-size: 0.9rem;
          opacity: 0.8;
        }"]]
     [:body
      [:div.container
       [:h1 (:title data "Welcome to Clojure Ultralight")]
       [:div.subtitle (:subtitle data "Powered by Clojure and Ultralight SDK")]
       
       (when (:message data)
         [:div.data-section
          [:h3 "Message"]
          [:p (:message data)]])
       
       (when (:items data)
         [:div.data-section
          [:h3 "Data Items"]
          (for [item (:items data)]
            [:div.data-item (str item)])])
       
       (when (:stats data)
         [:div.stats
          (for [[label value] (:stats data)]
            [:div.stat
             [:div.stat-value (str value)]
             [:div.stat-label (str label)]])])
       
       [:div
        [:button.button {:onclick "updateContent()"} "Update Content"]
        [:button.button {:onclick "showTime()"} "Show Time"]
        [:button.button {:onclick "changeTheme()"} "Change Theme"]]]]
     
     [:script
      "function updateContent() {
         document.querySelector('h1').textContent = 'Content Updated from JavaScript!';
         document.querySelector('.subtitle').textContent = 'Updated at: ' + new Date().toLocaleTimeString();
       }
       
       function showTime() {
         alert('Current time: ' + new Date().toLocaleString());
       }
       
       function changeTheme() {
         const colors = [
           'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
           'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
           'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
           'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
           'linear-gradient(135deg, #fa709a 0%, #fee140 100%)'
         ];
         const randomColor = colors[Math.floor(Math.random() * colors.length)];
         document.body.style.background = randomColor;
       }
       
       // Auto-update time every second
       setInterval(function() {
         const timeElement = document.getElementById('current-time');
         if (timeElement) {
           timeElement.textContent = new Date().toLocaleTimeString();
         }
       }, 1000);"]]))

(defn create-task-manager-html
  "Create a task manager interface similar to Sample 2"
  [tasks]
  (hiccup/html
    [:html
     [:head
      [:meta {:charset "UTF-8"}]
      [:title "Clojure Task Manager"]
      [:style {:type "text/css"}
       "* { 
          margin: 0; 
          padding: 0; 
          box-sizing: border-box;
          font-family: -apple-system, 'Segoe UI', 'Roboto', Arial, sans-serif;
        }
        body { 
          background: linear-gradient(135deg, #4e95ff, #6032e4, #c478ff);
          height: 100vh;
          display: flex;
          justify-content: center;
          align-items: center;
          color: white;
        }
        .container {
          display: flex;
          width: 90%;
          max-width: 850px;
          height: 510px;
          gap: 20px;
        }
        #leftPane {
          color: white;
          padding: 40px;
          flex: 1;
          display: flex;
          flex-direction: column;
          justify-content: center;
        }
        #leftPane h2 {
          font-size: 2.5rem;
          margin-bottom: 8px;
          text-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        #leftPane p {
          color: rgba(255, 255, 255, 0.9);
          font-size: 1.2rem;
          margin-top: 0;
        }
        #leftPane .date {
          margin-top: auto;
          background: rgba(255, 255, 255, 0.15);
          align-self: flex-start;
          padding: 8px 16px;
          border-radius: 12px;
          backdrop-filter: blur(5px);
        }
        #rightPane {
          border-radius: 24px;
          background-color: white;
          flex: 1.2;
          color: #4361ee;
          box-shadow: 0 16px 40px -6px rgba(31, 40, 101, 0.25);
          padding: 35px;
          overflow: hidden;
          position: relative;
        }
        #rightPane h3 {
          font-size: 1.5rem;
          margin-bottom: 5px;
        }
        #rightPane > p {
          color: #9095a7;
          font-size: 0.95rem;
          margin-bottom: 30px;
        }
        .task-group {
          margin-bottom: 25px;
        }
        .task-group h5 {
          text-transform: uppercase;
          letter-spacing: 1px;
          color: #9095a7;
          font-size: 0.75rem;
          border-bottom: 1px solid #eceef0;
          padding-bottom: 10px;
          margin-bottom: 5px;
          font-weight: 600;
        }
        #rightPane ul {
          padding-left: 0;
          margin-top: 12px;
        }
        #rightPane li {
          list-style-type: none;
          padding: 3px 14px;
          border-radius: 12px;
          margin-bottom: 6px;
          color: #4a4d61;
          font-size: 0.85rem;
          font-weight: 500;
          cursor: pointer;
          transition: all 0.2s ease;
          display: flex;
          align-items: center;
        }
        #rightPane li:hover {
          background-color: #f5f7ff;
          color: #4361ee;
        }
        #rightPane li:before {
          content: '';
          display: inline-block;
          height: 18px;
          width: 18px;
          margin-right: 12px;
          flex-shrink: 0;
          border-radius: 50%;
          border: 1.5px solid #dbe2e7;
          transition: all 0.1s ease;
        }
        #rightPane li.checked {
          color: #9095a7;
        }
        #rightPane li.checked:before {
          background-color: #4361ee;
          border-color: #4361ee;
          background-image: url(\"data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='10' height='8' viewBox='0 0 14 10' fill='none'><path d='M1 5L5 9L13 1' stroke='white' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'/></svg>\");
          background-position: center;
          background-repeat: no-repeat;
        }"]]
     [:body
      [:div.container
       [:div#leftPane
        [:h2 "Good morning."]
        [:p "Clojure-powered task management"]
        [:div.date#current-date "Today"]]
       [:div#rightPane
        [:h3 "Tasks from Clojure"]
        [:p "Click a task to mark it as completed."]
        
        (for [[group-name group-tasks] (group-by :category tasks)]
          [:div.task-group
           [:h5 (or group-name "General")]
           [:ul
            (for [task group-tasks]
              [:li {:class (when (:completed task) "checked")
                    :onclick "this.classList.toggle('checked')"}
               (:name task)])]])]]
      
      [:script
       "// Display current date
        var now = new Date();
        var options = { weekday: 'long', month: 'long', day: 'numeric' };
        document.getElementById('current-date').textContent = now.toLocaleDateString('en-US', options);"]]]))

(defn create-app
  "Create and configure an Ultralight application"
  [width height title]
  (let [app (UltralightApp.)]
    (.initialize app width height title)
    app))

(defn load-data-as-html
  "Load Clojure data as HTML into the application"
  [app data]
  (let [html (create-html-from-data data)]
    (.loadHTMLContent app html)))

(defn load-tasks-as-html
  "Load tasks as HTML into the application"
  [app tasks]
  (let [html (create-task-manager-html tasks)]
    (.loadHTMLContent app html)))

(defn run-app
  "Run the application main loop"
  [app]
  (.run app))

(defn example-data
  "Generate example data for the application"
  []
  {:title "Clojure Ultralight Demo"
   :subtitle "Real-time data rendering with Clojure"
   :message "This content was generated from Clojure data structures!"
   :items ["Dynamic content generation"
           "Hiccup HTML templating"
           "JavaScript interoperability"
           "Real-time updates"]
   :stats {"FPS" 60
           "Memory" "45MB"
           "Uptime" "2m 30s"
           "Tasks" 12}})

(defn example-tasks
  "Generate example tasks for the task manager"
  []
  [{:name "Implement Clojure wrapper for Ultralight" :category "Development" :completed true}
   {:name "Create HTML generation from data" :category "Development" :completed true}
   {:name "Add JavaScript interop capabilities" :category "Development" :completed false}
   {:name "Write documentation" :category "Documentation" :completed false}
   {:name "Create example applications" :category "Examples" :completed true}
   {:name "Test cross-platform compatibility" :category "Testing" :completed false}
   {:name "Optimize performance" :category "Optimization" :completed false}
   {:name "Package for distribution" :category "Release" :completed false}])

(defn demo-basic-app
  "Run a basic demo application"
  []
  (println "Starting Clojure Ultralight Basic Demo...")
  (let [app (create-app 900 600 "Clojure Ultralight - Basic Demo")]
    (load-data-as-html app (example-data))
    (println "Application initialized. Close the window to exit.")
    (run-app app)
    (.destroy app)
    (println "Application closed.")))

(defn demo-task-manager
  "Run a task manager demo application"
  []
  (println "Starting Clojure Ultralight Task Manager Demo...")
  (let [app (create-app 900 600 "Clojure Ultralight - Task Manager")]
    (load-tasks-as-html app (example-tasks))
    (println "Task manager initialized. Close the window to exit.")
    (run-app app)
    (.destroy app)
    (println "Task manager closed.")))

(defn -main
  "Main entry point"
  [& args]
  (println "Clojure Ultralight SDK Demo")
  (println "Available demos:")
  (println "1. basic - Basic data rendering demo")
  (println "2. tasks - Task manager demo")
  (println)
  
  (let [demo-type (or (first args) "basic")]
    (case demo-type
      "basic" (demo-basic-app)
      "tasks" (demo-task-manager)
      (do
        (println "Unknown demo type:" demo-type)
        (println "Running basic demo instead...")
        (demo-basic-app)))))

;; Additional utility functions for advanced usage

(defn update-content
  "Update the content of a running application"
  [app new-data]
  (load-data-as-html app new-data))

(defn execute-js
  "Execute JavaScript in the application"
  [app script]
  (.executeScript app script))

(defn execute-js-with-result
  "Execute JavaScript and get the result"
  [app script]
  (.executeScriptWithResult app script))

(defn set-title
  "Set the window title"
  [app title]
  (.setTitle app title))

;; Example of real-time data updates
(defn start-real-time-demo
  "Start a demo with real-time updates"
  []
  (println "Starting real-time demo...")
  (let [app (create-app 900 600 "Clojure Ultralight - Real-time Demo")]
    (load-data-as-html app (example-data))
    
    ;; Start a background thread for updates
    (future
      (loop [counter 0]
        (Thread/sleep 2000)
        (let [updated-data (assoc (example-data)
                                  :message (str "Updated " counter " times!")
                                  :stats {"Counter" counter
                                          "Time" (.format (java.text.SimpleDateFormat. "HH:mm:ss") (java.util.Date.))
                                          "Memory" (str (+ 45 (rand-int 20)) "MB")
                                          "FPS" (+ 55 (rand-int 10))})]
          (update-content app updated-data)
          (recur (inc counter)))))
    
    (run-app app)
    (.destroy app)
    (println "Real-time demo closed.")))