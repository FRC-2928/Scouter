(ns scouter.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :as json]
            [ring.util.response :refer [response]]
            [hiccup.core :as h]
            [scouter.db.db :as db]))

(def main-page (h/html
                 [:head [:title "Scouter"]
                        [:script {:src "js/main.js"}]]
                 [:body [:div {:id "app"} "Please enable javascript!"]
                  [:script "scouter.main.init();"]]))

(defroutes app-routes
  (GET "/" [] main-page)
  (GET "/team" request (response (db/getTeam (get-in request [:body "number"]))))
  (POST "/team" request (response (db/createTeamFromMap (get-in request [:body]))))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (json/wrap-json-body)
      (json/wrap-json-response)
      (wrap-defaults api-defaults)))
