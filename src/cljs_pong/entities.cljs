(ns cljs-pong.entities
  (:require [monet.canvas :refer [entity]]
            [cljs-pong.utils :refer [draw-rect]]))

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
        (assoc-in [:value :center-y] center-y))))
