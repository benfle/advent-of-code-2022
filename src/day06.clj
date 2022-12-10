(ns day06
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.string :as s]
            [clojure.pprint :refer [pprint]]))

(defn datastream
  []
  (with-open [rdr (io/reader (io/resource "day06-input.txt"))]
    (->> (line-seq rdr)
         first)))

(defn n-chars
  [n datastream]
  (lazy-seq
   (when (<= n (count datastream))
     (cons (take n datastream)
           (n-chars n (rest datastream))))))

(def answer1
  (->> (datastream)
       (n-chars 4)
       (reduce (fn [n last4]
                 (if (apply distinct? last4)
                   (reduced (+ n 4))
                   (inc n)))
               0)))

(def answer2
  (->> (datastream)
       (n-chars 14)
       (reduce (fn [n last14]
                 (if (apply distinct? last14)
                   (reduced (+ n 14))
                   (inc n)))
               0)))
