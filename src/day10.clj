(ns day10
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.zip :as zip]
            [clojure.pprint :refer [pprint]]))

(defn instructions
  []
  (with-open [rdr (io/reader (io/resource "day10-input.txt"))]
    (->> (line-seq rdr)
         (map #(let [[_ op _ arg] (re-find #"([^ ]+)( (-?\d+))?" %)]
                 [(keyword op) (edn/read-string arg)]))
         (doall))))

(defn execute
  [rf]
  (let [register (volatile! 1)]
    (fn
      ([]
       (rf))
      ([result]
       (rf result))
      ([result [op arg]]
       (case op
         :noop (rf result @register)
         :addx (let [res (rf result @register)]
                 (if (reduced? res)
                   res
                   (do
                     (vswap! register + arg)
                     (rf res @register)))))))))

(def signal (into [0] execute (instructions)))

(def answer1
  (->> (take 6 (iterate #(+ % 40) 20))
       (map #(* % (nth signal (dec %))))
       (reduce + 0)))

(def screen
  (map (fn [pixel x]
         (if (<= (abs (- x (mod pixel 40))) 1) "#" "."))
       (range 240)
       signal))

(defn print-screen
  [pixels]
  (println)
  (doseq [line (map #(apply str %)
                    (partition-all 40 pixels))]
    (println line)))

(print-screen screen)
