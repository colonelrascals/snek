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



(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
