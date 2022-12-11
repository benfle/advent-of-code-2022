(ns day09
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.zip :as zip]
            [clojure.pprint :refer [pprint]]))

(defn motions
  "Seq of motions."
  []
  (with-open [rdr (io/reader (io/resource "day09-input.txt"))]
    (->> (line-seq rdr)
         (map #(let [[_ direction magnitude] (re-find #"(.) (\d+)" %)]
                 [(first direction) (edn/read-string magnitude)]))
         (doall))))

(def delta
  {\U [ 0  1]
   \D [ 0 -1]
   \L [-1  0]
   \R [ 1  0]})

(defn head-positions
  "Seq of motions -> Seq of positions of the head of the rope (starting at [0 0])"
  [motions]
  (->> motions
       (mapcat (fn [[direction magnitude]]
                 (repeat magnitude direction)))
       (reductions (fn [pos direction]
                     (map + pos (delta direction)))
                   [0 0])))

(defn sign
  [n]
  (cond
    (< n 0) -1
    (= n 0)  0
    (< 0 n)  1))

(defn follow
  [[dx dy]]
  (if (and (<= -1 dx 1)
           (<= -1 dy 1))
    [0 0]
    [(sign dx) (sign dy)]))

(defn tail-positions
  "Seq of head positions -> Seq of tail positions"
  [positions]
  (reductions (fn [tail head]
                (map + tail (follow (map - head tail))))
              [0 0]
              positions))

(def answer1
  (->> (motions)
       (head-positions)
       (tail-positions)
       (set)
       (count)))

(def answer2
  (->> (motions)
       (head-positions)
       (iterate tail-positions)
       (drop 9)
       (first)
       (set)
       (count)))
