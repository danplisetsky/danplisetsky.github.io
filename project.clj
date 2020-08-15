(defproject cryogen "0.1.0"
            :description "Simple static site generator"
            :url "https://github.com/lacarmen/cryogen"
            :license {:name "Eclipse Public License"
                      :url "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.10.0"]
                           [ring/ring-devel "1.7.1"]
                           [compojure "1.6.1"]
                           [ring-server "0.5.0"]
                           [cryogen-markdown "0.1.11" :exclusions [cryogen-core]]
                           [org.clojure/tools.cli "1.0.194"]
                           [org.clojars.danplisetsky/cryogen-core "0.3.1-SNAPSHOT"]]
            :plugins [[lein-ring "0.12.5"]]
            :main cryogen.core
            :ring {:init cryogen.server/init
                   :handler cryogen.server/handler})
