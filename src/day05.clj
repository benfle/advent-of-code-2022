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
       :procedure ""})))

(pprint (parse-input))
