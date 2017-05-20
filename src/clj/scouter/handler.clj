(ns scouter.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  (GET "/hello" [] "Hello world pt 2")
  (GET "/" [] "<body>Hello World</body>")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
