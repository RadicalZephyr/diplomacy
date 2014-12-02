(ns diplomacy.game.connect-test
  (:require [clojure.test           :refer :all]
            [diplomacy.game.connect :refer :all]))

(deftest connected-components-test
  (testing "Recursive connected components"
    (doseq [impl [:recursive]]
      (is (= [1 1 0 1 1 1
              1 1 0 1 0 0
              1 1 1 1 0 0]
             (connected-components [-1 -1  0 -1 -1 -1
                                    -1 -1  0 -1  0  0
                                    -1 -1 -1 -1  0  0]
                                   6 3
                                   :impl :recursive)))

      (is (= [1 1 0 2 2 2
              1 1 0 2 0 0
              1 1 1 0 0 0]
             (connected-components [-1 -1  0 -1 -1 -1
                                    -1 -1  0 -1  0  0
                                    -1 -1 -1  0  0  0]
                                   6 3
                                   :impl :recursive)))

      (is (= [1 1 0 2 2 2
              1 1 0 2 0 0
              1 1 1 0 3 0]
             (connected-components [-1 -1  0 -1 -1 -1
                                    -1 -1  0 -1  0  0
                                    -1 -1 -1  0 -1  0]
                                   6 3
                                   :impl :recursive)))))
  (testing "Prior-neighbours"
    (binding [max-x 10 max-y 10]
      (is (empty?
          (prior-neighbours [0 0])))
     (doseq [pt [[1 0] [0 1]]]
       (is (= [[0 0]]
              (prior-neighbours pt))))
     (is (= [[0 1] [1 0]]
            (prior-neighbours [1 1]))))))
