(ns diplomacy.game)

;; Short Diplomacy glossary:

;;  Great Power - One of the players in the game, also called a "country"

;;  Province - The areas that the board is divided up into. Basically
;;    any place on the board that has a name

;;  Supply Center - Special types of provinces on the board that grant
;;    the country that controls them an extra fleet or army. All
;;    Supply Centers are on land provinces

;; Define the starting places/pieces
(def starting-pieces {:austria {"Vienna"          :army
                                "Budapest"        :army
                                "Trieste"         :fleet}

                      :england {"London"          :fleet
                                "Edinburgh"       :fleet
                                "Liverpool"       :army}

                      :france  {"Paris"           :army
                                "Marseilles"      :army
                                "Brest"           :fleet}

                      :germany {"Berlin"          :army
                                "Munich"          :army
                                "Kiel"            :fleet}

                      :italy   {"Rome"            :army
                                "Venice"          :army
                                "Naples"          :fleet}

                      :russia  {"Moscow"          :army
                                "Sevastopol"      :fleet
                                "Warsaw"          :army
                                "St Petersburg"   :fleet}

                      :turkey  {"Ankara"          :fleet
                                "Constantinople"  :army
                                "Smyrna"          :army}})


;; This is the canonical source of data on the Diplomacy map. The
;; mapping of abbreviations to provinces is built from this map and,
;; this represents the "truth" about the board at the beginning of the
;; game
(def provinces {"Switzerland" {:name "Switzerland" :type :l
                               :abbreviatons ["swi" "switz"] }
                "Adriatic Sea" {:name "Adriatic Sea" :type :w
                                :abbreviatons ["adr" "adriatic"] }
                "Aegean Sea" {:name "Aegean Sea" :type :w
                              :abbreviatons ["aeg" "aegean"] }
                "Albania" {:name "Albania" :type :l
                           :abbreviatons ["alb"] }
                "Ankara" {:name "Ankara" :type :T
                          :abbreviatons ["ank"] }
                "Apulia" {:name "Apulia" :type :l
                          :abbreviatons ["apu"] }
                "Armenia" {:name "Armenia" :type :l
                           :abbreviatons ["arm"] }
                "Baltic Sea" {:name "Baltic Sea" :type :w
                              :abbreviatons ["bal" "baltic"] }
                "Barents Sea" {:name "Barents Sea" :type :w
                               :abbreviatons ["bar" "barents"] }
                "Belgium" {:name "Belgium" :type :x
                           :abbreviatons ["bel"] }
                "Berlin" {:name "Berlin" :type :G
                          :abbreviatons ["ber"] }
                "Black Sea" {:name "Black Sea" :type :w
                             :abbreviatons ["bla" "black"] }
                "Bohemia" {:name "Bohemia" :type :l
                           :abbreviatons ["boh"] }
                "Brest" {:name "Brest" :type :F
                         :abbreviatons ["bre"] }
                "Budapest" {:name "Budapest" :type :A
                            :abbreviatons ["bud"] }
                "Bulgaria" {:name "Bulgaria" :type :x
                            :abbreviatons ["bul"] }
                "Burgundy" {:name "Burgundy" :type :l
                            :abbreviatons ["bur"] }
                "Clyde" {:name "Clyde" :type :l
                         :abbreviatons ["cly"] }
                "Constantinople" {:name "Constantinople" :type :T
                                  :abbreviatons ["con"] }
                "Denmark" {:name "Denmark" :type :x
                           :abbreviatons ["den"] }
                "Eastern Mediterranean" {:name "Eastern Mediterranean" :type :w
                                         :abbreviatons ["eas" "emed" "east"
                                                        "eastern" "eastmed"
                                                        "ems" "eme"] }
                "Edinburgh" {:name "Edinburgh" :type :E
                             :abbreviatons ["edi"] }
                "English Channel" {:name "English Channel" :type :w
                                   :abbreviatons ["eng" "english" "channel" "ech"] }
                "Finland" {:name "Finland" :type :l
                           :abbreviatons ["fin"] }
                "Galicia" {:name "Galicia" :type :l
                           :abbreviatons ["gal"] }
                "Gascony" {:name "Gascony" :type :l
                           :abbreviatons ["gas"] }
                "Greece" {:name "Greece" :type :x
                          :abbreviatons ["gre"] }
                "Gulf of Lyon" {:name "Gulf of Lyon" :type :w
                                :abbreviatons ["lyo" "gol" "gulfofl" "lyon"] }
                "Gulf of Bothnia" {:name "Gulf of Bothnia" :type :w
                                   :abbreviatons ["bot" "gob" "both"
                                                  "gulfofb" "bothnia"] }
                "Helgoland Bight" {:name "Helgoland Bight" :type :w
                                   :abbreviatons ["hel" "helgoland"] }
                "Holland" {:name "Holland" :type :x
                           :abbreviatons ["hol"] }
                "Ionian Sea" {:name "Ionian Sea" :type :w
                              :abbreviatons ["ion" "ionian"] }
                "Irish Sea" {:name "Irish Sea" :type :w
                             :abbreviatons ["iri" "irish"] }
                "Kiel" {:name "Kiel" :type :G
                        :abbreviatons ["kie"] }
                "Liverpool" {:name "Liverpool" :type :E
                             :abbreviatons ["lvp" "livp" "lpl"] }
                "Livonia" {:name "Livonia" :type :l
                           :abbreviatons ["lvn" "livo" "lvo" "lva"] }
                "London" {:name "London" :type :E
                          :abbreviatons ["lon"] }
                "Marseilles" {:name "Marseilles" :type :F
                              :abbreviatons ["mar" "mars"] }
                "Mid Atlantic Ocean" {:name "Mid Atlantic Ocean" :type :w
                                      :abbreviatons ["mao" "midatlanticocean"
                                                     "midatlantic" "mid" "mat"] }
                "Moscow" {:name "Moscow" :type :R
                          :abbreviatons ["mos"] }
                "Munich" {:name "Munich" :type :G
                          :abbreviatons ["mun"] }
                "Naples" {:name "Naples" :type :I
                          :abbreviatons ["nap"] }
                "North Atlantic Ocean" {:name "North Atlantic Ocean" :type :w
                                        :abbreviatons ["nao" "nat"] }
                "North Africa" {:name "North Africa" :type :l
                                :abbreviatons ["naf" "nora"] }
                "North Sea" {:name "North Sea" :type :w
                             :abbreviatons ["nth" "norsea" "nts"] }
                "Norway" {:name "Norway" :type :x
                          :abbreviatons ["nor" "nwy" "norw"] }
                "Norwegian Sea" {:name "Norwegian Sea" :type :w
                                 :abbreviatons ["nwg" "norwsea" "nrg" "norwegian"] }
                "Paris" {:name "Paris" :type :F
                         :abbreviatons ["par"] }
                "Picardy" {:name "Picardy" :type :l
                           :abbreviatons ["pic"] }
                "Piedmont" {:name "Piedmont" :type :l
                            :abbreviatons ["pie"] }
                "Portugal" {:name "Portugal" :type :x
                            :abbreviatons ["por"] }
                "Prussia" {:name "Prussia" :type :l
                           :abbreviatons ["pru"] }
                "Rome" {:name "Rome" :type :I
                        :abbreviatons ["rom"] }
                "Ruhr" {:name "Ruhr" :type :l
                        :abbreviatons ["ruh"] }
                "Rumania" {:name "Rumania" :type :x
                           :abbreviatons ["rum"] }
                "Serbia" {:name "Serbia" :type :x
                          :abbreviatons ["ser"] }
                "Sevastopol" {:name "Sevastopol" :type :R
                              :abbreviatons ["sev" "sevastapol"] }
                "Silesia" {:name "Silesia" :type :l
                           :abbreviatons ["sil"] }
                "Skagerrak" {:name "Skagerrak" :type :w
                             :abbreviatons ["ska"] }
                "Smyrna" {:name "Smyrna" :type :T
                          :abbreviatons ["smy"] }
                "Spain" {:name "Spain" :type :x
                         :abbreviatons ["spa"] }
                "St Petersburg" {:name "St Petersburg" :type :R
                                 :abbreviatons ["stp"] }
                "Sweden" {:name "Sweden" :type :x
                          :abbreviatons ["swe"] }
                "Syria" {:name "Syria" :type :l
                         :abbreviatons ["syr"] }
                "Trieste" {:name "Trieste" :type :A
                           :abbreviatons ["tri"] }
                "Tunis" {:name "Tunis" :type :x
                         :abbreviatons ["tun"] }
                "Tuscany" {:name "Tuscany" :type :l
                           :abbreviatons ["tus"] }
                "Tyrolia" {:name "Tyrolia" :type :l
                           :abbreviatons ["tyr" "tyl" "trl"] }
                "Tyrrhenian Sea" {:name "Tyrrhenian Sea" :type :w
                                  :abbreviatons ["tys" "tyrr" "tyrrhenian"
                                                 "tyn" "tyh"] }
                "Ukraine" {:name "Ukraine" :type :l
                           :abbreviatons ["ukr"] }
                "Venice" {:name "Venice" :type :I
                          :abbreviatons ["ven"] }
                "Vienna" {:name "Vienna" :type :A
                          :abbreviatons ["vie"] }
                "Wales" {:name "Wales" :type :l
                         :abbreviatons ["wal"] }
                "Warsaw" {:name "Warsaw" :type :R
                          :abbreviatons ["war"] }
                "Western Mediterranean" {:name "Western Mediterranean" :type :w
                                         :abbreviatons ["wes" "wmed" "west"
                                                        "western" "westmed"
                                                        "wms" "wme"] }
                "Yorkshire" {:name "Yorkshire" :type :l
                             :abbreviatons ["yor" "york" "yonkers"] }
                }  )

;; This is a convenience data structure built from the province map.
;; It allows us to do a fast lookup from abbreviations to the
;; corresponding province name
(def abbreviations (atom {}))

(defn add-to-abbreviations [board-atm [name {abbrevs :abbreviatons}]]
  (dorun (map (fn [abbrev] (swap! board-atm assoc abbrev name)) abbrevs)))

(defn build-abbreviations []
  (dorun (map (partial add-to-abbreviations abbreviations)
              provinces)))

(build-abbreviations)

;; (keys @abbreviations) gives a seq of all the possible abbreviations

(defn abbrev->area [abbrev]
  (->> abbrev
       (get @abbreviations)
       (get provinces)))

(defrecord Unit [type owner])

(defn make-unit [type owner]
  (Unit. type owner))

(defn add-units-for-country [board [country units]]
  (reduce (fn [board [province type]] (assoc-in board
                                                [province :occupied-by]
                                                (Unit. type country)))
          board
          units))

(defn place-starting-pieces [board]
  (reduce add-units-for-country board starting-pieces))

(defn initialize-board
  "Returns a game board initialized to the start of game state."
  []
  (->> provinces
   (reduce (fn [acc [pname {:keys [type]}]]
             (assoc acc pname {:type type
                               :occupied-by ::nothing}))
           {})
   place-starting-pieces))
