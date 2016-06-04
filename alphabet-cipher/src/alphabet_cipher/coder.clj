(ns alphabet-cipher.coder)

(defn quick-int [char-seq]
  (map int char-seq))

(defn normalize-chars [int-seq]
  (map (fn [int] (mod (- int 97) 26)) int-seq))

(defn string-to-alphabet-numbers [input-string]
  (-> input-string
      char-array
      seq
      quick-int
      normalize-chars))

(defn denormalize-ints [int-seq]
  (map (fn [int] (+ int 97)) int-seq))

(defn quick-char [char-seq]
  (map char char-seq))

(defn alphabet-numbers-to-string [int-seq]
  (-> int-seq
      denormalize-ints
      quick-char))

(defn normalize-letter [l]
  (if (< l 0)
    (+ l 26)
    (mod l 26)))

(defn add-letters [l1 l2]
  (normalize-letter (+ l1 l2)))

(defn subtract-letters [l1 l2]
  (normalize-letter (- l1 l2)))

(defn str-first
  "Returns the first character in a string as a string"
  [string]
  (subs string 0 1))

(defn str-rest
  "Returns the rest of the characters in a string as a string"
  [string]
  (subs string 1))

;; This will fail if a pattern isn't matched
;; (index out of bounds thrown by subs
;; Switch to cond to account for the failure case
(defn detectPattern [repeatedString]
  (loop [start (str-first repeatedString)
         end (str-rest repeatedString)]
    (if (= start (subs end 0 (.length start)))
      start
      (recur (str start (str-first end))
             (str-rest end)))))

(defn encode [keyword message]
   (let [keyword-ints (string-to-alphabet-numbers keyword)
         keyword-offset-cycle (cycle keyword-ints)
         message-ints (string-to-alphabet-numbers message)]
     (apply str (alphabet-numbers-to-string (map add-letters message-ints keyword-offset-cycle)))))

(defn decode [keyword message]
  (let [keyword-ints (string-to-alphabet-numbers keyword)
        keyword-offset-cycle (cycle keyword-ints)
        message-ints (string-to-alphabet-numbers message)]
  (apply str (alphabet-numbers-to-string (map subtract-letters message-ints keyword-offset-cycle)))))

(defn decipher [cipher message]
  (let [cipher-ints (string-to-alphabet-numbers cipher)
        message-ints (string-to-alphabet-numbers message)]
    (detectPattern (apply str (alphabet-numbers-to-string (map subtract-letters cipher-ints message-ints))))))

