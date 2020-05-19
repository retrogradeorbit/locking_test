(ns locking-test.core
  (:require [sci.core :as sci])
  (:gen-class))

(def ^:dynamic *unrestricted* false)

(defprotocol IBox
  ;;(setVal [_this _v])
  (getVal [_this]))

(defprotocol HasName
  (getName [_]))

(defprotocol IVar
  (bindRoot [this v]))


(deftype TBox [thread ^:volatile-mutable val]
  IBox
  #_(setVal [this v]
    (set! val  v))
  (getVal [this] val))


(deftype Frame [bindings prev])

(def top-frame (Frame. {} nil))

(def ^ThreadLocal dvals (proxy [ThreadLocal] []
                          (initialValue [] top-frame)))

;; (defn get-thread-binding-frame ^Frame []
;;   (.get dvals))

;; (defn get-thread-bindings* []
;;   (let [f (get-thread-binding-frame)]
;;     (loop [ret {}
;;            kvs (seq (.-bindings f))]
;;       (if kvs
;;         (let [[var* ^TBox tbox] (first kvs)
;;               tbox-val (getVal tbox)]
;;           (recur (assoc ret var* tbox-val)
;;                  (next kvs)))
;;         ret))))

(defn get-thread-binding* ^TBox [sci-var]
  (when-let [^Frame f (.get dvals)]
    (when-let [bindings (.-bindings f)]
      (get bindings sci-var))))


(defmacro with-writeable-var
  [the-var var-meta & body]
  `(let [vm# ~var-meta]
     (if (or *unrestricted* (not (:sci.impl/built-in vm#)))
       (do ~@body)
       (let [the-var# ~the-var
             ns# (:ns vm#)
             ns-name# (getName ns#)
             name# (getName the-var#)]
         (throw (ex-info (str "Built-in var #'" ns-name# "/" name# " is read-only.")
                         {:var ~the-var}))))))

(deftype myvar [^:volatile-mutable root
                sym
                ^:volatile-mutable meta]
  HasName
  (getName [this]
    sym)

  IVar
  (bindRoot [this v]
    (with-writeable-var this meta
      (set! (.-root this) v)))

  IBox
  (getVal [this] root)

  clojure.lang.IDeref
  (deref [this]
    (or (when-let [tbox (get-thread-binding* this)]
          (getVal tbox))
        root))
  )

(defn alter-var-root* [v f & args]
  (locking v (bindRoot v (apply f @v args))))


(defn -main
  "locking test"
  [& args]
  (let [v (myvar. 0 'foo nil)]
    (alter-var-root* v inc)
    (println @v))

  (let [ctx {:namespaces {}
             :bindings {}
             :imports {}
             :classes {}}]
    (println
     (-> "(inc 1)"
         (sci/eval-string ctx)))))
