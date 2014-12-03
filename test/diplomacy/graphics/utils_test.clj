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

(def str-type
  {BufferedImage/TYPE_INT_RGB        "INT_RGB"
   BufferedImage/TYPE_INT_ARGB       "INT_ARGB"
   BufferedImage/TYPE_INT_ARGB_PRE   "INT_ARGB_PRE"
   BufferedImage/TYPE_3BYTE_BGR      "3BYTE_BGR"
   BufferedImage/TYPE_4BYTE_ABGR     "4BYTE_ABGR"
   BufferedImage/TYPE_4BYTE_ABGR_PRE "4BYTE_ABGR_PRE"
   BufferedImage/TYPE_USHORT_565_RGB "USHORT_565_RGB"
   BufferedImage/TYPE_USHORT_555_RGB "USHORT_555_RGB"
   BufferedImage/TYPE_BYTE_GRAY      "BYTE_GRAY"
   BufferedImage/TYPE_USHORT_GRAY    "USHORT_GRAY"
   BufferedImage/TYPE_BYTE_BINARY    "BYTE_BINARY"
   BufferedImage/TYPE_BYTE_INDEXED   "BYTE_INDEXED"})

(deftest round-trip-test
  (testing "Starting with a vector"
    (doseq [pixels [[1 2 3 4]
                    [4 3 2 1]
                    [0 1 0 1]
                    [1 0 1 0]]
            img-type [BufferedImage/TYPE_INT_RGB
                      BufferedImage/TYPE_INT_ARGB
                      BufferedImage/TYPE_INT_ARGB_PRE
                      BufferedImage/TYPE_3BYTE_BGR
                      BufferedImage/TYPE_4BYTE_ABGR
                      BufferedImage/TYPE_4BYTE_ABGR_PRE
                      BufferedImage/TYPE_USHORT_565_RGB
                      BufferedImage/TYPE_USHORT_555_RGB
                      BufferedImage/TYPE_BYTE_GRAY
                      BufferedImage/TYPE_USHORT_GRAY
                      BufferedImage/TYPE_BYTE_BINARY
                      BufferedImage/TYPE_BYTE_INDEXED   ]
            :let [img (BufferedImage. 2 2 img-type)]]
      (testing (str pixels " and type " (str-type img-type))
       (is (= pixels
              (-> img
                  (set-pixels 0 0 2 2 pixels)
                  (get-pixels 0 0 2 2))))))))
