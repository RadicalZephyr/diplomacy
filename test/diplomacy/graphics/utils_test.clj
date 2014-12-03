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

(deftest set-pixels-test
  (testing "Nil returns"
    (is (= (set-pixels nil 0 0 0 1 [])
           nil))
    (is (= (set-pixels nil 0 0 1 0 [])
           nil))
    (is (= (set-pixels nil 0 0 0 0 [])
           nil)))

  (testing "Set pixel of image an image from vector"
    (doseq [img-type [BufferedImage/TYPE_INT_RGB
                      BufferedImage/TYPE_INT_ARGB
                      BufferedImage/TYPE_3BYTE_BGR
                      BufferedImage/TYPE_4BYTE_ABGR]]
      (let [img (BufferedImage. 10 10 img-type)]
        (is (= img
               (set-pixels img 0 0 2 2 [1 0 1 0])))))))

(deftest round-trip-test
  (testing "Starting with a vector"
    (doseq [pixels [[1 2 3 4]
                    [4 3 2 1]
                    [0 1 0 1]
                    [1 0 1 0]]
            img-type [BufferedImage/TYPE_INT_RGB
                      BufferedImage/TYPE_INT_ARGB
                      BufferedImage/TYPE_3BYTE_BGR
                      BufferedImage/TYPE_4BYTE_ABGR]
            :let [img (BufferedImage. 2 2 img-type)]]
      (is (= pixels
             (-> img
                 (set-pixels 0 0 2 2 pixels)
              (get-pixels 0 0 2 2)))))))
