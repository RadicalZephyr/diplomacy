(ns diplomacy.game.connect)


(defn mk-bounded-neighbours [xbound ybound]
  (fn [[x y]]
    (for [dx [-1 0 1]
          dy [-1 0 1]
          :let [nx (+ dx x)
                ny (+ dy y)]
          :when (and (or (= dx 0)
                         (= dy 0))
                     (not= 0 dx dy)
                     (>= xbound nx 0)
                     (>= ybound ny 0))]
      [(+ dx x) (+ dy y)])))

(defn find-components [rgbs]
  )

(defn search [rgdbs label x y]
  )

(defn connected-components [rgbs]
  (let [label (atom 1)]
    ))
