(ns locking-test.core
  (:require [sci.core :as sci])
  (:gen-class))

(defn -main
  "locking test"
  [& args]
  (let [ctx {:namespaces {}
             :bindings {}
             :imports {}
             :classes {}}]
    (-> "(inc 1)"
        (sci/eval-string ctx)
        println)))
