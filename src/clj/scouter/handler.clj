(ns scouter.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :as json]
            [ring.util.response :refer [response]]
            [hiccup.core :as h]
            [scouter.db.db :as db]
            [clojure.walk :refer [keywordize-keys]]))

(def main-page (h/html
                 [:head [:title "Scouter"]
                        [:script {:src "js/main.js"}]
                        [:script {:src "http://code.highcharts.com/highcharts.js"}]
                        [:script {:src "http://code.highcharts.com/modules/exporting.js"}]]
                 [:body [:div {:id "app"} "Please enable javascript!"]
                  [:script "scouter.main.init();"]
                  [:script "highcharts.core.main();"]]))

(defroutes app-routes
  (GET "/" [] main-page)
  (GET "/team" request (do (println request) (response (db/getTeam (get-in request [:query-params "number"])))))
  (POST "/team" request (response (db/createTeamFromMap (get-in request [:query-params]))))
  (GET "/teams" request (response (db/getTeams)))
  (GET "/shots" request (response (db/getShots)))
  (POST "/match" request (db/addMatch (keywordize-keys (:body request))))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (json/wrap-json-body)
      (json/wrap-json-response)
      (wrap-defaults api-defaults)))
