(ns card-game-war.game)

;; feel free to use these cards or use your own data structure
(def suits [:spade :club :diamond :heart])
(def ranks [2 3 4 5 6 7 8 9 10 :jack :queen :king :ace])
(def named-values {:jack 11
                  :queen 12
                  :king 13
                  :ace 14})

(def suit-boosters {:spade 0
                    :club 0.1
                    :diamond 0.2
                    :heart 0.3})
(def cards
  (for [suit suits
        rank ranks]
    [suit rank]))

(defn name->value [a-keyword]
  (a-keyword named-values))

(defn get-value [card]
  (if (number? (second card))
    (+ (second card) ((first card) suit-boosters))
    (+ (name->value (second card)) ((first card) suit-boosters))))

(defn fix-decks
  "When the deck is exhausted, this will shuffle the discard
  pile and make it the deck."
  [start end]
  (if (empty? start)
    (do (println "===== erryday I'm shuffling =====") [(shuffle end) []])
    [start end]))

;; Convenience mappings for readability
(def draw-from first)
(def discard-into concat)

(defn play-round
  "Takes two cards, returns a vector of cards to add to the
  corresponding decks that the two cards came from."
  [player1-card player2-card]
  (println "\n\nStarting Round:")
  (println "comparing" player1-card "with" player2-card)
  (if (> (get-value player1-card) (get-value player2-card))
    (do (println "player1-card wins") [[player1-card, player2-card], []])
    (do (println "player2-card wins") [[], [player1-card, player2-card]])))

(defn play-game [player1-cards player2-cards]
  (loop [p1-draw-pile player1-cards
         p1-discard-pile []
         p2-draw-pile player2-cards
         p2-discard-pile []]
    (let [[p1-draw-pile, p1-discard-pile] (fix-decks p1-draw-pile p1-discard-pile)
          _ (println "p1 decks" (count p1-draw-pile) (count p1-discard-pile))
          [p2-draw-pile, p2-discard-pile] (fix-decks p2-draw-pile p2-discard-pile)
          _ (println "p2 decks" (count p2-draw-pile) (count p2-discard-pile))]
      (cond
        (empty? p1-draw-pile) "Player 1 Lost!"
        (empty? p2-draw-pile) "Player 2 Lost!"
        :else (let [results (play-round (draw-from p1-draw-pile) (draw-from p2-draw-pile))]
                (recur
                  (rest p1-draw-pile)
                  (discard-into p1-discard-pile (first results))
                  (rest p2-draw-pile)
                  (discard-into p2-discard-pile (second results))))))))
