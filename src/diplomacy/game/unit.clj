(ns diplomacy.game.unit)


(defrecord Unit [type owner])

(defn make-fleet [owner]
  (Unit. ::fleet owner))

(defn make-army [owner]
  (Unit. ::army owner))
