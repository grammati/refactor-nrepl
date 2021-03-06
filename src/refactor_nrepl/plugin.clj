(ns refactor-nrepl.plugin
  (:require [clojure.java.io :as io]))

(defn version []
  (let [v (-> (or (io/resource "refactor-nrepl/refactor-nrepl/project.clj")
                  "project.clj")
              slurp
              read-string
              (nth 2))]
    (assert (string? v)
            (str "Something went wrong, version is not a string: "
                 v))
    v))

(defn middleware [project]
  (-> project
      (update-in [:dependencies]
                 (fnil into [])
                 [['refactor-nrepl (version)]])
      (update-in [:repl-options :nrepl-middleware]
                 (fnil into [])
                 '[refactor-nrepl.middleware/wrap-refactor])))
