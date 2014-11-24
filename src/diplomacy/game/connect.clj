(ns diplomacy.game.connect)

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
  (for [dx [-1 0 1]
        dy [-1 0 1]
        :let [nx (+ dx x)
              ny (+ dy y)]
        :when (and (or (= dx 0)
                       (= dy 0))
                   (not= 0 dx dy)
                   (> max-x nx -1)
                   (> max-y ny -1))]
    [(+ dx x) (+ dy y)]))

(defn search [rgbs label pt]
  (loop [rgbs (assoc2d rgbs label pt)
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
    (cond
     (seq pts) (let [pt (first pts)]
                 (if (= (get2d rgbs pt)
                        -1)
                   (do (swap! label inc)
                       (recur (search rgbs label pt)
                              (rest pts)))
                   (recur rgbs (rest pts))))
     :else rgbs)))

(defn connected-components [rgbs w h]
  (binding [max-x w
            max-y h]
    (let [label (atom 1)]
     (find-components rgbs label))))

(def test-grid [-1 -1 0 -1 -1 -1
                -1 -1 0 -1 0 0
                -1 -1 -1 -1 0 0])
