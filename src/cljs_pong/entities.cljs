(ns cljs-pong.entities
  (:require [monet.canvas :refer [entity]]
            [cljs-pong.utils :refer [draw-rect clamp]]))

(defn- rect-entity [[scr-w scr-h] ent-w ent-h colour]
  (entity {:x 0, :y 0
           :w (* scr-w ent-w)
           :h (* scr-h ent-h)}
          nil
          (partial draw-rect colour)))

(defn bg [scr]
  (rect-entity scr 1 1 "#000000"))

(defn paddle [[scr-w scr-h :as scr] side]
  (let [paddle        (rect-entity scr 0.055 0.275 "#ffffff")
        {:keys [w h]} (:value paddle)
        center-y      (- (/ scr-h 2) (/ h 2))
        side-offset   0.045]
    (-> paddle
        (assoc-in [:value :x] (condp = side
                                :left (* scr-w side-offset)
                                :right (- scr-w (+ (* scr-w side-offset) w))))
        (assoc-in [:value :y] center-y)
        (assoc-in [:value :center-y] center-y)

        (assoc :update (condp = side
                         :left nil
                         :right (fn [event world-state ent]
                                  (let [[_ mouse-y]  (:mouse-coords world-state)
                                        [_ canvas-h] (:canvas-size world-state)
                                        paddle-h     (get-in ent [:value :h])
                                        new-y        (- mouse-y (/ paddle-h 2))]
                                    (assoc-in ent [:value :y] (clamp new-y 0 (- canvas-h paddle-h))))))))))
