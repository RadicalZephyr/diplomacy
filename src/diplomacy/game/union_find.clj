(ns diplomacy.game.union-find
  (:refer-clojure :exclude [find]))

(defn find [parent x]
  (loop [j x]
    (let [next (get parent j)]
      (if (not= 0 next)
        (recur next)
        j))))

(defn union [parent x y]
  (let [px (find parent x)
        py (find parent y)]
    (if (not= px py)
      (assoc parent py px)
      parent)))

(defn add-label [parent]
  (conj parent 0))

(def empty-union-find [0])
