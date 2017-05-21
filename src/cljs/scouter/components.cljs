(ns scouter.components)

(defn webTitle [message]
  [:h1 message])
(defn subWebTitle [message]
  [:h5 message])
(defn subPage [message]
  [:h6 message])
(defn fieldShenanigeins [message]
  [:h8 message])


(defn button [value change key]
  [:button {:on-click #(swap! value (fn [v] (assoc-in v [key] (+ (js/Number change) (js/Number (:score v))))))}
   change])

(defn number-input [value key]
  [:input {:type "number"
           :value (key @value)
           :on-change #(reset! value (assoc-in @value [key] (-> % .-target .-value)))}])
(defn text-input [value key]
  [:input {:type "text"
           :value (key @value)
           :on-change #(reset! value (assoc-in @value [key] (-> % .-target .-value)))}])

(defn score-component [value before after key]
  [:div.numberinput
   (map #(button value % key) before)
   [number-input value key]
   (map #(button value % key) after)])
(defn graph-component [id] [:div {:class graph :id id}])


(defn page-component [] ;Erika Stuff
  [
   [webTitle "Thirst Robotics Competition"]
   [subWebTitle (str "Team " (:team @state))]
   [:p "Username"]
   [:p Event] ;pull down bar, accessing BA data of Events for xxx team
   [:hr]
   [subWebTitle "2018 ~ waterGate"]
   [:p "match number: " ] ;add num box
   [:p "team you're scouting: "] ;num box
   [:p "type of Scouting"] ;multiple choose (pit/field/meeting)
   [:hr]
   [subPage "Pit"]
   [:p"Drive Train: "];txt box
   [:p"Speed: "];num box
   [:p "prefered starting spot"] ;pull down red 1-3, blue 1-3, be able to choose two?
   [:p "auto type"] ;per yr labeled by com1-com6, nxt to this pull down of # of com robot can do
   [:p "com1: ______ com4:_______"] ;num input on each blank
   [:p "com2: ______ com5:_______"]
   [:p "com3: ______ com6:_______"]
   [:p "Other scouting notes: "] ;txt box, 3 lines? if we wanna have fun
   [:hr]
   [subPage "Field"]
   [fieldShenanigeins "Auto"]
   [:p "Baseline: "] ;yes/no
   [:p "com1"] ;num box, the com need to change per yr on the thing... how should we make a method for that???
   [:p "com2"] ;num box
   [fieldShenanigeins "Teleopt"]
   [:p "com1"]
   [:p "com2"]
   [:p "com3"]
   [fieldShenanigeins "End Game"]
   [:p "Climb"] ;yes/no box
   [:hr]
   [subPage "Scouting Meeting"]
   [fieldShenanigeins ""]])
