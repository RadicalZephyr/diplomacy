(ns diplomacy.game.union-find-test
  (:refer-clojure :exclude [find])
  (:require [clojure.test              :refer :all]
            [diplomacy.game.union-find :refer :all]))

(deftest union-find-test

  (let [test-struct [0 2 3 0 3 7 7 0 3]]
    (testing "Find operation"
      (doseq [val [1 2 3 4 8]]
        (is (= (find test-struct val)
               3)))
      (doseq [val [5 6 7]]
        (is (= (find test-struct val)
               7))))

    (testing "Union operation"
      (let [three-over [0 2 3 0 3 7 7 3 3]
            seven-over [0 2 3 7 3 7 7 0 3]]
        (doseq [first [1 2 3 4 8]
                second [5 6 7]]
          (is (= three-over
                 (union test-struct first second)))
          (is (= seven-over
                 (union test-struct second first))))))

    (testing "Missing elements"
      (is (= (find empty-union-find 1)
             1))
      (is (= (union empty-union-find 1 2)
             [0 0 1]))
      (is (= (union empty-union-find 2 1)
             [0 2 0])))))
