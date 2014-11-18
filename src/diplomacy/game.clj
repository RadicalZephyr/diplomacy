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
(def provinces {"North Africa"
                {:abbreviatons ["naf" "nora"],
                 :name "North Africa",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/land},
                "Apulia"
                {:abbreviatons ["apu"],
                 :name "Apulia",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/land},
                "Kiel"
                {:abbreviatons ["kie"],
                 :name "Kiel",
                 :sc-type :diplomacy.game.sc/germany,
                 :type :diplomacy.game.movement/land},
                "Munich"
                {:abbreviatons ["mun"],
                 :name "Munich",
                 :sc-type :diplomacy.game.sc/germany,
                 :type :diplomacy.game.movement/land},
                "Brest"
                {:abbreviatons ["bre"],
                 :name "Brest",
                 :sc-type :diplomacy.game.sc/france,
                 :type :diplomacy.game.movement/land},
                "Portugal"
                {:abbreviatons ["por"],
                 :name "Portugal",
                 :sc-type :diplomacy.game.sc/independent,
                 :type :diplomacy.game.movement/land},
                "Gulf of Lyon"
                {:abbreviatons ["lyo" "gol" "gulfofl" "lyon"],
                 :name "Gulf of Lyon",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/water},
                "Baltic Sea"
                {:abbreviatons ["bal" "baltic"],
                 :name "Baltic Sea",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/water},
                "Eastern Mediterranean"
                {:abbreviatons ["eas" "emed" "east" "eastern" "eastmed" "ems" "eme"],
                 :name "Eastern Mediterranean",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/water},
                "Smyrna"
                {:abbreviatons ["smy"],
                 :name "Smyrna",
                 :sc-type :diplomacy.game.sc/turkey,
                 :type :diplomacy.game.movement/land},
                "Livonia"
                {:abbreviatons ["lvn" "livo" "lvo" "lva"],
                 :name "Livonia",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/land},
                "Ankara"
                {:abbreviatons ["ank"],
                 :name "Ankara",
                 :sc-type :diplomacy.game.sc/turkey,
                 :type :diplomacy.game.movement/land},
                "Tuscany"
                {:abbreviatons ["tus"],
                 :name "Tuscany",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/land},
                "Tyrolia"
                {:abbreviatons ["tyr" "tyl" "trl"],
                 :name "Tyrolia",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/land},
                "Moscow"
                {:abbreviatons ["mos"],
                 :name "Moscow",
                 :sc-type :diplomacy.game.sc/russia,
                 :type :diplomacy.game.movement/land},
                "Warsaw"
                {:abbreviatons ["war"],
                 :name "Warsaw",
                 :sc-type :diplomacy.game.sc/russia,
                 :type :diplomacy.game.movement/land},
                "Sweden"
                {:abbreviatons ["swe"],
                 :name "Sweden",
                 :sc-type :diplomacy.game.sc/independent,
                 :type :diplomacy.game.movement/land},
                "Ukraine"
                {:abbreviatons ["ukr"],
                 :name "Ukraine",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/land},
                "Prussia"
                {:abbreviatons ["pru"],
                 :name "Prussia",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/land},
                "Belgium"
                {:abbreviatons ["bel"],
                 :name "Belgium",
                 :sc-type :diplomacy.game.sc/independent,
                 :type :diplomacy.game.movement/land},
                "Adriatic Sea"
                {:abbreviatons ["adr" "adriatic"],
                 :name "Adriatic Sea",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/water},
                "Edinburgh"
                {:abbreviatons ["edi"],
                 :name "Edinburgh",
                 :sc-type :diplomacy.game.sc/england,
                 :type :diplomacy.game.movement/land},
                "Yorkshire"
                {:abbreviatons ["yor" "york" "yonkers"],
                 :name "Yorkshire",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/land},
                "Ruhr"
                {:abbreviatons ["ruh"],
                 :name "Ruhr",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/land},
                "Burgundy"
                {:abbreviatons ["bur"],
                 :name "Burgundy",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/land},
                "Mid Atlantic Ocean"
                {:abbreviatons ["mao" "midatlanticocean" "midatlantic" "mid" "mat"],
                 :name "Mid Atlantic Ocean",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/water},
                "Syria"
                {:abbreviatons ["syr"],
                 :name "Syria",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/land},
                "Clyde"
                {:abbreviatons ["cly"],
                 :name "Clyde",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/land},
                "Constantinople"
                {:abbreviatons ["con"],
                 :name "Constantinople",
                 :sc-type :diplomacy.game.sc/turkey,
                 :type :diplomacy.game.movement/land},
                "Berlin"
                {:abbreviatons ["ber"],
                 :name "Berlin",
                 :sc-type :diplomacy.game.sc/germany,
                 :type :diplomacy.game.movement/land},
                "Rumania"
                {:abbreviatons ["rum"],
                 :name "Rumania",
                 :sc-type :diplomacy.game.sc/independent,
                 :type :diplomacy.game.movement/land},
                "Tunis"
                {:abbreviatons ["tun"],
                 :name "Tunis",
                 :sc-type :diplomacy.game.sc/independent,
                 :type :diplomacy.game.movement/land},
                "Western Mediterranean"
                {:abbreviatons ["wes" "wmed" "west" "western" "westmed" "wms" "wme"],
                 :name "Western Mediterranean",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/water},
                "Greece"
                {:abbreviatons ["gre"],
                 :name "Greece",
                 :sc-type :diplomacy.game.sc/independent,
                 :type :diplomacy.game.movement/land},
                "Wales"
                {:abbreviatons ["wal"],
                 :name "Wales",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/land},
                "Budapest"
                {:abbreviatons ["bud"],
                 :name "Budapest",
                 :sc-type :diplomacy.game.sc/austria,
                 :type :diplomacy.game.movement/land},
                "Naples"
                {:abbreviatons ["nap"],
                 :name "Naples",
                 :sc-type :diplomacy.game.sc/italy,
                 :type :diplomacy.game.movement/land},
                "Skagerrak"
                {:abbreviatons ["ska"],
                 :name "Skagerrak",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/water},
                "Piedmont"
                {:abbreviatons ["pie"],
                 :name "Piedmont",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/land},
                "Serbia"
                {:abbreviatons ["ser"],
                 :name "Serbia",
                 :sc-type :diplomacy.game.sc/independent,
                 :type :diplomacy.game.movement/land},
                "Gascony"
                {:abbreviatons ["gas"],
                 :name "Gascony",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/land},
                "Black Sea"
                {:abbreviatons ["bla" "black"],
                 :name "Black Sea",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/water},
                "Armenia"
                {:abbreviatons ["arm"],
                 :name "Armenia",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/land},
                "Gulf of Bothnia"
                {:abbreviatons ["bot" "gob" "both" "gulfofb" "bothnia"],
                 :name "Gulf of Bothnia",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/water},
                "London"
                {:abbreviatons ["lon"],
                 :name "London",
                 :sc-type :diplomacy.game.sc/england,
                 :type :diplomacy.game.movement/land},
                "Galicia"
                {:abbreviatons ["gal"],
                 :name "Galicia",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/land},
                "Silesia"
                {:abbreviatons ["sil"],
                 :name "Silesia",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/land},
                "Norway"
                {:abbreviatons ["nor" "nwy" "norw"],
                 :name "Norway",
                 :sc-type :diplomacy.game.sc/independent,
                 :type :diplomacy.game.movement/land},
                "Tyrrhenian Sea"
                {:abbreviatons ["tys" "tyrr" "tyrrhenian" "tyn" "tyh"],
                 :name "Tyrrhenian Sea",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/water},
                "Trieste"
                {:abbreviatons ["tri"],
                 :name "Trieste",
                 :sc-type :diplomacy.game.sc/austria,
                 :type :diplomacy.game.movement/land},
                "Vienna"
                {:abbreviatons ["vie"],
                 :name "Vienna",
                 :sc-type :diplomacy.game.sc/austria,
                 :type :diplomacy.game.movement/land},
                "Albania"
                {:abbreviatons ["alb"],
                 :name "Albania",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/land},
                "Liverpool"
                {:abbreviatons ["lvp" "livp" "lpl"],
                 :name "Liverpool",
                 :sc-type :diplomacy.game.sc/england,
                 :type :diplomacy.game.movement/land},
                "Marseilles"
                {:abbreviatons ["mar" "mars"],
                 :name "Marseilles",
                 :sc-type :diplomacy.game.sc/france,
                 :type :diplomacy.game.movement/land},
                "North Sea"
                {:abbreviatons ["nth" "norsea" "nts"],
                 :name "North Sea",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/water},
                "Rome"
                {:abbreviatons ["rom"],
                 :name "Rome",
                 :sc-type :diplomacy.game.sc/italy,
                 :type :diplomacy.game.movement/land},
                "Finland"
                {:abbreviatons ["fin"],
                 :name "Finland",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/land},
                "Helgoland Bight"
                {:abbreviatons ["hel" "helgoland"],
                 :name "Helgoland Bight",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/water},
                "Aegean Sea"
                {:abbreviatons ["aeg" "aegean"],
                 :name "Aegean Sea",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/water},
                "Paris"
                {:abbreviatons ["par"],
                 :name "Paris",
                 :sc-type :diplomacy.game.sc/france,
                 :type :diplomacy.game.movement/land},
                "Denmark"
                {:abbreviatons ["den"],
                 :name "Denmark",
                 :sc-type :diplomacy.game.sc/independent,
                 :type :diplomacy.game.movement/land},
                "Barents Sea"
                {:abbreviatons ["bar" "barents"],
                 :name "Barents Sea",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/water},
                "Venice"
                {:abbreviatons ["ven"],
                 :name "Venice",
                 :sc-type :diplomacy.game.sc/italy,
                 :type :diplomacy.game.movement/land},
                "English Channel"
                {:abbreviatons ["eng" "english" "channel" "ech"],
                 :name "English Channel",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/water},
                "Spain"
                {:abbreviatons ["spa"],
                 :name "Spain",
                 :sc-type :diplomacy.game.sc/independent,
                 :type :diplomacy.game.movement/land},
                "Ionian Sea"
                {:abbreviatons ["ion" "ionian"],
                 :name "Ionian Sea",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/water},
                "Irish Sea"
                {:abbreviatons ["iri" "irish"],
                 :name "Irish Sea",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/water},
                "Norwegian Sea"
                {:abbreviatons ["nwg" "norwsea" "nrg" "norwegian"],
                 :name "Norwegian Sea",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/water},
                "North Atlantic Ocean"
                {:abbreviatons ["nao" "nat"],
                 :name "North Atlantic Ocean",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/water},
                "Holland"
                {:abbreviatons ["hol"],
                 :name "Holland",
                 :sc-type :diplomacy.game.sc/independent,
                 :type :diplomacy.game.movement/land},
                "Sevastopol"
                {:abbreviatons ["sev" "sevastapol"],
                 :name "Sevastopol",
                 :sc-type :diplomacy.game.sc/russia,
                 :type :diplomacy.game.movement/land},
                "St Petersburg"
                {:abbreviatons ["stp"],
                 :name "St Petersburg",
                 :sc-type :diplomacy.game.sc/russia,
                 :type :diplomacy.game.movement/land},
                "Bulgaria"
                {:abbreviatons ["bul"],
                 :name "Bulgaria",
                 :sc-type :diplomacy.game.sc/independent,
                 :type :diplomacy.game.movement/land},
                "Picardy"
                {:abbreviatons ["pic"],
                 :name "Picardy",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/land},
                "Bohemia"
                {:abbreviatons ["boh"],
                 :name "Bohemia",
                 :sc-type :diplomacy.game.sc/na,
                 :type :diplomacy.game.movement/land}})

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
   (reduce (fn [acc [pname {:keys [type]}]]
             (assoc acc pname {:type type
                               :occupied-by ::nothing}))
           {})
   place-starting-pieces))

(defn change-types [provinces [name {:keys [type]}]]
  (let [land-type (case type
                    :w                           :diplomacy.game.movement/water
                    (:A :E :F :G :I :R :T :x :l) :diplomacy.game.movement/land)
        sc-type   (case type
                    (:l :w) :diplomacy.game.sc/na
                    :x      :diplomacy.game.sc/independent
                    :A      :diplomacy.game.sc/austria
                    :E      :diplomacy.game.sc/england
                    :F      :diplomacy.game.sc/france
                    :G      :diplomacy.game.sc/germany
                    :I      :diplomacy.game.sc/italy
                    :R      :diplomacy.game.sc/russia
                    :T      :diplomacy.game.sc/turkey)]
    (-> provinces
        (assoc-in [name :type] land-type)
        (assoc-in [name :sc-type] sc-type))))

(defn land-locked? [[name data]]
  (case name
      ("Paris"
       "Burgundy"
       "Ruhr"
       "Munich"
       "Silesia"
       "Warsaw"
       "Moscow"
       "Ukraine"
       "Tyrolia"
       "Bohemia"
       "Galicia"
       "Vienna"
       "Budapest"
       "Serbia") true
       false))

(defn coastal? [[name data :as item]]
  (and (= :diplomacy.game.movement/land
          (:type data))
       (not (land-locked? item))))

(defn mark-coastal [ps]
  (reduce (fn [ps [name data]]
            (assoc-in ps [name :type]
                      :diplomacy.game.movement/coastal))
          ps
          (filter coastal? ps)))
