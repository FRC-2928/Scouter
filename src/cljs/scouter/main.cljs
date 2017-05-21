(ns scouter.main
  (:require [reagent.core :as r]
            [scouter.views :as views]
            [secretary.core :as secretary :include-macros true]
            [reagent.session :as session]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType])
  (:import goog.History))

(def state (r/atom {:score 0 :team "xxxx"}))

(defn page []
  [(session/get :current-page)])

(defn add-routes! []
  (secretary/set-config! :prefix "#")
  (secretary/defroute "/" []
    (session/put! :current-page views/team-list-page))
  (doto (History.)
        (events/listen
          HistoryEventType/NAVIGATE
          (fn [event]
              (secretary/dispatch! (.-token event))))
        (.setEnabled true)))

(defn mount-components []
  (r/render [#'page] (.getElementById js/document "app")))

(defn ^:export init []
  (add-routes!)
  (mount-components))
