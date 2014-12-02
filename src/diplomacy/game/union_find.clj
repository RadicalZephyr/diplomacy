(ns diplomacy.game.union-find
  (:refer-clojure :exclude [find]))

(defn find [parent x]
  (when (not= x 0)
    (loop [j x]
      (let [next (get parent j)]
        (if (and next
                 (not= 0 next))
          (recur next)
          j)))))

(defn pad-to [parent max]
  (into parent
        (take (- max (count parent))
              (repeat 0))))

(defn union [parent x y]
  (let [parent (pad-to parent (max x y))
        px (find parent x)
        py (find parent y)]
    (if (and px py
             (not= px py))
      (assoc parent py px)
      parent)))

(defn add-label [parent]
  (conj parent 0))

(def empty-union-find [0])
