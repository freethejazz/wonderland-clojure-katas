(ns doublets.solver
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]))

(def words (-> "words.edn"
               (io/resource)
               (slurp)
               (read-string)))

(def words-by-size (group-by count words))

(defn get-hamming-distance
  "Returns the difference of character substitutions
  needed to get from w1 to w2"
  [w1 w2]
  (reduce + (map #(if (= %1 %2) 0 1) (seq w1) (seq w2))))

(defn get-words-with-distance [corpus w dist]
  (filter #(= (get-hamming-distance % w) dist) corpus))

(defn sort-by-closeness [coll word]
  (sort-by #(get-hamming-distance % word) coll))

; Start with in place substitutions, later refactor
; to also include additions and substitutions
(defn doublets [word1 word2]
  ; Fail cowardly if word1 and word2
  ; aren't the same length
  (if-not (= (count word1) (count word2))
    []
    (loop [breadcrumbs [word1]
          word-bag (remove #{word1} (get words-by-size (count word1)))
          w1 word1
          w2 word2]
      (let [w1-candidates (get-words-with-distance word-bag w1 1)
            w1-sorted (sort-by-closeness w1-candidates w2)
            next-word (first w1-sorted)]
        (if (= w1 w2)
          breadcrumbs
          (if (nil? w1)
            []
            (recur (conj breadcrumbs next-word)
                   (remove #{next-word} word-bag)
                 next-word
                 w2)))))))
