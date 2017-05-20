(ns scouter.main
  (:require [reagent.core :as r]))

(defn test-component [message]
  [:h1 message])

(defn ^:export run []
  (r/render [test-component "hello"] 
            (.-body js/document)))
