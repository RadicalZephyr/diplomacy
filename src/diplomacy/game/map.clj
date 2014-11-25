(ns diplomacy.game.map
  (:require [diplomacy.game.connect :as cn]
            [clojure.java.io :as io]
            [seesaw.core :as s]
            [seesaw.graphics :as g])
  (:import  (javax.imageio ImageIO
                           ImageReader
                           IIOImage)
            java.awt.Color
            java.awt.color.ColorSpace
            java.awt.geom.AffineTransform
            (java.awt.image AffineTransformOp
                            BufferedImage
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
                             :minimum-size [385 :by 410]
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

(def corner->point {:tlo (fn [x y] [(inc x) (inc y)])
                    :tro (fn [x y] [(dec x) (inc y)])
                    :blo (fn [x y] [(inc x) (dec y)])
                    :bro (fn [x y] [(dec x) (dec y)])
                    :tli (fn [x y] [(inc x) (inc y)])
                    :tri (fn [x y] [(dec x) (inc y)])
                    :bli (fn [x y] [(inc x) (dec y)])
                    :bri (fn [x y] [(dec x) (dec y)])
                    })

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

(defn get-area-subimage [img points]
  (let [xs (map first points)
        ys (map second points)
        x-min (apply min xs)
        x-max (apply max xs)
        y-min (apply min ys)
        y-max (apply max ys)]
    (when (and (> x-max x-min)
               (> y-max y-min))
     (.getSubimage img x-min y-min (- x-max x-min) (- y-max y-min)))))

(defmacro with-cleanup [[binding value :as let-vec] close-fn & forms]
  `(let ~let-vec
     (try
       ~@forms
       (finally (~close-fn ~binding)))))

(defn highlight-points [img color points]
  (with-cleanup [gfx (.createGraphics img)] .dispose
    (.setBackground gfx color)
    (dorun
     (map (fn [[x y]]
            (.clearRect gfx x y 1 1))
          points))))

(defn rgb->image [img rgb]
  (let [img-w (.getWidth  img)
        img-h (.getHeight img)
        nimg (BufferedImage. img-w img-h BufferedImage/TYPE_INT_RGB)]
    (dorun
     (for [x (range img-w)
           y (range img-h)
           :let [c (Color. (cn/get2d rgb [x y]))]]
       (.setRGB img x y c)))
    nimg))

(defn connected-image [img]
  (let [rgb (get-all-rgb  img)
        img-w (.getWidth  img)
        img-h (.getHeight img)
        nrgb (cn/connected-components rgb img-w img-h)]
    (rgb->image img nrgb)))

(defn components [img]
  (let [rgb (get-all-rgb img)
        w (.getWidth  img)
        h (.getHeight img)
        crgb (cn/connected-components rgb w h)
        pts (for [x (range w)
                  y (range h)]
              [x y])]
    (binding [cn/max-x w cn/max-y h]
      (->> pts
           (reduce
            (fn [acc pt]
              (update-in acc [(cn/get2d crgb pt)]
                         conj pt))
            {})
           (filter (fn [[_ pts]]
                     (> (count pts) 30)))))))

;; Image pre-processing stuff

(defn threshold-table [threshold]
  (byte-array (map (fn [x] (if (< x threshold) 0 255))
                   (range 256))))

(defn make-scale-op [scale]
  (let [scale (float scale)
        aft (AffineTransform.)]
    (.scale aft scale scale)
    (AffineTransformOp. aft AffineTransformOp/TYPE_BILINEAR)))

(defn color-convert [img colorspace]
  (let [ccop (ColorConvertOp. (ColorSpace/getInstance
                               colorspace)
                              nil)]
    (.filter ccop img nil)))

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
  (with-cleanup [gfx (.createGraphics img)] .dispose
    (.setBackground gfx Color/WHITE)
    (.clearRect gfx 396 3 357 70)
    (.clearRect gfx 394 3 1 70))
  (-main)
  (def fimg (reduce (fn [img op]
                     (.filter op img nil))
                   img filter-ops))
  (draw-image fimg
              (get-canvas))

  (def corners (get-all-corners (w-by-h img [3 3]))))
