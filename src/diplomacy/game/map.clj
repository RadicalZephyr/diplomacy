(ns diplomacy.game.map
  (:require [clojure.java.io :as io])
  (:import  (javax.imageio ImageIO
                           ImageReader
                           IIOImage)))

(defn file->image [filename]
  (let [istream (ImageIO/createImageInputStream filename)
        itr (ImageIO/getImageReaders istream)]
    (if-not (.hasNext itr)
      (throw (ex-info  "No image reader found for stream" {:filename filename}))
      (let [reader (.next itr)]
        (.setInput reader istream true)
        (.read reader 0)))))

(defn threshold [cutoff]
  (fn [x] (if (< cutoff)
            0
            127)))
