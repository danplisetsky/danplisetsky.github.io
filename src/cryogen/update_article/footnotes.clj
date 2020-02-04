(ns cryogen.update-article.footnotes
  (:require [clojure.edn :as edn]
            [cryogen.update-article.util :refer [create-anchor-link]]))

(defn ^:private tag-contains-sup?
  "Checks whether a given tag contains a sup tag in its :content
  with nothing but a number in it (in sup's content).
  Only checks one level deep.
  Returns a seq of :sup tags if found, empty seq otherwise."
  [{content :content}]
  (filter (fn [{:keys [tag content]}]
            (and (= tag :sup)
                 (= (count content) 1)
                 (-> content
                     first
                     edn/read-string
                     number?)))
          content))

(defn ^:private sup->sup-with-link
  [{:keys [content attrs] :as sup}]
  (let [number (-> content first)
        id (str "footnoteref" (keyword number))
        new-attrs (assoc attrs :id id)
        new-content (list (create-anchor-link (str "#footnote" (keyword number))
                                              (list number)))]
    (assoc sup :attrs new-attrs :content new-content)))

(defn ^:private tag-with-sup->tag-with-sup-with-link
  "Take a map representing an html tag.

  Returns
    - unmodified tag if it doesn't contain a sup tag with
    nothing but a number in it
    - a modified sup tag with id and link attached"
  [tag]
  (if-some [sups (seq (tag-contains-sup? tag))]
    (let [sups-with-link (->> sups
                              (mapv #(array-map % (sup->sup-with-link %)))
                              (into {}))
          content (:content tag)
          new-content (replace sups-with-link content)]
      (assoc tag :content new-content))
    tag))


(defn add-footnote-links
  [content-dom]
  (->> content-dom
       (map tag-with-sup->tag-with-sup-with-link)))
