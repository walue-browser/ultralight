(defproject clojure-ultralight "1.0.0"
  :description "Clojure wrapper for Ultralight SDK - HTML rendering engine"
  :url "https://github.com/walue-browser/ultralight"
  :license {:name "Ultralight License"
            :url "https://ultralig.ht/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/data.json "2.4.0"]
                 [cheshire "5.11.0"]
                 [hiccup "1.0.5"]]
  :java-source-paths ["src/java"]
  :source-paths ["src/clojure"]
  :resource-paths ["resources"]
  :jvm-opts ["-Djava.library.path=./native"]
  :main clojure-ultralight.core
  :aot [clojure-ultralight.core])