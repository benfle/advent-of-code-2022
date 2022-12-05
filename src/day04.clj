(ns day04
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.string :as s]
            [clojure.pprint :refer [pprint]]))

(defn assignments
  []
  (with-open [rdr (io/reader (io/resource "day04-input.txt"))]
    (->> (line-seq rdr)
         (map #(map (fn [range]
                      (->> range
                           (re-find #"(\d+)-(\d+)")
                           (rest)
                           (map edn/read-string)))
                    (s/split % #",")))
         (doall))))

(defn fully-contains?
  [[s1 e1] [s2 e2]]
  (or (<= s1 s2 e2 e1)
      (<= s2 s1 e1 e2)))

(def answer1
  (->> (assignments)
       (filter #(apply fully-contains? %))
       count))

(defn overlap?
  [[s1 e1] [s2 e2]]
  (not
   (or (< e1 s2)
       (< e2 s1))))

(def answer2
  (->> (assignments)
       (filter #(apply overlap? %))
       count))
