(ns cryogen.update-article.core
  (:require [cryogen.update-article.anchor-links :refer [add-self-links]]
            [cryogen.update-article.footnotes :refer [add-footnote-links]]))

(defn update-article-fn
  [article _]
  (-> article
      (update :content-dom add-self-links)
      (update :content-dom add-footnote-links)))
