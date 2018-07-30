(ns snek.util
  (:import
   (java.awt.event KeyListener KeyEvent))
  (:require [snek.color :refer [c-width c-height]]))

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

(def length (memoize length))

(defn add-points [[x0 y0] [x1 y1]]
"Shifts the head of the snake using the two points"
[(+ x0 x1) (+ y0 y1)])

(defn move [{:keys [body dir] :as snake} grows]
  "Evals the snake after one move"
  (assoc snake :body
         (cons (add-points (first body) dir)
               (if grows body (butlast body)))))

(defn turn [snake dir]
  (assoc snake :dir dir))

(defn win? [{body :body} level]
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

(def screen-rect (memoize screen-rect))

