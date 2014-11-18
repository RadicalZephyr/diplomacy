(ns diplomacy.game)

;; Short Diplomacy glossary:

;;  Great Power - One of the players in the game, also called a "country"

;;  Province - The areas that the board is divided up into. Basically
;;    any place on the board that has a name

;;  Supply Center - Special types of provinces on the board that grant
;;    the country that controls them an extra fleet or army. All
;;    Supply Centers are on land provinces

;; Define the starting places/pieces
(def starting-pieces {:austria {"Vienna"          :diplomacy.game.unit/army
                                "Budapest"        :diplomacy.game.unit/army
                                "Trieste"         :diplomacy.game.unit/fleet}

                      :england {"London"          :diplomacy.game.unit/fleet
                                "Edinburgh"       :diplomacy.game.unit/fleet
                                "Liverpool"       :diplomacy.game.unit/army}

                      :france  {"Paris"           :diplomacy.game.unit/army
                                "Marseilles"      :diplomacy.game.unit/army
                                "Brest"           :diplomacy.game.unit/fleet}

                      :germany {"Berlin"          :diplomacy.game.unit/army
                                "Munich"          :diplomacy.game.unit/army
                                "Kiel"            :diplomacy.game.unit/fleet}

                      :italy   {"Rome"            :diplomacy.game.unit/army
                                "Venice"          :diplomacy.game.unit/army
                                "Naples"          :diplomacy.game.unit/fleet}

                      :russia  {"Moscow"          :diplomacy.game.unit/army
                                "Sevastopol"      :diplomacy.game.unit/fleet
                                "Warsaw"          :diplomacy.game.unit/army
                                "St Petersburg"   :diplomacy.game.unit/fleet}

                      :turkey  {"Ankara"          :diplomacy.game.unit/fleet
                                "Constantinople"  :diplomacy.game.unit/army
                                "Smyrna"          :diplomacy.game.unit/army}})


;; This is the canonical source of data on the Diplomacy map. The
;; mapping of abbreviations to provinces is built from this map and,
;; this represents the "truth" about the board at the beginning of the
;; game
(def provinces {"North Africa"
                {:abbreviatons ["naf" "nora"],
                 :name "North Africa",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/coastal},
                "Apulia"
                {:abbreviatons ["apu"],
                 :name "Apulia",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/coastal},
                "Kiel"
                {:abbreviatons ["kie"],
                 :name "Kiel",
                 :sc-type :diplomacy.game.sc/germany,
                 :movement-type :diplomacy.game.movement/coastal},
                "Munich"
                {:abbreviatons ["mun"],
                 :name "Munich",
                 :sc-type :diplomacy.game.sc/germany,
                 :movement-type :diplomacy.game.movement/inland},
                "Brest"
                {:abbreviatons ["bre"],
                 :name "Brest",
                 :sc-type :diplomacy.game.sc/france,
                 :movement-type :diplomacy.game.movement/coastal},
                "Portugal"
                {:abbreviatons ["por"],
                 :name "Portugal",
                 :sc-type :diplomacy.game.sc/independent,
                 :movement-type :diplomacy.game.movement/coastal},
                "Gulf of Lyon"
                {:abbreviatons ["lyo" "gol" "gulfofl" "lyon"],
                 :name "Gulf of Lyon",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/water},
                "Baltic Sea"
                {:abbreviatons ["bal" "baltic"],
                 :name "Baltic Sea",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/water},
                "Eastern Mediterranean"
                {:abbreviatons ["eas" "emed" "east" "eastern" "eastmed" "ems" "eme"],
                 :name "Eastern Mediterranean",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/water},
                "Smyrna"
                {:abbreviatons ["smy"],
                 :name "Smyrna",
                 :sc-type :diplomacy.game.sc/turkey,
                 :movement-type :diplomacy.game.movement/coastal},
                "Livonia"
                {:abbreviatons ["lvn" "livo" "lvo" "lva"],
                 :name "Livonia",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/coastal},
                "Ankara"
                {:abbreviatons ["ank"],
                 :name "Ankara",
                 :sc-type :diplomacy.game.sc/turkey,
                 :movement-type :diplomacy.game.movement/coastal},
                "Tuscany"
                {:abbreviatons ["tus"],
                 :name "Tuscany",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/coastal},
                "Tyrolia"
                {:abbreviatons ["tyr" "tyl" "trl"],
                 :name "Tyrolia",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/inland},
                "Moscow"
                {:abbreviatons ["mos"],
                 :name "Moscow",
                 :sc-type :diplomacy.game.sc/russia,
                 :movement-type :diplomacy.game.movement/inland},
                "Warsaw"
                {:abbreviatons ["war"],
                 :name "Warsaw",
                 :sc-type :diplomacy.game.sc/russia,
                 :movement-type :diplomacy.game.movement/inland},
                "Sweden"
                {:abbreviatons ["swe"],
                 :name "Sweden",
                 :sc-type :diplomacy.game.sc/independent,
                 :movement-type :diplomacy.game.movement/coastal},
                "Ukraine"
                {:abbreviatons ["ukr"],
                 :name "Ukraine",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/inland},
                "Prussia"
                {:abbreviatons ["pru"],
                 :name "Prussia",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/coastal},
                "Belgium"
                {:abbreviatons ["bel"],
                 :name "Belgium",
                 :sc-type :diplomacy.game.sc/independent,
                 :movement-type :diplomacy.game.movement/coastal},
                "Adriatic Sea"
                {:abbreviatons ["adr" "adriatic"],
                 :name "Adriatic Sea",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/water},
                "Edinburgh"
                {:abbreviatons ["edi"],
                 :name "Edinburgh",
                 :sc-type :diplomacy.game.sc/england,
                 :movement-type :diplomacy.game.movement/coastal},
                "Yorkshire"
                {:abbreviatons ["yor" "york" "yonkers"],
                 :name "Yorkshire",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/coastal},
                "Ruhr"
                {:abbreviatons ["ruh"],
                 :name "Ruhr",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/inland},
                "Burgundy"
                {:abbreviatons ["bur"],
                 :name "Burgundy",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/inland},
                "Mid Atlantic Ocean"
                {:abbreviatons ["mao" "midatlanticocean" "midatlantic" "mid" "mat"],
                 :name "Mid Atlantic Ocean",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/water},
                "Syria"
                {:abbreviatons ["syr"],
                 :name "Syria",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/coastal},
                "Clyde"
                {:abbreviatons ["cly"],
                 :name "Clyde",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/coastal},
                "Constantinople"
                {:abbreviatons ["con"],
                 :name "Constantinople",
                 :sc-type :diplomacy.game.sc/turkey,
                 :movement-type :diplomacy.game.movement/coastal},
                "Berlin"
                {:abbreviatons ["ber"],
                 :name "Berlin",
                 :sc-type :diplomacy.game.sc/germany,
                 :movement-type :diplomacy.game.movement/coastal},
                "Rumania"
                {:abbreviatons ["rum"],
                 :name "Rumania",
                 :sc-type :diplomacy.game.sc/independent,
                 :movement-type :diplomacy.game.movement/coastal},
                "Tunis"
                {:abbreviatons ["tun"],
                 :name "Tunis",
                 :sc-type :diplomacy.game.sc/independent,
                 :movement-type :diplomacy.game.movement/coastal},
                "Western Mediterranean"
                {:abbreviatons ["wes" "wmed" "west" "western" "westmed" "wms" "wme"],
                 :name "Western Mediterranean",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/water},
                "Greece"
                {:abbreviatons ["gre"],
                 :name "Greece",
                 :sc-type :diplomacy.game.sc/independent,
                 :movement-type :diplomacy.game.movement/coastal},
                "Wales"
                {:abbreviatons ["wal"],
                 :name "Wales",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/coastal},
                "Budapest"
                {:abbreviatons ["bud"],
                 :name "Budapest",
                 :sc-type :diplomacy.game.sc/austria,
                 :movement-type :diplomacy.game.movement/inland},
                "Naples"
                {:abbreviatons ["nap"],
                 :name "Naples",
                 :sc-type :diplomacy.game.sc/italy,
                 :movement-type :diplomacy.game.movement/coastal},
                "Skagerrak"
                {:abbreviatons ["ska"],
                 :name "Skagerrak",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/water},
                "Piedmont"
                {:abbreviatons ["pie"],
                 :name "Piedmont",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/coastal},
                "Serbia"
                {:abbreviatons ["ser"],
                 :name "Serbia",
                 :sc-type :diplomacy.game.sc/independent,
                 :movement-type :diplomacy.game.movement/inland},
                "Gascony"
                {:abbreviatons ["gas"],
                 :name "Gascony",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/coastal},
                "Black Sea"
                {:abbreviatons ["bla" "black"],
                 :name "Black Sea",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/water},
                "Armenia"
                {:abbreviatons ["arm"],
                 :name "Armenia",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/coastal},
                "Gulf of Bothnia"
                {:abbreviatons ["bot" "gob" "both" "gulfofb" "bothnia"],
                 :name "Gulf of Bothnia",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/water},
                "London"
                {:abbreviatons ["lon"],
                 :name "London",
                 :sc-type :diplomacy.game.sc/england,
                 :movement-type :diplomacy.game.movement/coastal},
                "Galicia"
                {:abbreviatons ["gal"],
                 :name "Galicia",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/inland},
                "Silesia"
                {:abbreviatons ["sil"],
                 :name "Silesia",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/inland},
                "Norway"
                {:abbreviatons ["nor" "nwy" "norw"],
                 :name "Norway",
                 :sc-type :diplomacy.game.sc/independent,
                 :movement-type :diplomacy.game.movement/coastal},
                "Tyrrhenian Sea"
                {:abbreviatons ["tys" "tyrr" "tyrrhenian" "tyn" "tyh"],
                 :name "Tyrrhenian Sea",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/water},
                "Trieste"
                {:abbreviatons ["tri"],
                 :name "Trieste",
                 :sc-type :diplomacy.game.sc/austria,
                 :movement-type :diplomacy.game.movement/coastal},
                "Vienna"
                {:abbreviatons ["vie"],
                 :name "Vienna",
                 :sc-type :diplomacy.game.sc/austria,
                 :movement-type :diplomacy.game.movement/inland},
                "Albania"
                {:abbreviatons ["alb"],
                 :name "Albania",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/coastal},
                "Liverpool"
                {:abbreviatons ["lvp" "livp" "lpl"],
                 :name "Liverpool",
                 :sc-type :diplomacy.game.sc/england,
                 :movement-type :diplomacy.game.movement/coastal},
                "Marseilles"
                {:abbreviatons ["mar" "mars"],
                 :name "Marseilles",
                 :sc-type :diplomacy.game.sc/france,
                 :movement-type :diplomacy.game.movement/coastal},
                "North Sea"
                {:abbreviatons ["nth" "norsea" "nts"],
                 :name "North Sea",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/water},
                "Rome"
                {:abbreviatons ["rom"],
                 :name "Rome",
                 :sc-type :diplomacy.game.sc/italy,
                 :movement-type :diplomacy.game.movement/coastal},
                "Finland"
                {:abbreviatons ["fin"],
                 :name "Finland",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/coastal},
                "Helgoland Bight"
                {:abbreviatons ["hel" "helgoland"],
                 :name "Helgoland Bight",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/water},
                "Aegean Sea"
                {:abbreviatons ["aeg" "aegean"],
                 :name "Aegean Sea",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/water},
                "Paris"
                {:abbreviatons ["par"],
                 :name "Paris",
                 :sc-type :diplomacy.game.sc/france,
                 :movement-type :diplomacy.game.movement/inland},
                "Denmark"
                {:abbreviatons ["den"],
                 :name "Denmark",
                 :sc-type :diplomacy.game.sc/independent,
                 :movement-type :diplomacy.game.movement/coastal},
                "Barents Sea"
                {:abbreviatons ["bar" "barents"],
                 :name "Barents Sea",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/water},
                "Venice"
                {:abbreviatons ["ven"],
                 :name "Venice",
                 :sc-type :diplomacy.game.sc/italy,
                 :movement-type :diplomacy.game.movement/coastal},
                "English Channel"
                {:abbreviatons ["eng" "english" "channel" "ech"],
                 :name "English Channel",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/water},
                "Spain"
                {:abbreviatons ["spa"],
                 :name "Spain",
                 :sc-type :diplomacy.game.sc/independent,
                 :movement-type :diplomacy.game.movement/coastal},
                "Ionian Sea"
                {:abbreviatons ["ion" "ionian"],
                 :name "Ionian Sea",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/water},
                "Irish Sea"
                {:abbreviatons ["iri" "irish"],
                 :name "Irish Sea",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/water},
                "Norwegian Sea"
                {:abbreviatons ["nwg" "norwsea" "nrg" "norwegian"],
                 :name "Norwegian Sea",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/water},
                "North Atlantic Ocean"
                {:abbreviatons ["nao" "nat"],
                 :name "North Atlantic Ocean",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/water},
                "Holland"
                {:abbreviatons ["hol"],
                 :name "Holland",
                 :sc-type :diplomacy.game.sc/independent,
                 :movement-type :diplomacy.game.movement/coastal},
                "Sevastopol"
                {:abbreviatons ["sev" "sevastapol"],
                 :name "Sevastopol",
                 :sc-type :diplomacy.game.sc/russia,
                 :movement-type :diplomacy.game.movement/coastal},
                "St Petersburg"
                {:abbreviatons ["stp"],
                 :name "St Petersburg",
                 :sc-type :diplomacy.game.sc/russia,
                 :movement-type :diplomacy.game.movement/coastal},
                "Bulgaria"
                {:abbreviatons ["bul"],
                 :name "Bulgaria",
                 :sc-type :diplomacy.game.sc/independent,
                 :movement-type :diplomacy.game.movement/coastal},
                "Picardy"
                {:abbreviatons ["pic"],
                 :name "Picardy",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/coastal},
                "Bohemia"
                {:abbreviatons ["boh"],
                 :name "Bohemia",
                 :sc-type :diplomacy.game.sc/na,
                 :movement-type :diplomacy.game.movement/inland}})

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
  (reduce (fn [board [province type]]
            (assoc-in board
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
   (reduce (fn [acc [pname {:keys [sc-type]}]]
             (assoc acc pname {:owned-by    sc-type
                               :occupied-by ::nothing}))
           {})
   place-starting-pieces))
