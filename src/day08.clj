(ns day08
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.zip :as zip]
            [clojure.pprint :refer [pprint]]))

(defn read-map
  []
  (with-open [rdr (io/reader (io/resource "day08-input.txt"))]
    (->> (line-seq rdr)
         (mapv #(mapv (fn [c] (Character/digit c 10)) %))
         (doall))))

(def visible?
  (let [m (read-map)
        transpose #(apply mapv vector %)
        build-tallest-map #(reduce (fn [tallest-m row]
                                     (conj tallest-m (mapv max (last tallest-m) row)))
                                   [(vec (repeat (count (first m)) -1))]
                                   (butlast %))
        tallest-maps {:top    (build-tallest-map m)
                      :bottom (vec (reverse (build-tallest-map (reverse m))))
                      :left   (transpose (build-tallest-map (transpose m)))
                      :right  (transpose (reverse (build-tallest-map (reverse (transpose m)))))}]
    (fn [[i j]]
      (let [tree-size (get-in m [i j])]
        (->> (vals tallest-maps)
             (map #(get-in % [i j]))
             (some #(< % tree-size)))))))

(def answer1
  (let [m (read-map)]
    (->> (for [i (range (count m))
               j (range (count (first m)))]
           (visible? [i j]))
         (filter identity)
         count)))
