(ns quil-gameoflife.core
  (:require
    [quil.core :as q]
    [quil.middleware :as m]))

; Quil Game of Life
; created 4. 7. 2021
; updated 28. 12. 2021
; updated 29. 1. 2023

(def initial-grid
  (->> (repeat 50 0)
       (repeat 50)
       (mapv vec)))

(def image-cords
  [[5 1] [5 2] [6 1] [6 2]
   [5 11] [6 11] [7 11]
   [4 12] [8 12]
   [3 13] [9 13]
   [3 14] [9 14]
   [6 15]
   [4 16] [8 16]
   [5 17] [6 17] [7 17]
   [6 18]
   [3 21] [4 21] [5 21]
   [3 22] [4 22] [5 22]
   [2 23] [6 23]
   [1 25] [2 25] [6 25] [7 25]
   [3 35] [4 35]
   [3 36] [4 36]])

(def cords
  [[-1 -1] [-1 0] [-1 1]
   [0 -1] [0 1]
   [1 -1] [1 0] [1 1]])

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :rgb)
  (reduce #(assoc-in %1 %2 1)
          initial-grid
          image-cords))

(defn alive-sum [state y x]
  (->> (for [[y-diff x-diff] cords]
         (get-in state [(+ y y-diff) (+ x x-diff)] 0))
       (reduce +)))

(defn change-cell [state y x]
  (case (alive-sum state y x)
    (0 1) 0
    2 (get-in state [y x])
    3 1
    0))

(defn update-state [state]
  (->> (for [y (range 50)]
         (for [x (range 50)]
           (change-cell state y x)))
       (mapv vec)))

(defn draw-state
  "Draw state of sketch."
  [state]
  (q/background 0)
  (q/fill 0 255 0)
  (doseq [y (range 50)
          x (range 50)]
    (if (zero? (get-in state [y x]))
      (q/fill 0 0 0)
      (q/fill 0 255 0))
    (q/rect (* x 10) (* y 10) 10 10)))

(q/defsketch
  quil-gameoflife
  :title "Game of Life"
  :size [500 500]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])