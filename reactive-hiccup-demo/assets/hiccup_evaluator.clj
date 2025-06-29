(ns hiccup-evaluator
  (:require [hiccup.core :refer [html]]))

;; Function to evaluate Hiccup code and return HTML
(defn evaluate-hiccup [hiccup-code]
  (try
    (let [result (load-string hiccup-code)]
      {:success true
       :html result})
    (catch Exception e
      {:success false
       :error (.getMessage e)})))

;; Example reactive component
(defn reactive-component [data]
  [:div.container
   [:h1.title (:title data "Reactive Hiccup Demo")]
   [:div.subtitle (:subtitle data "Real-time updates with Clojure & Ultralight")]
   
   [:div.content
    [:p.message (:message data "This content is generated from Hiccup!")]
    
    [:div.counter
     [:h3 "Counter: " [:span.count (:count data 0)]]
     [:div.progress-bar
      [:div.progress {:style (str "width: " (mod (:count data 0) 101) "%")}]]]
    
    [:div.data-items
     [:h3 "Data Items:"]
     [:ul
      (for [item (:items data)]
        [:li.item item])]]
    
    [:div.stats
     [:h3 "Statistics:"]
     [:div.stats-grid
      (for [[key value] (:stats data)]
        [:div.stat-item
         [:div.stat-value value]
         [:div.stat-label key]])]]
    
    [:div.timestamp
     [:p "Last updated: " (:timestamp data "Never")]]]
   
   [:div.footer
    [:p "Powered by Clojure Hiccup & Ultralight"]]])

;; Sample data for the component
(def sample-data
  {:title "Reactive Hiccup Demo"
   :subtitle "Real-time updates with Clojure & Ultralight"
   :message "This content is generated from Hiccup and updates in real-time!"
   :count 42
   :items ["Functional UI generation"
           "Reactive data binding"
           "Real-time updates"
           "Native performance"
           "Cross-platform compatibility"]
   :stats {"Memory" "45MB"
           "CPU" "2%"
           "FPS" 60
           "Uptime" "5m 23s"}
   :timestamp "2025-06-29 12:34:56"})

;; Generate sample HTML
(html (reactive-component sample-data))