(ns snek.core
  (:import
   (java.awt Color Dimension)
   (javax.swing JPanel JFrame Timer JOptionPane)
   (java.awt.event ActionListener KeyListener KeyEvent))
  (:gen-class))

;-----------------------
;helpers
;-----------------------

(defmacro def-with-docs [name docstring value]
  `(def ~(with-meta name {:doc docstring}) ~value))

;------------------------
;constants
;  i -- initial
;  d -- duration
;  c -- court
;  p -- panel
;
;------------------------

(def c-width 50)
(def c-height 30)
(def point-size 15)
(def i-quantum 100)
(def d-quantum -5)
(def m-quantum 50)
(def i-length 5)
(def d-length 3)
(def p-width (* c-width point-size))
(def p-height (* c-height point-size))
(def-with-docs directions "Using Java KeyEvent Class. VK indicates virtual key codes. See https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyEvent.html for more info"
  {KeyEvent/VK_LEFT [-1 0]
   KeyEvent/VK_RIGHT [1 0]
   KeyEvent/VK_UP [0 -1]
   KeyEvent/VK_DOWN [0 1]})

(defn quantum [level]
  (max (+ i-length (* level d-length)) m-quantum))

(defn length [level]
  (+ i-length (* level d-length)))

(def length (memorize length))

(defn add-points [[x0 y0] [x1 y1]])
"Shifts the head of the snake using the two points"
[(+ x0 x1) (+ y0 y1)])

(defn move [{:keys [body dir] :as snake} grows]
  "Evals the snake after one move"
  (assoc snake :body
         (cons (add-points (first body) dir)
               (if grows body (butlast body)))))

(defn turn [snake dir]
  (assoc snake :dir dir))

(defn win? [{body :body level}]
  (>= (count body) (length level)))

(defn eats-self? [[head & tail]]
  (contains? (set tail) head))

(defn eats-boarder? [[[x y]]]
  (or (>= x c-width)
      (>= y c-height)
      (< x 0)
      (< y 0)))

(defn lose? [{body :body}]
  (or (eats-self? body)
      (eats-boarder? body)))

(defn eats-blip? [{[head] :body} {[apple] :body}]
  (= head apple))

(defn screen-rect [[x y]]
  (map (fn [x] (* point-size x))
       [x y 1 1]))

(def screen-rect (memorize screen-rect))

;TODO - Create new NS for below

(defn ->color [[r g b]]
  (Color. r g b))

(defn bright-color []
  (->> (repeatedly #(rand-int 256))
       (partition 3 1)
       (some #(when (= (apply + %) bright-sum) %))))

(defn contrast? [x y]
  (letfn [(diff [x y] (Math/abs (- x y)))]
    (if (>= (apply + (map diff x y))
            bright-diff)
      y nil)))

(defn contrast-color [color]
  (->> (repeatedly bright-color)
       (some (partial contrast? color))))

9(defn vary-component [x]
   (letfn [(pm [x] [(rand-int x) (rand-int (- x))])]
     (let [x (apply + x (pm color-variation))]
       (cond (> x 255) 255
             (< x 0) 0
             :else x))))

(defn vary-color [color]
  (->color (map vary-component color)))

(defn new-snake []
  {:body (list [1 1])
   :dir [1 0]
   :color (bright-color)})

(defn new-blip-for [{color :color}]
  {:body [[(rand-int c-width)
           (rand-int c-height)]]
   :color (contrast-color color)})
                                        ;
;TODO - Seperate to new NS

(defn reset-game [snake blip pause]
  (dosync
   (ref-set snake (new-snake))
   (ref-set blip (new-blip-for @snake))
   (ref-set pause true))
  nil)

(defn update-dir [snake dir]
  (when dir
    (dosync (alter snake turn dir))))

(defn update-pos [snake blip]
  (dosync
   (if (eats-blip? @snake @blip)
     (do (ref-set blip (new-blip-for @snake))
         (alter snake move true))
     (alter snake move false)))
  nil)

;TODO BUILD THE GUI

(defn show-text [g title subtitle]
    (doto g
      (.setColor text-color)
      (.setFont (Font. "Tahoma" Font/TRUETYPE_FONT 30))
      (.drawString title 80 120)
      (.setFont (Font. "Tahoma" Font/TRUETYPE_FONT 12))
      (.drawString subtitle 60 150)))

(defn paint [g {:keys [body color]}]
  (doseq [[x y w h] (map screen-rect body)]
    (doto g
      (.setColor (vary-color color))
      (.fillRect x y w h))))

(defn game-panel [snake blip level pause timer]
  (proxy [JPanel ActionListener KeyListener] []
         (paintComponent [g]
                         (proxy-super paintComponent g)
                         (if @pause
                           (show-text g (str "Level " @level)
                                      "Press any key to continue...")
                           (do (paint g @snake)
                               (paint g @blip))))
         (actionPerformed [e]
           (when-not @pause
             (update-pos snake blip))
           (when (lose? @snake)
             (reset-game snake blip pause))
           (when (win? @snake @level)
             (swap! level inc)
             (reset-game snake blip pause)
             (.setDelay timer (quantum @level)))
           (.repaint this))
         (keyPressed [e]
           (if @pause
             (dosync (ref-set pause false))
             (update-dir snake (dirs (.getKeyCode e)))))
         (windowClosed []
           (System/exit 0))
         (keyReleased [e])
         (keyTyped [e])))

(defn game []
  (let [snke (ref (new-snake))
        blip (ref (new-blip-for @snake))
        level (atom 0)
        pause (ref true)
        frame (JFrame. "Snake")
        timer (Timer. (quantum @level) nil)
        panel (game-panel snake blip level pause timer)]
    (doto panel
      (.setFocusable true)
      (.addKeyListener panel)
      (.setBackground background-color)
      (.setPreferredSize (Dimension. p-width p-height)))
    (doto frame
      (.add panel)
      (.pack)
      (.setVisible true)
      (.setResizable false)
      (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
      (.setLocationRelativeTo nil))
    (doto timer
      (.addActionListener panel)
      (.start))
    [snake blip level timer]))

(defn -main
  "This is where I will launch the game."
  [& args]
  (game))
