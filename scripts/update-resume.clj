(ns update-resume
  (:require [clojure.pprint :refer [pprint]]
            [clojure.tools.cli :as cli]
            [clojure.java.io :as io])
  (:import [java.io File]))

(defn create-file [& paths]
  (apply io/file paths))

(def ^String user-dir (System/getProperty "user.dir"))

(def ^File new-resume (create-file "/Users/danp/Library/Mobile Documents/com~apple~CloudDocs/storage/cv-and-applications/DanielPlisetsky_resume.pdf"))

(def ^File current-resume-in-proj (create-file user-dir "content/pdf/resume.pdf"))

(defn copy-resume [new-resume current-resume]
  (io/copy (io/file new-resume)
           (io/file current-resume)))

;;; MAIN

(def cli-options
  [["-p" "--path PATH" "Path to resume"
    :default current-resume-in-proj]
   ["-h" "--help"]])

(defn -main []
  (let [args (cli/parse-opts *command-line-args* cli-options)]
    (cond
      (get-in args [:options :help])
      (println (:summary args))
      (seq (:arguments args))
      (copy-resume (first (:arguments args))
                   current-resume-in-proj)
      :else
      (copy-resume new-resume current-resume-in-proj))))

(-main)
