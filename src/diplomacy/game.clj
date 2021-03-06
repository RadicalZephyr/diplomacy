(ns diplomacy.game
  (:require [diplomacy.game.unit :as u])
  (:require [diplomacy.game.movement :as m]))

;; Short Diplomacy glossary:

;;  Great Power - One of the players in the game, also called a "country"

;;  Province - The areas that the board is divided up into. Basically
;;    any place on the board that has a name

;;  Supply Center - Special types of provinces on the board that grant
;;    the country that controls them an extra fleet or army. All
;;    Supply Centers are on land provinces

;; Define the starting places/pieces
(def starting-pieces {::austria {"Vienna"          u/make-army
                                 "Budapest"        u/make-army
                                 "Trieste"         u/make-fleet}

                      ::england {"London"          u/make-fleet
                                 "Edinburgh"       u/make-fleet
                                 "Liverpool"       u/make-army }

                      ::france  {"Paris"           u/make-army
                                 "Marseilles"      u/make-army
                                 "Brest"           u/make-fleet}

                      ::germany {"Berlin"          u/make-army
                                 "Munich"          u/make-army
                                 "Kiel"            u/make-fleet}

                      ::italy   {"Rome"            u/make-army
                                 "Venice"          u/make-army
                                 "Naples"          u/make-fleet}

                      ::russia  {"Moscow"          u/make-army
                                 "Sevastopol"      u/make-fleet
                                 "Warsaw"          u/make-army
                                 "St Petersburg"   u/make-fleet}

                      ::turkey  {"Ankara"          u/make-fleet
                                 "Constantinople"  u/make-army
                                 "Smyrna"          u/make-army }})


;; This is the canonical source of data on the Diplomacy map. The
;; mapping of abbreviations to provinces is built from this map and,
;; this represents the "truth" about the board at the beginning of the
;; game
(def provinces {"North Africa"
                {:abbreviatons ["naf" "nora"],
                 :name "North Africa",
                 :sc-type ::na,
                 :movement-type m/coastal},
                "Apulia"
                {:abbreviatons ["apu"],
                 :name "Apulia",
                 :sc-type ::na,
                 :movement-type m/coastal},
                "Kiel"
                {:abbreviatons ["kie"],
                 :name "Kiel",
                 :sc-type ::germany,
                 :movement-type m/coastal},
                "Munich"
                {:abbreviatons ["mun"],
                 :name "Munich",
                 :sc-type ::germany,
                 :movement-type m/inland},
                "Brest"
                {:abbreviatons ["bre"],
                 :name "Brest",
                 :sc-type ::france,
                 :movement-type m/coastal},
                "Portugal"
                {:abbreviatons ["por"],
                 :name "Portugal",
                 :sc-type ::independent,
                 :movement-type m/coastal},
                "Gulf of Lyon"
                {:abbreviatons ["lyo" "gol" "gulfofl" "lyon"],
                 :name "Gulf of Lyon",
                 :sc-type ::na,
                 :movement-type m/water},
                "Baltic Sea"
                {:abbreviatons ["bal" "baltic"],
                 :name "Baltic Sea",
                 :sc-type ::na,
                 :movement-type m/water},
                "Eastern Mediterranean"
                {:abbreviatons ["eas" "emed" "east" "eastern" "eastmed" "ems" "eme"],
                 :name "Eastern Mediterranean",
                 :sc-type ::na,
                 :movement-type m/water},
                "Smyrna"
                {:abbreviatons ["smy"],
                 :name "Smyrna",
                 :sc-type ::turkey,
                 :movement-type m/coastal},
                "Livonia"
                {:abbreviatons ["lvn" "livo" "lvo" "lva"],
                 :name "Livonia",
                 :sc-type ::na,
                 :movement-type m/coastal},
                "Ankara"
                {:abbreviatons ["ank"],
                 :name "Ankara",
                 :sc-type ::turkey,
                 :movement-type m/coastal},
                "Tuscany"
                {:abbreviatons ["tus"],
                 :name "Tuscany",
                 :sc-type ::na,
                 :movement-type m/coastal},
                "Tyrolia"
                {:abbreviatons ["tyr" "tyl" "trl"],
                 :name "Tyrolia",
                 :sc-type ::na,
                 :movement-type m/inland},
                "Moscow"
                {:abbreviatons ["mos"],
                 :name "Moscow",
                 :sc-type ::russia,
                 :movement-type m/inland},
                "Warsaw"
                {:abbreviatons ["war"],
                 :name "Warsaw",
                 :sc-type ::russia,
                 :movement-type m/inland},
                "Sweden"
                {:abbreviatons ["swe"],
                 :name "Sweden",
                 :sc-type ::independent,
                 :movement-type m/coastal},
                "Ukraine"
                {:abbreviatons ["ukr"],
                 :name "Ukraine",
                 :sc-type ::na,
                 :movement-type m/inland},
                "Prussia"
                {:abbreviatons ["pru"],
                 :name "Prussia",
                 :sc-type ::na,
                 :movement-type m/coastal},
                "Belgium"
                {:abbreviatons ["bel"],
                 :name "Belgium",
                 :sc-type ::independent,
                 :movement-type m/coastal},
                "Adriatic Sea"
                {:abbreviatons ["adr" "adriatic"],
                 :name "Adriatic Sea",
                 :sc-type ::na,
                 :movement-type m/water},
                "Edinburgh"
                {:abbreviatons ["edi"],
                 :name "Edinburgh",
                 :sc-type ::england,
                 :movement-type m/coastal},
                "Yorkshire"
                {:abbreviatons ["yor" "york" "yonkers"],
                 :name "Yorkshire",
                 :sc-type ::na,
                 :movement-type m/coastal},
                "Ruhr"
                {:abbreviatons ["ruh"],
                 :name "Ruhr",
                 :sc-type ::na,
                 :movement-type m/inland},
                "Burgundy"
                {:abbreviatons ["bur"],
                 :name "Burgundy",
                 :sc-type ::na,
                 :movement-type m/inland},
                "Mid Atlantic Ocean"
                {:abbreviatons ["mao" "midatlanticocean" "midatlantic" "mid" "mat"],
                 :name "Mid Atlantic Ocean",
                 :sc-type ::na,
                 :movement-type m/water},
                "Syria"
                {:abbreviatons ["syr"],
                 :name "Syria",
                 :sc-type ::na,
                 :movement-type m/coastal},
                "Clyde"
                {:abbreviatons ["cly"],
                 :name "Clyde",
                 :sc-type ::na,
                 :movement-type m/coastal},
                "Constantinople"
                {:abbreviatons ["con"],
                 :name "Constantinople",
                 :sc-type ::turkey,
                 :movement-type m/coastal},
                "Berlin"
                {:abbreviatons ["ber"],
                 :name "Berlin",
                 :sc-type ::germany,
                 :movement-type m/coastal},
                "Rumania"
                {:abbreviatons ["rum"],
                 :name "Rumania",
                 :sc-type ::independent,
                 :movement-type m/coastal},
                "Tunis"
                {:abbreviatons ["tun"],
                 :name "Tunis",
                 :sc-type ::independent,
                 :movement-type m/coastal},
                "Western Mediterranean"
                {:abbreviatons ["wes" "wmed" "west" "western" "westmed" "wms" "wme"],
                 :name "Western Mediterranean",
                 :sc-type ::na,
                 :movement-type m/water},
                "Greece"
                {:abbreviatons ["gre"],
                 :name "Greece",
                 :sc-type ::independent,
                 :movement-type m/coastal},
                "Wales"
                {:abbreviatons ["wal"],
                 :name "Wales",
                 :sc-type ::na,
                 :movement-type m/coastal},
                "Budapest"
                {:abbreviatons ["bud"],
                 :name "Budapest",
                 :sc-type ::austria,
                 :movement-type m/inland},
                "Naples"
                {:abbreviatons ["nap"],
                 :name "Naples",
                 :sc-type ::italy,
                 :movement-type m/coastal},
                "Skagerrak"
                {:abbreviatons ["ska"],
                 :name "Skagerrak",
                 :sc-type ::na,
                 :movement-type m/water},
                "Piedmont"
                {:abbreviatons ["pie"],
                 :name "Piedmont",
                 :sc-type ::na,
                 :movement-type m/coastal},
                "Serbia"
                {:abbreviatons ["ser"],
                 :name "Serbia",
                 :sc-type ::independent,
                 :movement-type m/inland},
                "Gascony"
                {:abbreviatons ["gas"],
                 :name "Gascony",
                 :sc-type ::na,
                 :movement-type m/coastal},
                "Black Sea"
                {:abbreviatons ["bla" "black"],
                 :name "Black Sea",
                 :sc-type ::na,
                 :movement-type m/water},
                "Armenia"
                {:abbreviatons ["arm"],
                 :name "Armenia",
                 :sc-type ::na,
                 :movement-type m/coastal},
                "Gulf of Bothnia"
                {:abbreviatons ["bot" "gob" "both" "gulfofb" "bothnia"],
                 :name "Gulf of Bothnia",
                 :sc-type ::na,
                 :movement-type m/water},
                "London"
                {:abbreviatons ["lon"],
                 :name "London",
                 :sc-type ::england,
                 :movement-type m/coastal},
                "Galicia"
                {:abbreviatons ["gal"],
                 :name "Galicia",
                 :sc-type ::na,
                 :movement-type m/inland},
                "Silesia"
                {:abbreviatons ["sil"],
                 :name "Silesia",
                 :sc-type ::na,
                 :movement-type m/inland},
                "Norway"
                {:abbreviatons ["nor" "nwy" "norw"],
                 :name "Norway",
                 :sc-type ::independent,
                 :movement-type m/coastal},
                "Tyrrhenian Sea"
                {:abbreviatons ["tys" "tyrr" "tyrrhenian" "tyn" "tyh"],
                 :name "Tyrrhenian Sea",
                 :sc-type ::na,
                 :movement-type m/water},
                "Trieste"
                {:abbreviatons ["tri"],
                 :name "Trieste",
                 :sc-type ::austria,
                 :movement-type m/coastal},
                "Vienna"
                {:abbreviatons ["vie"],
                 :name "Vienna",
                 :sc-type ::austria,
                 :movement-type m/inland},
                "Albania"
                {:abbreviatons ["alb"],
                 :name "Albania",
                 :sc-type ::na,
                 :movement-type m/coastal},
                "Liverpool"
                {:abbreviatons ["lvp" "livp" "lpl"],
                 :name "Liverpool",
                 :sc-type ::england,
                 :movement-type m/coastal},
                "Marseilles"
                {:abbreviatons ["mar" "mars"],
                 :name "Marseilles",
                 :sc-type ::france,
                 :movement-type m/coastal},
                "North Sea"
                {:abbreviatons ["nth" "norsea" "nts"],
                 :name "North Sea",
                 :sc-type ::na,
                 :movement-type m/water},
                "Rome"
                {:abbreviatons ["rom"],
                 :name "Rome",
                 :sc-type ::italy,
                 :movement-type m/coastal},
                "Finland"
                {:abbreviatons ["fin"],
                 :name "Finland",
                 :sc-type ::na,
                 :movement-type m/coastal},
                "Helgoland Bight"
                {:abbreviatons ["hel" "helgoland"],
                 :name "Helgoland Bight",
                 :sc-type ::na,
                 :movement-type m/water},
                "Aegean Sea"
                {:abbreviatons ["aeg" "aegean"],
                 :name "Aegean Sea",
                 :sc-type ::na,
                 :movement-type m/water},
                "Paris"
                {:abbreviatons ["par"],
                 :name "Paris",
                 :sc-type ::france,
                 :movement-type m/inland},
                "Denmark"
                {:abbreviatons ["den"],
                 :name "Denmark",
                 :sc-type ::independent,
                 :movement-type m/coastal},
                "Barents Sea"
                {:abbreviatons ["bar" "barents"],
                 :name "Barents Sea",
                 :sc-type ::na,
                 :movement-type m/water},
                "Venice"
                {:abbreviatons ["ven"],
                 :name "Venice",
                 :sc-type ::italy,
                 :movement-type m/coastal},
                "English Channel"
                {:abbreviatons ["eng" "english" "channel" "ech"],
                 :name "English Channel",
                 :sc-type ::na,
                 :movement-type m/water},
                "Spain"
                {:abbreviatons ["spa"],
                 :name "Spain",
                 :sc-type ::independent,
                 :movement-type m/coastal},
                "Ionian Sea"
                {:abbreviatons ["ion" "ionian"],
                 :name "Ionian Sea",
                 :sc-type ::na,
                 :movement-type m/water},
                "Irish Sea"
                {:abbreviatons ["iri" "irish"],
                 :name "Irish Sea",
                 :sc-type ::na,
                 :movement-type m/water},
                "Norwegian Sea"
                {:abbreviatons ["nwg" "norwsea" "nrg" "norwegian"],
                 :name "Norwegian Sea",
                 :sc-type ::na,
                 :movement-type m/water},
                "North Atlantic Ocean"
                {:abbreviatons ["nao" "nat"],
                 :name "North Atlantic Ocean",
                 :sc-type ::na,
                 :movement-type m/water},
                "Holland"
                {:abbreviatons ["hol"],
                 :name "Holland",
                 :sc-type ::independent,
                 :movement-type m/coastal},
                "Sevastopol"
                {:abbreviatons ["sev" "sevastapol"],
                 :name "Sevastopol",
                 :sc-type ::russia,
                 :movement-type m/coastal},
                "St Petersburg"
                {:abbreviatons ["stp"],
                 :name "St Petersburg",
                 :sc-type ::russia,
                 :movement-type m/coastal},
                "Bulgaria"
                {:abbreviatons ["bul"],
                 :name "Bulgaria",
                 :sc-type ::independent,
                 :movement-type m/coastal},
                "Picardy"
                {:abbreviatons ["pic"],
                 :name "Picardy",
                 :sc-type ::na,
                 :movement-type m/coastal},
                "Bohemia"
                {:abbreviatons ["boh"],
                 :name "Bohemia",
                 :sc-type ::na,
                 :movement-type m/inland}})

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

(defn add-units-for-country [board [country units]]
  (reduce (fn [board [province mk-unit]]
            (assoc-in board
                      [province :occupied-by]
                      (mk-unit country)))
          board
          units))

(defn place-starting-pieces [board]
  (reduce add-units-for-country board starting-pieces))

(defn initialize-board
  "Returns a game board initialized to the start of game state."
  []
  (->> provinces
   (reduce (fn [acc [pname {:keys [sc-type]}]]
             (assoc acc pname {:owned-by    sc-type
                               :occupied-by ::nothing}))
           {})
   place-starting-pieces))
