(ns diplomacy.game.map
  (:require [clojure.java.io :as io]
            [seesaw.core :as s]
            [seesaw.graphics :as g])
  (:import  (javax.imageio ImageIO
                           ImageReader
                           IIOImage)))

(defn file->image [filename]
  (with-open [istream (ImageIO/createImageInputStream filename)]
   (let [itr (ImageIO/getImageReaders istream)]
     (if-not (.hasNext itr)
       (throw (ex-info  "No image reader found for stream" {:filename filename}))
       (let [reader (.next itr)]
         (.setInput reader istream true)
         (.read reader 0))))))

(defn threshold [cutoff]
  (fn [x] (if (< cutoff)
            0
            127)))

(def root (atom
           (s/frame :title "Images!!"
                    :minimum-size [755 :by 777]
                    :content (s/canvas :id :canvas))))

(defn draw-image [buffered-image canvas]
  (s/config! canvas :paint
             (fn [c g]
               (.drawImage g buffered-image nil 0 0))))

(defn get-canvas []
  (s/select @root [:#canvas]))

(defn -main [& args]
  (s/invoke-later
   (-> @root
    s/pack!
    s/show!)))
