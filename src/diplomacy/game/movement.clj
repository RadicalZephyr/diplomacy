(ns diplomacy.game.movement)

(def coastal ::coastal)
(def water   ::water)
(def inland  ::inland)

(defn check-province-movement-type [province expected]
  (let [movement-type (cond
                       ;; Must be iterating through a seq of provinces
                       (and (vector? province)
                            (= (count province)
                               2))
                       (get-in province [1 :movement-type])

                       ;; Got a simple province map
                       (map? province)
                       (get province :movement-type))]
    (= movement-type
       expected)))

(defn land-locked? [province]
  (check-province-movement-type province
                                inland))

(defn coastal? [province]
  (check-province-movement-type province
                                coastal))

(defn water? [province]
  (check-province-movement-type province
                                water))
