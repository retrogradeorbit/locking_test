(ns locking-test.core
  (:gen-class))

(defmacro with-writeable-var
  [the-var var-meta & body]
  `(let [vm# ~var-meta]
     (if (not (:sci.impl/built-in vm#))
       (do ~@body)
       (let [the-var# ~the-var
             ns# (:ns vm#)
             ns-name# (getName ns#)
             name# (getName the-var#)]
         (throw (ex-info (str "Built-in var #'" ns-name# "/" name# " is read-only.")
                         {:var ~the-var}))))))

(defprotocol IVar
  (bindRoot [this v])
  (getName [this]
    (:name (meta this))))

(deftype SciVar [^:volatile-mutable root
                 ^:volatile-mutable meta]
  IVar
  (bindRoot [this v]
    (set! root v))
  clojure.lang.IDeref
  (deref [this] root)
  clojure.lang.IReference
  (alterMeta [this f args]
    (with-writeable-var this meta
      (locking (set! meta (apply f meta args)))))
  (resetMeta [this m]
    (locking (set! meta m))))

(defn alter-sci-var-root [v f & args]
  (locking v (bindRoot v (apply f @v args))))

(defn -main
  "locking test"
  [& _args]
  (let [var (->SciVar 1 {})]
    (alter-sci-var-root var #(+ 1 %))
    (alter-meta! var (constantly {:a 1}))
    (prn @var (meta var))))
