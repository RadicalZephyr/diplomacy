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


(defn connected-components [rgbs w h & {:keys [impl] :or {:impl :recursive}}]
  (binding [max-x w
            max-y h]
    (case impl
      :recursive (recursive-connected-components rgbs)
      :classical (classical-connected-components rgbs)
      (throw (ex-info "No implementation of that type defined." {:impl impl})))))

(defn print-grid [grid w]
  (dorun (map println (partition w grid))))
