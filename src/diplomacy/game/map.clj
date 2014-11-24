(ns diplomacy.game.map
  (:require [clojure.java.io :as io]
            [seesaw.core :as s]
            [seesaw.graphics :as g])
  (:import  (javax.imageio ImageIO
                           ImageReader
                           IIOImage)
            java.awt.Color
            java.awt.color.ColorSpace
            (java.awt.image ByteLookupTable
                            ColorConvertOp
                            LookupOp
                            PixelGrabber)))

(defn file->image [filename]
  (with-open [istream (ImageIO/createImageInputStream filename)]
   (let [itr (ImageIO/getImageReaders istream)]
     (if-not (.hasNext itr)
       (throw (ex-info  "No image reader found for stream" {:filename filename}))
       (let [reader (.next itr)]
         (.setInput reader istream true)
         (.read reader 0))))))

(def root (atom nil))

(defn draw-image [buffered-image canvas & {:keys [op]}]
  (s/config! canvas :paint
             (fn [c g]
               (.drawImage g buffered-image op 0 0))))

(defn draw-image-rect [img canvas rect]
  (s/config! canvas :paint
             (fn [c g]
               (.drawImage g img nil 0 0)
               (.fill g rect))))

(defn get-canvas []
  (s/select @root [:#canvas]))

(defn show-frame [frame]
  (s/invoke-later
   (-> frame
       s/pack!
       s/show!)))

(defn -main [& args]
  (compare-and-set! root
                    nil
                    (s/frame :title "Images!!"
                             :minimum-size [755 :by 777]
                             :content (s/canvas :id :canvas)))
  (show-frame @root))

(defn threshold-table [threshold]
  (byte-array (map (fn [x] (if (< x threshold) 0 255))
                   (range 256))))

;; Create a ColorConvertOp to transform to grayscale
(def filter-ops
  [(ColorConvertOp. (ColorSpace/getInstance
                     ColorSpace/CS_GRAY)
                    nil)
   (LookupOp. (ByteLookupTable. 0 (threshold-table 150))
              nil)])

(defn grab-pixels [img [x y] [w h]]
  (let [px (int-array (* w h))
        pg (PixelGrabber. img x y w h px 0 w)]
    (if (.grabPixels pg)
      (into [] px))))

(def outside-corner-patterns {[-16777216 -16777216 -16777216 -1] :tlo
                              [-16777216 -16777216 -1 -16777216] :tro
                              [-16777216 -1 -16777216 -16777216] :blo
                              [-1 -16777216 -16777216 -16777216] :bro})

(def inside-corner-patterns {[-16777216 -1 -1 -1] :tli
                             [-1 -16777216 -1 -1] :tri
                             [-1 -1 -16777216 -1] :bli
                             [-1 -1 -1 -16777216] :bri})

(do
  (def f (io/file "resources" "diplo-map-simple.gif"))
  (def img  (file->image f))
  (let [gfx (.createGraphics img)]
    (.setBackground gfx Color/WHITE)
    (.clearRect gfx 396 3 357 70)
    (.clearRect gfx 394 3 1 70))
  (-main)
  (def img (reduce (fn [img op]
                     (.filter op img nil))
                   img filter-ops))
  (draw-image img
              (get-canvas)))
