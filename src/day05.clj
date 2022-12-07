(ns day05
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.pprint :refer [pprint]]))

(defn parse-input
  []
  (with-open [rdr (io/reader (io/resource "day05-input.txt"))]
    (let [[stacks _ procedure] (->> (line-seq rdr)
                                    (partition-by empty?)
                                    (doall))
          stack-cnt (edn/read-string (last (str/split (last stacks) #"\s+")))
          index (->> (range 1 (inc stack-cnt))
                     (map #(vector % (+ 1 (* (dec %) 4))))
                     (into {}))]
      {:stacks (reduce (fn [stacks line]
                         (reduce (fn [stacks [stack-idx idx]]
                                   (let [crate (nth line idx)]
                                     (if (= crate \space)
                                       stacks
                                       (update stacks stack-idx conj crate))))
                                 stacks
                                 index))
                       {}
                       (rest (reverse stacks)))
       :procedure (map #(map edn/read-string (re-seq #"\d+" %))
                       procedure)})))

(defn rearrange
  [{:keys [stacks procedure move-crates-at-once?]}]
  (reduce (fn [stacks [n from to]]
            (let [[crates xs] (split-at n (stacks from))]
              (-> stacks
                  (assoc from xs)
                  (update to #(concat (if move-crates-at-once?
                                        crates
                                        (reverse crates))
                                      %)))))
          stacks
          procedure))

(def s (rearrange (parse-input)))

(def answer1
  (->> (parse-input)
       rearrange
       (sort-by key)
       (map #(first (second %)))
       (apply str)))

(def answer2
  (->> (assoc (parse-input) :move-crates-at-once? true)
       rearrange
       (sort-by key)
       (map #(first (second %)))
       (apply str)))
