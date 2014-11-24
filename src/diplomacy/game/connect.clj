(ns diplomacy.game.connect)

(def ^:dynamic assoc2d)

(def ^:dynamic neighbours)

(defn mk-assoc2d [xbound ybound]
  (fn [coll [x y]]
    (assoc (+ (* y xbound)
              x))))

(defn mk-bounded-neighbours [xbound ybound]
  (fn [[x y]]
    (into #{}
          (for [dx [-1 0 1]
                dy [-1 0 1]
                :let [nx (+ dx x)
                      ny (+ dy y)]
                :when (and (or (= dx 0)
                               (= dy 0))
                           (not= 0 dx dy)
                           (>= xbound nx 0)
                           (>= ybound ny 0))]
            [(+ dx x) (+ dy y)]))))

(defn find-components [rgbs]
  )

(defn search [rgbs label x y]
  (let [rgbs ()])
  )

(defn connected-components [rgbs w h]
  (binding [assoc2d    (mk-assoc2d w h)
            neighbours (mk-bounded-neighbours w h)]
   (let [label (atom 1)]
     (find-components rgbs label))))
