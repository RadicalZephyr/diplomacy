(ns diplomacy.game.unit)

(defprotocol Unit)

(defrecord Fleet [owner]
  Unit)

(defrecord Army [owner]
  Unit)

(defn make-fleet [owner]
  (Fleet. owner))

(defn make-army [owner]
  (Army. owner))
