(defproject scouter "0.1-alpha"
  :description ""
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :source-paths ["src/clj"]
  :repl-options {:init-ns scouter.handler}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [ring/ring-defaults "0.2.1"]
                 [korma "0.4.3"]
                 [log4j "1.2.15" :exclusions [javax.mail/mail
                                              javax.jms/jms
                                              com.sun.jdmk/jmxtools
                                              com.sun.jmx/jmxri]]
                 [org.clojure/java.jdbc "0.3.6"]
                 [com.h2database/h2 "1.4.195"]
                 [org.clojure/clojurescript "1.9.542"]
                 [reagent "0.6.2"]
                 [hiccup "1.0.5"]
                 [ragtime "0.7.1"]
                 [ring/ring-json "0.4.0"]
                 [honeysql "0.9.0-beta2"]
                 [cljs-ajax "0.6.0"]
                 [secretary "1.2.3"]
                 [org.clojure/core.async "0.3.442"]
                 [reagent-utils "0.2.1"]
                 [cljsjs/d3 "4.3.0-5"]
                 [gyptis "0.2.2"]
                 [com.taoensso/encore "2.91.0"]]
  :plugins [[lein-ring "0.9.7"]
            [lein-cljsbuild "1.1.6"]]
  :ring {:handler scouter.handler/app}
  :cljsbuild {
    :builds [{:source-paths ["src/cljs"]
              :compiler {:output-to "resources/public/js/main.js"
                         :optimizations :whitespace
                         :pretty-print true}}]}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})
