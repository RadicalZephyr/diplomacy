(ns diplomacy.graphics.utils-test
  (:require [clojure.test             :refer :all]
            [diplomacy.graphics.utils :refer :all]))

(deftest get-pixels-test
  (testing "Nil returns"
    (is (= (get-pixels nil 0 0 0 1)
           []))
    (is (= (get-pixels nil 0 0 1 0)
           []))
    (is (= (get-pixels nil 0 0 0 0)
           []))))

(deftest set-pixels-test)
