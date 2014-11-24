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
                            LookupOp)))

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

(do
  (def f (io/file "resources" "diplo-map-simple.gif"))
  (def img  (file->image f))
  (let [gfx (.createGraphics img)]
    (.setBackground gfx Color/WHITE)
    (.clearRect gfx 396 3 357 70)
    (.clearRect gfx 394 3 1 70))
  (-main)
  (draw-image (reduce (fn [img op]
                        (.filter op img nil))
                      img filter-ops)
              (get-canvas)))
