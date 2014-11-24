(ns diplomacy.game.map
  (:require [clojure.java.io :as io]
            [seesaw.core :as s]
            [seesaw.graphics :as g])
  (:import  (javax.imageio ImageIO
                           ImageReader
                           IIOImage)
            java.awt.Color
            java.awt.color.ColorSpace
            java.awt.geom.AffineTransform
            (java.awt.image AffineTransformOp
                            ByteLookupTable
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

(defn grab-pixels [img [x y] [w h]]
  (let [px (int-array (* w h))
        pg (PixelGrabber. img x y w h px 0 w)]
    (if (.grabPixels pg)
      (into [] px))))

(defn grab-all-pixels [img [w h]]
  (let [img-w (.getWidth img)
        img-h (.getHeight img)]
    (for [x (range (- img-w w))
          y (range (- img-h h))]
      {:x x :y y :pixels (grab-pixels img [x y] [w h])})))

(def corner-patterns-2x {[-16777216 -16777216 -16777216 -1] :tlo
                         [-16777216 -16777216 -1 -16777216] :tro
                         [-16777216 -1 -16777216 -16777216] :blo
                         [-1 -16777216 -16777216 -16777216] :bro
                         [-16777216 -1 -1 -1] :tli
                         [-1 -16777216 -1 -1] :tri
                         [-1 -1 -16777216 -1] :bli
                         [-1 -1 -1 -16777216] :bri})

(def corner-patterns-3x {[-16777216 -16777216 -16777216
                          -16777216 -1 -1
                          -16777216 -1 -1] :tlo
                         [-16777216 -16777216 -16777216
                          -1 -1 -16777216
                          -1 -1 -16777216] :tro
                         [-16777216 -1 -1
                          -16777216 -1 -1
                          -16777216 -16777216 -16777216] :blo
                         [-1 -1 -16777216
                          -1 -1 -16777216
                          -16777216 -16777216 -16777216] :bro
                         [-16777216 -1 -1
                          -1 -1 -1
                          -1 -1 -1] :tli
                         [-1 -1 -16777216
                          -1 -1 -1
                          -1 -1 -1] :tri
                         [-1 -1 -1
                          -1 -1 -1
                          -16777216 -1 -1] :bli
                         [-1 -1 -1
                          -1 -1 -1
                          -1 -1 -16777216] :bri})


(defn corner? [{pxs :pixels}]
  (corner-patterns-3x pxs))

(defn to-corner [{:keys [x y pixels] :as m}]
  (assoc m :corner (corner-patterns-3x pixels)))

(defn classify-all-pixels [img]
  (let [w 2 h 2
        img-w (.getWidth img)
        img-h (.getHeight img)]
    (for [x (range (- img-w w))
          y (range (- img-h h))
          :let [pxs (grab-pixels img [x y] [w h])
                id (corner-patterns-2x pxs)]
          :when id]
      {:x x :y y :type id})))

(defn get-all-rgb [img]
  (let [img-w (.getWidth  img)
        img-h (.getHeight img)]
    (into [] (.getRGB img 0 0 img-w img-h nil 0 img-w))))

(defn make-get-xy [img]
  (let [img-w (.getWidth  img)]
    (fn [coll [x y]]
      (get coll (+ x
                   (* y img-w))))))

(defn w-by-h [img [w h]]
  (let [rgbs (get-all-rgb img)
        get-xy (make-get-xy img)
        img-w (.getWidth  img)
        img-h (.getHeight img)]
    (for [x (range (- img-w w))
          y (range (- img-h h))]
      {:x x :y y
       :pixels (into []
                     (map (partial get-xy rgbs)
                          (for [dx (range w)
                                dy (range h)]
                            [(+ x dx) (+ y dy)])))})))

(defn get-all-corners [grids]
  (->> grids
      (filter corner?)
      (map to-corner)))

(defn threshold-table [threshold]
  (byte-array (map (fn [x] (if (< x threshold) 0 255))
                   (range 256))))

(defn make-scale-op [scale]
  (let [scale (float scale)
        aft (AffineTransform.)]
    (.scale aft scale scale)
    (AffineTransformOp. aft AffineTransformOp/TYPE_BILINEAR)))

(def filter-ops
  [(ColorConvertOp. (ColorSpace/getInstance
                     ColorSpace/CS_GRAY)
                    nil)
   (LookupOp. (ByteLookupTable. 0 (threshold-table 150))
              nil)
   (make-scale-op 0.5)
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
  (def img (reduce (fn [img op]
                     (.filter op img nil))
                   img filter-ops))
  (draw-image img
              (get-canvas))

  (def corners (get-all-corners (w-by-h img [3 3]))))
