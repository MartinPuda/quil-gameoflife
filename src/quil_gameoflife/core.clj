(ns quil-gameoflife.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

; Quil Game of Life
; created 4. 7. 2021


; Coordinates for initial image, will be filled
;written as [y x], starting with [0 0] from left top
(def gun-cords [[5 1] [5 2] [6 1] [6 2]
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
                [3 36] [4 36]
                ])

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 30)
  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :rgb)
  ;Setup function returns initial state. It contains 50x50 matrix.
  ;Some fields are filled with 1, based on coordinates.
  {:matrix (let [m (vec (repeat 50 (vec (repeat 50 0))))]
             (loop [matrix m cords gun-cords]
               (if (empty? cords) matrix
                                  (recur (assoc-in matrix (first cords) 1) (rest cords)))))
} )

(defn change-cell
  "State of cell with coordinates [y x] is changed according to the rules.
  Args:
  m is 50x50 matrix
  y,x are coordinates to check"
  [m y x]
  (let [cell (get-in m [y x])
        coords [[-1 -1] [-1 0] [-1 1]
                [0 -1] [0 1]
                [1 -1] [1 0] [1 1]]
        alive-neigh (apply + (for [c coords]
                               (get-in m [(+ y (first c))
                                          (+ x (second c))]
                                       0)))]
    (cond (or (> 2 alive-neigh)
              (> alive-neigh 3)) 0
          (= alive-neigh 3) 1
          :else cell)))

(defn update-state
  "Each cell in matrix is updated."
  [state]
  {:matrix (->> (let [m (:matrix state)]
                  (for [y (range 50)]
                    (for [x (range 50)]
                      (change-cell m y x))))
                (map vec)
                (vec))
   })

(defn draw-state
  "Draw state of sketch."
  [state]
  (q/background 0)
  (q/fill 0 255 0)
  (let [m (:matrix state)]
    (doseq [y (range 50)]
      (doseq [x (range 50)]
        (let [cell (get-in m [y x])]
          (if (= cell 0) (q/fill 0 0 0) (q/fill 0 255 0))
          (q/rect (* x 10) (* y 10) 10 10))))))

(q/defsketch quil-gameoflife
             :title "Game of Life"
             :size [500 500]
             ; setup function called only once, during sketch initialization.
             :setup setup
             ; update-state is called on each iteration before draw-state.
             :update update-state
             :draw draw-state
             :features [:keep-on-top]
             ; This sketch uses functional-mode middleware.
             ; Check quil wiki for more info about middlewares and particularly
             ; fun-mode.
             :middleware [m/fun-mode])
