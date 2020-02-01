(ns cryogen.core
  (:require [cryogen-core.compiler :refer [compile-assets-timed]]
            [cryogen-core.plugins :refer [load-plugins]]
            [cryogen.update-article.core :refer [update-article-fn]]
            [cryogen.read-data :refer [read-data]]))

(defn -main []
  (load-plugins)
  (compile-assets-timed {:data (read-data)
                         :update-article-fn update-article-fn})
  (System/exit 0))
