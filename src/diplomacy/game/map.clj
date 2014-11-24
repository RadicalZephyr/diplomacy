(ns diplomacy.game.map
  (:require [clojure.java.io :as io])
  (:import  (javax.imageio ImageIO IIOImage)))

(defn file->image [filename]
  (let [istream (ImageIO/createImageInputStream filename)
        itr (ImageIO/getImageReader istream)]
    (if-not (.hasNext itr)
      (throw (ex-info  "No image reader found for stream" {:filename filename})))
    (.next itr)))

(defn threshold [cutoff]
  (fn [x] (if (< cutoff)
            0
            127)))
