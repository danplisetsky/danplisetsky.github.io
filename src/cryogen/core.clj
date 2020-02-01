(ns cryogen.core
  (:require [cryogen-core.compiler :refer [compile-assets-timed]]
            [cryogen-core.plugins :refer [load-plugins]]
            [cryogen.read-data :refer [read-data]]))

(defn -main []
  (load-plugins)
  (compile-assets-timed {:data (read-data)})
  (System/exit 0))
