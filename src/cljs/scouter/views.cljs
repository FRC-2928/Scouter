(ns scouter.views
  (:require [scouter.components :as c]
            [reagent.core :as r]))

(defn team-list-page []
  (let [data (r/atom {})]
    (fn []
      [c/number-input data :teamNumber])))
