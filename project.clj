(defproject cljs-pong "0.1.0-SNAPSHOT"
  :description "Pong"
  :url "https://github.com/ZachMassia/cljs-pong"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2227"]
                 [com.cemerick/piggieback "0.1.3"]
                 [weasel "0.2.0"]
                 [rm-hull/monet "0.1.10"]
                 [rm-hull/big-bang "0.0.1-SNAPSHOT"]
                 [prismatic/dommy "0.1.2"]]

  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-simpleton "1.3.0"]]

  :source-paths ["src"]

  ;; Weasel setup --------------------------------------------------------------
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

  :injections [(require '[cemerick.piggieback :refer [cljs-repl]]
                        '[weasel.repl.websocket :refer [repl-env]])
               (defn browser-repl []
                 (cljs-repl :repl-env (repl-env :ip "0.0.0.0" :port 9001)))]
  ;; ===========================================================================

  :aliases {"server" ["trampoline" "simpleton" "8080" "file" ":from" "resources"]}
  
  :cljsbuild { 
    :builds [{:id "cljs-pong"
              :source-paths ["src"]
              :compiler {
                :output-to "resources/js/cljs_pong.js"
                :output-dir "resources/js/out"
                :optimizations :none
                :source-map true}}]})
