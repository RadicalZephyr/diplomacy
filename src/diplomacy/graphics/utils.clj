(ns diplomacy.graphics.utils
  (:import java.awt.image.BufferedImage))

(defn get-pixels [img x y w h]
  (cond
   (or (= w 0)
       (= h 0)) []
   :else
   (let [img-type (.getType img)
         pixels (int-array (* w h))]
     (into []
           (if (or (= img-type BufferedImage/TYPE_INT_ARGB)
                   (= img-type BufferedImage/TYPE_INT_RGB))
             (let [raster (.getRaster img)]
               (.getDataElements raster x y w h pixels))
             (do
               (.getRGB img x y w h pixels 0 w)
               pixels))))))

(defn set-pixels [img x y w h pixels]
  (cond
   (or (not pixels)
       (= w 0)
       (= h 0)) nil
   (> (* w h)
      (count pixels))
   (throw (ex-info "Pixels length must be greater than w*h"
                   {:count (count pixels)
                    :width w
                    :height h}))
   :else (let [img-type (.getType img)
               pixels (int-array pixels)]
           (if (or (= img-type BufferedImage/TYPE_INT_ARGB)
                   (= img-type BufferedImage/TYPE_INT_RGB))
             (let [raster (.getRaster img)]
               (.setDataElements raster x y w h pixels))
             (.setRGB img x y w h pixels 0 w))
           img)))
