(defproject diplomacy "0.1.0-SNAPSHOT"
  :description "Run diplomacy games!"
  :url "http://www.github.com/RadicalZephyr/diplomacy"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.5"]
                 [ring-server "0.3.1"]
                 [seesaw "1.4.4"]]
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler diplomacy.handler/app
         :init diplomacy.handler/init
         :destroy diplomacy.handler/destroy}
  :profiles
  {:uberjar {:aot :all}
   :production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.3.1"]]}})
