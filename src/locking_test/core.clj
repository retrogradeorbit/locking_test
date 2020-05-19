(ns locking-test.core
  (:gen-class))

(def o (Object.))

(defn -main
  "locking test"
  [& args]
  (locking o
    (println "Locking test passed!")))
