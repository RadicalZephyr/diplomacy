(ns diplomacy.game.connect-test
  (:require [clojure.test           :refer :all]
            [diplomacy.game.connect :refer :all]))

(deftest connected-components-test
  (testing "Recursive connected components"
    (doseq [impl [:recursive :classical]]
      (is (= [1 1 0 1 1 1
              1 1 0 1 0 0
              1 1 1 1 0 0]
             (connected-components 6 3
                                   [-1 -1  0 -1 -1 -1
                                    -1 -1  0 -1  0  0
                                    -1 -1 -1 -1  0  0]
                                   :impl impl)))

      (is (= [1 1 0 2 2 2
              1 1 0 2 0 0
              1 1 1 0 0 0]
             (connected-components 6 3
                                   [-1 -1  0 -1 -1 -1
                                    -1 -1  0 -1  0  0
                                    -1 -1 -1  0  0  0]
                                   :impl impl)))

      (is (= [1 1 0 2 2 2
              1 1 0 2 0 0
              1 1 1 0 3 0]
             (connected-components 6 3
                                   [-1 -1  0 -1 -1 -1
                                    -1 -1  0 -1  0  0
                                    -1 -1 -1  0 -1  0]
                                   :impl impl)))
      (is (= [1 1 0 2 0 2
              1 1 0 2 2 2
              1 1 1 0 2 0]
             (connected-components 6 3 [-1 -1  0 -1  0 -1
                                    -1 -1  0 -1 -1 -1
                                    -1 -1 -1  0 -1  0]
                                   :impl impl)))))
  (testing "Prior-neighbours"
    (binding [max-x 10 max-y 10]
      (is (empty?
          (prior-neighbours [0 0])))
     (doseq [pt [[1 0] [0 1]]]
       (is (= [[0 0]]
              (prior-neighbours pt))))
     (is (= [[0 1] [1 0]]
            (prior-neighbours [1 1])))))

  (testing "label for point"
    (let [rgbs [1 2 3 4
                4 3 2 1]]
      (binding [max-x 4
                max-y 2]
        (is (= (vec
                (labels rgbs (for [x (range 4)
                                   y (range 2)] [x y])))
               [1 4 2 3 3 2 4 1]))))))
