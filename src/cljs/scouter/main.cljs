(ns scouter.main
  (:require [reagent.core :as r]))
  
(def state (r/atom {:score 0 :team "xxxx" }))
  
(defn test-component [message]
  [:h1 message])
 
(defn button [value change key]
	[:button {:on-click #(swap! value (fn [v] (assoc-in v [key] (+ (js/Number change) (js/Number (:score v))))))}
	change])
	
(defn number-input [value key]
  [:input {:type "number"
           :value (key @value)
           :on-change #(reset! value (assoc-in @value [key] (-> % .-target .-value)))}])
(defn text-input [value key]
  [:input {:type "text"
           :value (key @value)
           :on-change #(reset! value (assoc-in @value [key] (-> % .-target .-value)))}])

(defn score-component [value before after key]
	[:div.numberinput
		(map #(button value % key) before)
		[number-input value key]
		(map #(button value % key) after)])
	

(defn page-component []
	[
	[text-input state name]
	])		   
(defn ^:export run []
  (r/render [score-component state [] [1 5] :score]
            (.-body js/document)))

			
			