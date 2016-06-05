(ns card-game-war.game-test
  (:require [clojure.test :refer :all]
            [card-game-war.game :refer :all]))


;; fill in  tests for your game
(deftest test-play-round
  (def two-spade [:spade 2])
  (def three-spade [:spade 3])
  (def jack-spade [:spade :jack])
  (def queen-spade [:spade :queen])
  (def king-spade [:spade :king])
  (def ace-spade [:spade :ace])

  (def two-club [:club 2])
  (def two-heart [:heart 2])
  (def two-diamond [:diamond 2])

  (testing "the highest rank wins the cards in the round"
    (is (= [[] [two-spade three-spade]] (play-round two-spade three-spade))))

  (testing "queens are higher rank than jacks"
    (is (= [[] [jack-spade queen-spade]] (play-round jack-spade queen-spade))))

  (testing "kings are higher rank than queens"
    (is (= [[] [queen-spade king-spade]] (play-round queen-spade king-spade))))

  (testing "aces are higher rank than kings"
    (is (= [[] [king-spade ace-spade]] (play-round king-spade ace-spade))))

  (testing "if the ranks are equal, clubs beat spades"
    (is (= [[] [two-spade two-club]] (play-round two-spade two-club))))

  (testing "if the ranks are equal, diamonds beat clubs"
    (is (= [[] [two-club two-diamond]] (play-round two-club two-diamond))))

  (testing "if the ranks are equal, hearts beat diamonds"
    (is (= [[] [two-heart two-heart]] (play-round two-heart two-heart)))))

(deftest test-play-game
  (def shuffledDeck (shuffle cards))
  (def player1 (take 26 shuffledDeck))
  (def player2 (drop 26 shuffledDeck))

  (testing "the player loses when they run out of cards"
    (is (= "Player 1 Lost!" (play-game [] player2)))
    (is (= "Player 2 Lost!" (play-game player1 [])))))

