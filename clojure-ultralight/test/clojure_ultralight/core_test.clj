(ns clojure-ultralight.core-test
  (:require [clojure.test :refer :all]
            [clojure-ultralight.core :as ul]
            [hiccup.core :as hiccup]))

(deftest test-html-generation
  (testing "HTML generation from data structures"
    (let [data {:title "Test Title"
                :subtitle "Test Subtitle"
                :message "Test Message"
                :items ["Item 1" "Item 2"]
                :stats {"Key1" "Value1" "Key2" "Value2"}}
          html (ul/create-html-from-data data)]
      
      (is (string? html))
      (is (.contains html "Test Title"))
      (is (.contains html "Test Subtitle"))
      (is (.contains html "Test Message"))
      (is (.contains html "Item 1"))
      (is (.contains html "Item 2"))
      (is (.contains html "Key1"))
      (is (.contains html "Value1")))))

(deftest test-task-html-generation
  (testing "Task manager HTML generation"
    (let [tasks [{:name "Task 1" :category "Category A" :completed true}
                 {:name "Task 2" :category "Category B" :completed false}]
          html (ul/create-task-manager-html tasks)]
      
      (is (string? html))
      (is (.contains html "Task 1"))
      (is (.contains html "Task 2"))
      (is (.contains html "Category A"))
      (is (.contains html "Category B"))
      (is (.contains html "checked")))))

(deftest test-example-data
  (testing "Example data generation"
    (let [data (ul/example-data)]
      (is (map? data))
      (is (contains? data :title))
      (is (contains? data :subtitle))
      (is (contains? data :message))
      (is (contains? data :items))
      (is (contains? data :stats))
      (is (vector? (:items data)))
      (is (map? (:stats data))))))

(deftest test-example-tasks
  (testing "Example tasks generation"
    (let [tasks (ul/example-tasks)]
      (is (vector? tasks))
      (is (> (count tasks) 0))
      (is (every? map? tasks))
      (is (every? #(contains? % :name) tasks))
      (is (every? #(contains? % :category) tasks))
      (is (every? #(contains? % :completed) tasks)))))

;; Note: We can't easily test the native JNI parts without actually
;; initializing the Ultralight SDK, which requires a display.
;; These tests focus on the pure Clojure functions.

(deftest test-hiccup-integration
  (testing "Hiccup HTML generation works correctly"
    (let [hiccup-html (hiccup/html [:div [:h1 "Test"] [:p "Content"]])]
      (is (string? hiccup-html))
      (is (.contains hiccup-html "<div>"))
      (is (.contains hiccup-html "<h1>Test</h1>"))
      (is (.contains hiccup-html "<p>Content</p>")))))

(deftest test-data-validation
  (testing "Data structure validation"
    (let [valid-data {:title "Valid" :items ["a" "b"] :stats {"x" 1}}
          html (ul/create-html-from-data valid-data)]
      (is (string? html))
      (is (.contains html "Valid")))
    
    (let [minimal-data {:title "Minimal"}
          html (ul/create-html-from-data minimal-data)]
      (is (string? html))
      (is (.contains html "Minimal")))
    
    (let [empty-data {}
          html (ul/create-html-from-data empty-data)]
      (is (string? html))
      ;; Should still generate valid HTML even with empty data
      (is (.contains html "<html>")))))

(deftest test-task-validation
  (testing "Task data validation"
    (let [valid-tasks [{:name "Test Task" :category "Test" :completed false}]
          html (ul/create-task-manager-html valid-tasks)]
      (is (string? html))
      (is (.contains html "Test Task")))
    
    (let [empty-tasks []
          html (ul/create-task-manager-html empty-tasks)]
      (is (string? html))
      ;; Should still generate valid HTML even with no tasks
      (is (.contains html "<html>")))))

(run-tests)