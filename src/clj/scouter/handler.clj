(ns scouter.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  (GET "/hello" [] "Hello world pt 2")
  (GET "/" [] "<head><script type=\"text/javascript\" src=\"js/main.js\"></script></head><body>Hello World<script>scouter.main.run();</script></body>")
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
