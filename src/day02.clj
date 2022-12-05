(ns day02
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.string :as s]
            [clojure.pprint :refer [pprint]]))

(defn strategy-guide
  []
  (with-open [rdr (io/reader (io/resource "day02-input.txt"))]
    (->> (line-seq rdr)
         (map #(s/split % #" "))
         (doall))))

(def winner
  {#{:rock :scissors}  :rock
   #{:rock :paper}     :paper
   #{:paper :scissors} :scissors})

(def shape-score
  {:rock     1
   :paper    2
   :scissors 3})

(def round-score
  {:loss 0
   :draw 3
   :win  6})

(defn play
  [[opponent-shape my-shape :as round]]
  (cond
    (= opponent-shape my-shape)       :draw
    (= my-shape (winner (set round))) :win
    :else                             :loss))

(def answer1
  (->> (strategy-guide)
       (map #(map {"A" :rock
                   "B" :paper
                   "C" :scissors
                   "X" :rock
                   "Y" :paper
                   "Z" :scissors}
                  %))
       (map (fn [round]
              (+ (shape-score (second round))
                 (round-score (play round)))))
       (reduce + 0)))

(def play->shape
  {[:rock     :loss] :scissors
   [:rock     :draw] :rock
   [:rock     :win]  :paper
   [:paper    :loss] :rock
   [:paper    :draw] :paper
   [:paper    :win]  :scissors
   [:scissors :loss] :paper
   [:scissors :draw] :scissors
   [:scissors :win]  :rock})

(def answer2
  (->> (strategy-guide)
       (map #(map {"A" :rock
                   "B" :paper
                   "C" :scissors
                   "X" :loss
                   "Y" :draw
                   "Z" :win}
                  %))
       (map #(+ (shape-score (play->shape %))
                (round-score (second %))))
       (reduce + 0)))
