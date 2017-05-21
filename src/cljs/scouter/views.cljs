(ns scouter.views
  (:require [scouter.components :as c]
            [reagent.core :as r]
            [ajax.core :as ajax]
            [secretary.core :as s :include-macros true]
            [taoensso.encore :refer [reset-in!]]
            [cljs.core.async :as async :refer [<!]]
            [clojure.walk :refer [keywordize-keys]]))

(def state (r/atom {:teams [] :match {:matchnumber 1 :red1 {:shots 0 :line false}} :chart-data []}))
(defn team-refresh []
  (ajax/GET "/teams" {:format :json :handler (fn [res] (swap! state assoc :teams (keywordize-keys res)))}))

(defn chart-refresh []
  (ajax/GET "/shots" {:format :json :handler (fn [res] (swap! state assoc :chart-data (keywordize-keys res)))}))

(chart-refresh)
(team-refresh)
(defn team-info-component [{number :number team-name :name primary :primary_color secondary :secondary_color} team]
  [:div.teaminfo team-name ": " [:span {:style {:color primary :outline-color secondary :outline-width "1px" :outline-style "solid"}} number]])

(defn team-list-page []
  (let [teams ((js->clj @state) :teams)]
    [:div
      [:a {:on-click #(s/dispatch! "/match")} "match entry"]
      (for [team teams] [team-info-component team])]))

(defn team-selector-component [data]
  [:div.team-selector-component {:style {:display "flex"}}
   (for [team ["red1" "red2" "red3" "blue1" "blue2" "blue3"]]
     [:div [:p team]
     [c/text-input data [:match (keyword team) :number]]])])

(def chart-config
  {:chart {:type "bar"}
   :title {:text "Shots made by team"}
   :xAxis {:categories ["Shots"]
           :title {:text nil}}
   :yAxis {:min 0
           :title {:text "Shots"
                   :align "high"}
           :labels {:overflow "justify"}}
   :plotOptions {:bar {:dataLabels {:enabled true}}}
   :legend {:layout "vertical"
            :align "right"
            :verticalAlign "top"
            :x -40
            :y 100
            :floating true
            :borderWidth 1
            :shadow true}
   :series []
   })
(defn match-input-page []
 [:div
  [:p "Match"]
  [c/text-input state [:match :matchnumber]]
  [team-selector-component state]
  [:p "Auto shots made"]
  [c/score-component state [-10 -5 -1] [1 5 10] [:match :red1 :shots]]
  [:p "Auto line crossed"]
  [c/boolean-input state [:match :red1 :line]]
  [:p {:on-click (fn [] (ajax/POST "/match" {:format :json :params (@state :match)}) (s/dispatch! "/"))} "Send Match"]])

(defn graph-display []
  [:div {:style {:min-width "310px" :max-width "800px" 
                 :height "400px" :margin "0 auto"}}])

(defn graph-did-mount [this]
  (js/Highcharts.Chart. (r/dom-node this) (clj->js chart-config)) )
(defn ^:export datadump []
  (enable-console-print!)
  (println @state))
