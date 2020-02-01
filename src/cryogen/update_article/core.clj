(ns cryogen.update-article.core
  (:require [cryogen.update-article.anchor-links :refer [add-self-links]]))

(defn update-article-fn
  [article _]
  (update article :content-dom add-self-links))
