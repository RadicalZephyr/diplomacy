(ns diplomacy.game.union-find-test
  (:refer-clojure :exclude [find])
  (:require [clojure.test :refer :all]
            [diplomacy.game.union-find :refer :all]))

(deftest union-find-test

  (let [test-struct [0 2 3 0 3 7 7 0 3]]
    (testing "Find operation"
     (is (= (find test-struct 1)
            3))
     (is (= (find test-struct 2)
            3))
     (is (= (find test-struct 3)
            3))
     (is (= (find test-struct 4)
            3))
     (is (= (find test-struct 8)
            3))

     (is (= (find test-struct 5)
            7))
     (is (= (find test-struct 6)
            7))
     (is (= (find test-struct 7)
            7)))
    (testing "Union operation"
      (let [three-over [0 2 3 0 3 7 7 3 3]
            seven-over [0 2 3 7 3 7 7 0 3]]
        (is (= three-over
               (union test-struct 3 7)))
        (is (= seven-over
               (union test-struct 7 3)))))))
