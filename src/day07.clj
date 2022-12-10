(ns day07
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.zip :as zip]
            [clojure.pprint :refer [pprint]]))

(defn terminal-output
  []
  (with-open [rdr (io/reader (io/resource "day07-input.txt"))]
    (->> (line-seq rdr)
         (doall))))

(defn visit-child
  [dir child-name]
  (loop [loc (zip/down dir)]
    (if (= (:name (zip/node loc)) child-name)
      loc
      (recur (zip/right loc)))))

(defn build-directory-tree
  [lines]
  (let [tree-zip #(zip/zipper
                   (fn [node]
                     (= :dir (:type node)))
                   :children
                   (fn [node children]
                     (assoc node :children children))
                   %)]
    (->> lines
         (map #(str/split % #" "))
         (reduce (fn [loc [first second third :as line]]
                   (case first
                     "$"  (case second
                            "cd" (case third
                                   "/" (tree-zip (zip/root loc))
                                   ".." (zip/up loc)
                                   (visit-child loc third))
                            "ls" loc)
                     "dir" (zip/append-child loc {:type :dir
                                                  :name second})
                     (zip/append-child loc {:type :file
                                            :name second
                                            :size (edn/read-string first)})))
                 (tree-zip {:type :dir
                            :name "/"}))
         zip/root)))

(defn add-sizes
  [node]
  (case (:type node)
    :file node
    :dir (let [children (map add-sizes (:children node))]
           (-> node
               (assoc :children children)
               (assoc :size (reduce + 0 (map :size children)))))))

(defn fs-seq
  []
  (->> (terminal-output)
       build-directory-tree
       add-sizes
       (tree-seq #(= (:type %) :dir)
                 :children)))

(def answer1
  (->> (fs-seq)
       (filter #(and (= :dir (:type %))
                     (< (:size %) 100000)))
       (map :size)
       (reduce + 0)))

(def filesystem-size 70000000)
(def required-size   30000000)
(def available-size (- filesystem-size (:size (first (fs-seq)))))
(def freeup-size    (- required-size available-size))

(def answer2
  (->> (fs-seq)
       (filter #(and (= :dir (:type %))
                     (<= freeup-size (:size %))))
       (sort-by :size)
       first
       :size))
