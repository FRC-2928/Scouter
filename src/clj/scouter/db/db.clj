(ns scouter.db.db
  (:require [clojure.java.jdbc :as j]
            [honeysql.core :as sql]
            [honeysql.helpers :refer :all]))

(def db-settings
  {:classname "org.h2.Driver"
   :subprotocol "h2:file"
   :subname "./resources/db/database.db"
   :user "sa"
   :password ""})

;; Ragtime isn't working and I'm too lazy to make it work
(defn migrate [file]
  (j/execute! db-settings [(slurp file)]))

(defn createTeam [number team-name primary secondary]
  (j/execute! db-settings (-> (insert-into :teams)
                              (columns :number :name :primary_color :secondary_color)
                              (values [[number team-name primary secondary]])
                              sql/format)))
(defn createTeamFromMap [team-map]
  (apply createTeam (map team-map ["number" "name" "primary" "secondary"])))

(defn getTeam [number]
  (j/query db-settings (->  (select :*)
                            (from :teams)
                            (where [:= :number number])
                            sql/format)))
