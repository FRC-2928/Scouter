(ns scouter.db.setup
  (require [ragtime.jdbc :as jdbc]
           [scouter.db.db :as db]))

(def config
  {:datastore (jdbc/sql-database {:connection-uri "jdbc:h2:./resources/db/korma.db"})
  :migrations (jdbc/load-resources "migrations")})
