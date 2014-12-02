(ns diplomacy.game.connect
  (:require [diplomacy.game.union-find :as uf]))

(def ^:dynamic max-x)

(def ^:dynamic max-y)

(defn get2d [coll [x y]]
  (get coll (+ (* y max-x)
               x)))

(defn assoc2d [coll [x y] val]
  (assoc coll
    (+ (* y max-x)
       x)
    val))

(defn bounded-neighbours [[x y]]
  (doall
   (for [dx [-1 0 1]
         dy [-1 0 1]
         :let [nx (+ dx x)
               ny (+ dy y)]
         :when (and (or (= dx 0)
                        (= dy 0))
                    (not= 0 dx dy)
                    (> max-x nx -1)
                    (> max-y ny -1))]
     [(+ dx x) (+ dy y)])))

(defn search [rgbs label pt]
  (loop [rgbs (assoc2d rgbs pt label)
         pts (bounded-neighbours pt)]
    (if (seq pts)
      (let [pt (first pts)]
        (if (= (get2d rgbs pt)
               -1)
          (recur (search rgbs label pt)
                 (rest pts))
          (recur rgbs
                 (rest pts))))
      rgbs)))

(defn find-components [rgbs label]
  (loop [rgbs rgbs
         pts  (for [x (range max-x)
                    y (range max-y)]
                [x y])]
    (if (seq pts)
      (let [pt (first pts)]
        (if (= (get2d rgbs pt)
               -1)
          (do (swap! label inc)
              (recur (search rgbs @label pt)
                     (rest pts)))
          (recur rgbs (rest pts))))
      rgbs)))

(defn recursive-connected-components [rgbs]
  (let [label (atom 0)]
    (find-components rgbs label)))

(defn prior-neighbours [[x y]]
  (doall
   (for [dx [-1 0]
         dy [-1 0]
         :let [nx (+ dx x)
               ny (+ dy y)]
         :when (and (not= dx dy)
                    (> max-x nx -1)
                    (> max-y ny -1))]
     [nx ny])))

(defn labels [rgbs pts]
  (map (partial get2d rgbs) pts))

(defn pt->pt-and-pn [pts]
  (map (fn [pt]
         {:pt pt
          :pn (prior-neighbours pt)})
       pts))

(defn first-pass [rgbs]
  (let [rgbs (atom rgbs)
        label (atom 0)
        unions (atom uf/empty-union-find)]

    (dorun
     (->> (for [y (range max-y)
                x (range max-x)] [x y])
          pt->pt-and-pn
          (map #(assoc % :value (get2d @rgbs (:pt %))))

          (map (fn [{:keys [pt pn value]}]
                 (println @rgbs)
                 (when (= -1 value)
                   (if (seq pn)
                     (let [labels (labels @rgbs pn)
                           m (apply min labels)]
                       (dorun
                        (map #(swap! unions uf/union % m)
                             labels))
                       (swap! rgbs assoc2d pt m))
                     (do
                       (swap! label inc)
                       (swap! rgbs assoc2d pt @label))))))))

    [@rgbs @unions]))

(defn pass-one [rgbs pts]
  (let [label (atom 1)
        union (atom uf/empty-union-find)]
    (loop [rgbs rgbs
           pts pts]
      (if (seq pts)
        (let [pt (first pts)]
          (if (= (get2d rgbs pt)
                 -1)
            (if-let [pn (seq (prior-neighbours pt))]
              (let [m (->> pn
                           (labels rgbs)
                           (apply min))]
                ;; Union all the labels of the prior-neighbours of this pixel
                (dorun
                 (map #(swap! union uf/union % m)
                      (labels rgbs pn)))
                ;; And then set the label this point in the image with
                ;; the smallest label
                (recur (assoc2d rgbs pt m)
                       (rest pts)))
              ;; Otherwise, just label this point, and
              (do
                (let [lbl @label]
                  (swap! label inc)
                  (recur (assoc2d rgbs pt lbl)
                         (rest pts)))))
            ;; If it's not a -1, then skip this pixel
            (recur rgbs (rest pts))))
        [rgbs @union]))))

(defn pass-two [[rgbs union] orgbs]
  (loop [rgbs rgbs
         pts (for [x (range 0 max-x)
                   y (range 0 max-y)]
               [x y])]
    (if (seq pts)
        (let [pt (first pts)]
          (if (= (get2d orgbs pt)
                 -1)
            ;; Replace the labels with the corresponding unioned root label
            (recur (assoc2d rgbs pt (uf/find union (get2d rgbs pt)))
                   (rest pts))
            ;; If it wasn't originally a -1, then skip this pixel
            (recur rgbs (rest pts))))
        rgbs)))

(defn classical-connected-components [rgbs]
  (-> rgbs
      (pass-one (for [x (range 0 max-x)
                      y (range 0 max-y)]
                  [x y]))
      (pass-two rgbs)))

(defn connected-components [rgbs w h & {:keys [impl] :or {:impl :recursive}}]
  (binding [max-x w
            max-y h]
    (case impl
      :recursive (recursive-connected-components rgbs)
      :classical (classical-connected-components rgbs)
      (throw (ex-info "No implementation of that type defined." {:impl impl})))))

(defn print-grid [grid w]
  (dorun (map println (partition w grid))))
