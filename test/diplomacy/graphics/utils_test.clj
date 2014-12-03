(ns diplomacy.graphics.utils-test
  (:require [clojure.test             :refer :all]
            [diplomacy.graphics.utils :refer :all])
  (:import java.awt.image.BufferedImage))

(deftest get-pixels-test
  (testing "Nil returns"
    (is (= (get-pixels nil 0 0 0 1)
           []))
    (is (= (get-pixels nil 0 0 1 0)
           []))
    (is (= (get-pixels nil 0 0 0 0)
           [])))

  (testing "Get pixel vectors from an image"
    (doseq [img-type [BufferedImage/TYPE_INT_RGB
                      BufferedImage/TYPE_INT_ARGB
                      BufferedImage/TYPE_3BYTE_BGR
                      BufferedImage/TYPE_4BYTE_ABGR]]
      (let [img (BufferedImage. 10 10 img-type)]
        (is (= (class [])
               (class (get-pixels img 0 0 10 10))))))))

(deftest set-pixels-test)
