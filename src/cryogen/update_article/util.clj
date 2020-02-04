(ns cryogen.update-article.util)

(defn create-anchor-link
  "Takes an href and a seq (content)"
  [^String href content]
  {:tag     :a,
   :attrs   {:href href}
   :content content})
