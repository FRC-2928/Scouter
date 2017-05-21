(ns scouter.ajax
  (:require
    [cljs.core.async :as async :refer [chan close! >!]]
    [ajax.core :as ajax])
  (:require-macros
    [cljs.core.async.macros :refer [go alt!]]))
