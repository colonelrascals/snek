(ns snek.color
  (:import
   (java.awt Color Dimension Font)))

(def color-variation 35)
(def bright-sum 350)
(def bright-diff 250)
(def background-color (Color/WHITE))
(def text-color (Color/DARK_GRAY))



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
 
