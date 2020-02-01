(ns cryogen.read-data
  (:require [clojure.string :as string]
            [cryogen-core.compiler :as cryogen-compiler]
            [cryogen-core.io :as cryogen-io]))

(def data-root "data")

(defn ^:private find-data
  "Returns a list of all .edn files found under the content/data directory"
  [data-dir]
  (let [path-to-data (cryogen-io/path cryogen-compiler/content-root data-dir)]
    (cryogen-io/find-assets path-to-data ".edn" [])))

(defn ^:private file->name
  "Takes a (.edn) java.io.File and return its name without the extension"
  [file]
  (-> file
      (.getName)
      (string/replace ".edn" "")))

(defn ^:pricate file->content
  "Take an .edn java.io.File and returns its content"
  [file]
  (-> file
      (.getAbsolutePath)
      (cryogen-io/read-edn-resource)))

(defn read-data
  "Returns a seq of maps representing the data from .edn data files"
  []
  (->> (find-data data-root)
       (pmap (fn [file]
               (hash-map (-> file file->name keyword)       ; get file name and use it as keyword
                         (file->content file))))
       (apply merge)))