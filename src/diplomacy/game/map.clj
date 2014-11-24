(ns diplomacy.game.map
  (:require [clojure.java.io :as io]
            [seesaw.core :as s]
            [seesaw.graphics :as g])
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

(defn -main [& args]
  (s/invoke-later
   (->
    (s/frame :title "Images!!"
             :on-close :dispose
             :size [640 :by 480]
             :content (s/canvas :id :canvas))
    s/pack!
    s/show!)))
