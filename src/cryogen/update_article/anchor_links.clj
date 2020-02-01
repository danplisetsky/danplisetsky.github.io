(ns cryogen.update-article.anchor-links)

(defn ^:private create-link-icon
  []
  {:tag :i,
   :attrs {:class "fas fa-link"}})

(defn ^:private create-anchor-link
  [href]
  {:tag :a,
   :attrs {:href href}
   :content (list (create-link-icon))})

(defn ^:private header-tag->header-tag-with-link
  "Takes a map representing an html tag.
  Example: {:tag :p, :attrs nil, :content (\"Hi\").
  Returns
    - unmodified tag if :tag is not a header
    - a modified header tag with a link tag added to the :content key"
  [tag]
  (if (some #{(:tag tag)} [:h3 :h4 :h5 :h6])
    (let [id (get-in tag [:attrs :id])
          content (:content tag)
          new-content (concat content
                              (list (create-anchor-link (str "#" id))))]
      (assoc tag :content new-content))
    tag))

(defn add-self-links
  "Takes a content-dom (a seq of maps).
  For each h3 through h6, adds a self-referential anchor link.
  Returns updated content-dom"
  [content-dom]
  (->> content-dom
       (map header-tag->header-tag-with-link)))
