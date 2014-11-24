(ns diplomacy.game.connect)

(def ^:dynamic max-x)

(def ^:dynamic max-y)

(defn get2d [coll [x y]])

(defn assoc2d [coll [x y]]
  (assoc (+ (* y max-x)
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

(defn find-components [rgbs label]
  (for [x (range max-x)
        y (range max-y)]
    (if )
    )
  )

(defn search [rgbs label x y]
  (let [rgbs ()])
  )

(defn connected-components [rgbs w h]
  (binding [max-x w
            max-y h]
   (let [label (atom 1)]
     (find-components rgbs label w h))))
