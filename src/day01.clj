(ns day01
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.pprint :refer [pprint]]))

(defn calories
  []
  (with-open [rdr (io/reader (io/resource "day01-input.txt"))]
    (->> (line-seq rdr)
         (map edn/read-string)
         (partition-by nil?)
         (remove #(nil? (first %)))
         doall)))

(def answer1
  (->> (calories)
       (map #(reduce + 0 %))
       (reduce max 0)))

(def answer2
  (->> (calories)
       (map #(reduce + 0 %))
       (sort >)
       (take 3)
       (reduce + 0)))
