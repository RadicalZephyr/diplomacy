(ns diplomacy.game.union-find-test
  (:require [clojure.test :refer :all]
            [diplomacy.game.union-find :refer :all]))

(deftest union-find-test
  (testing "Find operation"
    (let [test-struct [0 2 3 0 3 7 7 0 3]]
      (is (= (ufind test-struct 1)
             3))
      (is (= (ufind test-struct 2)
             3))
      (is (= (ufind test-struct 3)
             3))
      (is (= (ufind test-struct 4)
             3))
      (is (= (ufind test-struct 8)
             3))

      (is (= (ufind test-struct 5)
             7))
      (is (= (ufind test-struct 6)
             7))
      (is (= (ufind test-struct 7)
             7)))))
