(ns scouter.db.db
  (:require [korma.db]
            [clojure.string :as str]))

(def db-map
  {:classname "org.h2.Driver"
   :user "scouter"
   :password "" ; Bad practice, but should never be publicly facing anyway
   :subprotocol "h2"
   :subname "resources/db/korma.db"
   :naming {:keys str/lower-case
            :fields str/upper-case}})

(defdb db db-map)
