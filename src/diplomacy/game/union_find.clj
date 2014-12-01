(ns diplomacy.game.union-find)

(defn ufind [parent x]
  (loop [j x]
    (let [next (get parent j)]
      (if (not= 0 next)
        (recur next)
        j))))

(defn uunion [parent x y]
  (let [px (ufind parent x)
        py (ufind parent y)]
    (if (not= px py)
      (assoc parent py px)
      parent)))

(def test-uf [0 2 3 0 3 7 7 0 3])
