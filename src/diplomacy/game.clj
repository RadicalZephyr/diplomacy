(ns diplomacy.game)

(def countries {"Switzerland" { :type :l :abbreviatons ["swi" "switz"] }
                "Adriatic Sea" { :type :w :abbreviatons ["adr" "adriatic"] }
                "Aegean Sea" { :type :w :abbreviatons ["aeg" "aegean"] }
                "Albania" { :type :l :abbreviatons ["alb"] }
                "Ankara" { :type :T :abbreviatons ["ank"] }
                "Apulia" { :type :l :abbreviatons ["apu"] }
                "Armenia" { :type :l :abbreviatons ["arm"] }
                "Baltic Sea" { :type :w :abbreviatons ["bal" "baltic"] }
                "Barents Sea" { :type :w :abbreviatons ["bar" "barents"] }
                "Belgium" { :type :x :abbreviatons ["bel"] }
                "Berlin" { :type :G :abbreviatons ["ber"] }
                "Black Sea" { :type :w :abbreviatons ["bla" "black"] }
                "Bohemia" { :type :l :abbreviatons ["boh"] }
                "Brest" { :type :F :abbreviatons ["bre"] }
                "Budapest" { :type :A :abbreviatons ["bud"] }
                "Bulgaria" { :type :x :abbreviatons ["bul"] }
                "Burgundy" { :type :l :abbreviatons ["bur"] }
                "Clyde" { :type :l :abbreviatons ["cly"] }
                "Constantinople" { :type :T :abbreviatons ["con"] }
                "Denmark" { :type :x :abbreviatons ["den"] }
                "Eastern Mediterranean" { :type :w :abbreviatons ["eas" "emed" "east" "eastern" "eastmed" "ems" "eme"] }
                "Edinburgh" { :type :E :abbreviatons ["edi"] }
                "English Channel" { :type :w :abbreviatons ["eng" "english" "channel" "ech"] }
                "Finland" { :type :l :abbreviatons ["fin"] }
                "Galicia" { :type :l :abbreviatons ["gal"] }
                "Gascony" { :type :l :abbreviatons ["gas"] }
                "Greece" { :type :x :abbreviatons ["gre"] }
                "Gulf of Lyon" { :type :w :abbreviatons ["lyo" "gol" "gulfofl" "lyon"] }
                "Gulf of Bothnia" { :type :w :abbreviatons ["bot" "gob" "both" "gulfofb" "bothnia"] }
                "Helgoland Bight" { :type :w :abbreviatons ["hel" "helgoland"] }
                "Holland" { :type :x :abbreviatons ["hol"] }
                "Ionian Sea" { :type :w :abbreviatons ["ion" "ionian"] }
                "Irish Sea" { :type :w :abbreviatons ["iri" "irish"] }
                "Kiel" { :type :G :abbreviatons ["kie"] }
                "Liverpool" { :type :E :abbreviatons ["lvp" "livp" "lpl"] }
                "Livonia" { :type :l :abbreviatons ["lvn" "livo" "lvo" "lva"] }
                "London" { :type :E :abbreviatons ["lon"] }
                "Marseilles" { :type :F :abbreviatons ["mar" "mars"] }
                "Mid Atlantic Ocean" { :type :w :abbreviatons ["mao" "midatlanticocean" "midatlantic" "mid" "mat"] }
                "Moscow" { :type :R :abbreviatons ["mos"] }
                "Munich" { :type :G :abbreviatons ["mun"] }
                "Naples" { :type :I :abbreviatons ["nap"] }
                "North Atlantic Ocean" { :type :w :abbreviatons ["nao" "nat"] }
                "North Africa" { :type :l :abbreviatons ["naf" "nora"] }
                "North Sea" { :type :w :abbreviatons ["nth" "norsea" "nts"] }
                "Norway" { :type :x :abbreviatons ["nor" "nwy" "norw"] }
                "Norwegian Sea" { :type :w :abbreviatons ["nwg" "norwsea" "nrg" "norwegian"] }
                "Paris" { :type :F :abbreviatons ["par"] }
                "Picardy" { :type :l :abbreviatons ["pic"] }
                "Piedmont" { :type :l :abbreviatons ["pie"] }
                "Portugal" { :type :x :abbreviatons ["por"] }
                "Prussia" { :type :l :abbreviatons ["pru"] }
                "Rome" { :type :I :abbreviatons ["rom"] }
                "Ruhr" { :type :l :abbreviatons ["ruh"] }
                "Rumania" { :type :x :abbreviatons ["rum"] }
                "Serbia" { :type :x :abbreviatons ["ser"] }
                "Sevastopol" { :type :R :abbreviatons ["sev" "sevastapol"] }
                "Silesia" { :type :l :abbreviatons ["sil"] }
                "Skagerrak" { :type :w :abbreviatons ["ska"] }
                "Smyrna" { :type :T :abbreviatons ["smy"] }
                "Spain" { :type :x :abbreviatons ["spa"] }
                "St Petersburg" { :type :R :abbreviatons ["stp"] }
                "Sweden" { :type :x :abbreviatons ["swe"] }
                "Syria" { :type :l :abbreviatons ["syr"] }
                "Trieste" { :type :A :abbreviatons ["tri"] }
                "Tunis" { :type :x :abbreviatons ["tun"] }
                "Tuscany" { :type :l :abbreviatons ["tus"] }
                "Tyrolia" { :type :l :abbreviatons ["tyr" "tyl" "trl"] }
                "Tyrrhenian Sea" { :type :w :abbreviatons ["tys" "tyrr" "tyrrhenian" "tyn" "tyh"] }
                "Ukraine" { :type :l :abbreviatons ["ukr"] }
                "Venice" { :type :I :abbreviatons ["ven"] }
                "Vienna" { :type :A :abbreviatons ["vie"] }
                "Wales" { :type :l :abbreviatons ["wal"] }
                "Warsaw" { :type :R :abbreviatons ["war"] }
                "Western Mediterranean" { :type :w :abbreviatons ["wes" "wmed" "west" "western" "westmed" "wms" "wme"] }
                "Yorkshire" { :type :l :abbreviatons ["yor" "york" "yonkers"] }
                }  )

(def board {})

(def abbreviations (atom {}))

(defn add-to-abbreviations [board-atm [name {abbrevs :abbreviatons}]]
  (dorun (map (fn [abbrev] (swap! board-atm assoc abbrev name)) abbrevs)))

(defn build-abbreviations []
  (dorun (map (partial add-to-abbreviations abbreviations)
              countries)))
