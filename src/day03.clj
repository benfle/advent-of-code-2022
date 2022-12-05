(ns day03
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.string :as s]
            [clojure.set :as set]
            [clojure.pprint :refer [pprint]]))

(defn rucksacks
  []
  (with-open [rdr (io/reader (io/resource "day03-input.txt"))]
    (->> (line-seq rdr)
         (doall))))

(def priority
  (zipmap (concat (map char (range 97 123))
                  (map char (range 65 91)))
          (map inc (range))))

(def answer1
  (->> (rucksacks)
       (map #(split-at (/ (count %) 2) %))
       (map (fn [[c1 c2]]
              (-> (set/intersection (set c1) (set c2))
                  first
                  priority)))
       (reduce + 0)))

(def answer2
  (->> (rucksacks)
       (partition 3)
       (map #(apply set/intersection (map set %)))
       (map #(priority (first %)))
       (reduce + 0)))
