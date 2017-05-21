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
(defn getTeamMatch [number match]
  (j/query db-settings (->  (select :*)
                            (from :teammatches)
                            (where [:and [:= :number number] [:= :matchnumber match]])
                            sql/format
                            )))

(defn getTeamStats [number]
  (j/query db-settings (->  (select :matchnumber :autogears :linecrossed :shots)
                            (from :teammatches)
                            (where [:= :team (-> (select :id)
                                                 (from :teams)
                                                 (where [:= :number number]))])
                            sql/format)))

(defn getShots []
  (j/query db-settings (-> (select :m.shots :t.number)
                           (from [:teammatches :m] [:teams :t])
                           (join [:= :m.team :t.id])
                           sql/format)))
(defn query [q]
  (j/query db-settings (-> (select :*) (from q) sql/format))
  )
(defn getTeams []
  (j/query db-settings (-> (select :*)
                          (from :teams)
                          sql/format)))

(defn addMatch [params-map]
  (println params-map)
  (if (not-empty (j/query db-settings (-> (select :*)
                               (from :matches)
                               (where [:= :matchnumber (params-map :matchnumber)])
                               sql/format)))
    (j/execute! db-settings (-> (update :matches)
                                (sset {:red1 (->  (select :id)
                                                  (from :teams)
                                                  (where [:= :number (str (get-in params-map [:red1 :number]))]))})
                                sql/format))
    (j/execute! db-settings (-> (insert-into :matches)
                                (columns :matchnumber :red1)
                                (values [[(:matchnumber params-map) (-> (select :id)
                                                                        (from :teams)
                                                                        (where [:= :number (str (get-in params-map [:red1 :number]))]))]])
                                sql/format)))
  (if (not-empty (j/query db-settings (-> (select :*)
                               (from :teammatches)
                               (where [:and [:= :matchnumber (:matchnumber params-map)]
                                            [:= :team (-> (select :id)
                                                          (from :teams)
                                                          (where [:= :number (str (get-in params-map [:red1 :number]))]))]])
                               sql/format)))
    (j/execute! db-settings (-> (update :teammatches)
                                (sset {:linecrossed (get-in params-map [:red1 :line])
                                       :shots (get-in params-map [:red1 :shots])})
                                (where [:and [:= :matchnumber (int (:matchnumber params-map))]
                                             [:= :team (-> (select :id)
                                                          (from :teams)
                                                          (where [:= :number (str (get-in params-map [:red1 :number]))]))]])
                                sql/format))
    (j/execute! db-settings (-> (insert-into :teammatches)
                                (values [{:matchnumber (int (:matchnumber params-map))
                                          :team (-> (select :id)
                                                    (from :teams)
                                                    (where [:= :number (str (get-in params-map [:red1 :number]))]))
                                          :linecrossed (get-in params-map [:red1 :line])
                                          :shots (get-in params-map [:red1 :shots])}])
                                sql/format))))
