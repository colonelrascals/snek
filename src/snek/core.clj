(ns snek.core
  (:import
   (java.awt Color Dimension)
   (javax.swing JPanel JFrame Timer JOptionPane)
   (java.awt.event ActionListener KeyListener KeyEvent)))

;-----------------------
;helpers
;-----------------------
(defmacro def-with-docs [name docstring value]
  `(def ~(with-meta name {:doc docstring}) ~value))
;------------------------
; constants
;------------------------

(def width 50)
(def height 30)
(def point-size 15)
(def turn-millis 100)
(def win-length 8)
(def-with-docs directions "Using Java KeyEvent Class. VK indicates virtual key codes. See https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyEvent.html for more info"
  {KeyEvent/VK_LEFT [-1 0]
   KeyEvent/VK_RIGHT [1 0]
   KeyEvent/VK_UP [0 -1]
   KeyEvent/VK_DOWN [0 1]})

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
