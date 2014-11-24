(ns diplomacy.game.connect)

(def ^:dynamic max-x)

(def ^:dynamic max-y)

(defn get2d [coll [x y]]
  (get coll (+ (* y max-x)
               x)))

(defn assoc2d [coll [x y]]
  (assoc coll (+ (* y max-x)
                 x)))

(defn bounded-neighbours [[x y]]
  (into #{}
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
  (loop [rgbs (assoc2d rgbs label pt)
         pts (bounded-neighbours pt)]
    (let [pt (get pts 1)]
      (if (and pt
               (= (get2d rgbs pt)
                  -1))
        (recur (search rgbs label pt)
               (disj pts pt))
        rgbs))))

(defn find-components [rgbs label]
  (for [x (range max-x)
        y (range max-y)
        :let [pt [x y]]]
    (when (= (get2d rgbs pt)
           -1)
      (swap! label inc)
      (search rgbs label pt))))

(defn connected-components [rgbs w h]
  (binding [max-x w
            max-y h]
    (let [label (atom 1)]
     (find-components rgbs label))))

(def test-grid [-1 -1 0 -1 -1 -1
                -1 -1 0 -1 0 0
                -1 -1 -1 -1 0 0])
