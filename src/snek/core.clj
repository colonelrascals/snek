(ns snek.core
  (:import
   (java.awt Dimension Font)
   (javax.swing JPanel JFrame Timer JOptionPane)
   (java.awt.event ActionListener KeyListener KeyEvent))
  (:require [snek.color :as color]
            [snek.util :refer :all])
  (:gen-class))

(defn reset-game [snake blip pause]
  (dosync
   (ref-set snake (color/new-snake))
   (ref-set blip (color/new-blip-for @snake))
   (ref-set pause true))
  nil)

(defn update-dir [snake dir]
  (when dir
    (dosync (alter snake turn dir))))

(defn update-pos [snake blip]
  (dosync
   (if (eats-blip? @snake @blip)
     (do (ref-set blip (color/new-blip-for @snake))
         (alter snake move true))
     (alter snake move false)))
  nil)

;TODO BUILD THE GUI

(defn show-text [g title subtitle]
    (doto g
      (.setColor color/text-color)
      (.setFont (Font. "Tahoma" Font/TRUETYPE_FONT 30))
      (.drawString title 80 120)
      (.setFont (Font. "Tahoma" Font/TRUETYPE_FONT 12))
      (.drawString subtitle 60 150)))

(defn paint [g {:keys [body color]}]
  (doseq [[x y w h] (map screen-rect body)]
    (doto g
      (.setColor (color/vary-color color))
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
             (update-dir snake (directions (.getKeyCode e)))))
         (windowClosed []
           (System/exit 0))
         (keyReleased [e])
         (keyTyped [e])))

(defn game []
  (let [snake (ref (color/new-snake))
        blip (ref (color/new-blip-for @snake))
        level (atom 0)
        pause (ref true)
        frame (JFrame. "Snake")
        timer (Timer. (quantum @level) nil)
        panel (game-panel snake blip level pause timer)]
    (doto panel
      (.setFocusable true)
      (.addKeyListener panel)
      (.setBackground color/background-color)
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
