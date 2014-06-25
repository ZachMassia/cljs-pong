(ns cljs-pong.utils
  (:require [weasel.repl :as repl]
            [dommy.attrs :refer [attr]]
            [monet.canvas :as canvas]))

(defn set-on-load!
  "Set the window's onload function."
  [f]
  (set! (.-onload js/window) f))

(defn connect-repl
  "Connect to a repl if not already connected."
  [port]
  (when-not (repl/alive?)
    (repl/connect (str "ws://localhost:" port))))

;;
;; Canvas helpers ---------------------------------------------------------
;;
(defn draw-rect [colour ctx dim]
  (-> ctx
      (canvas/fill-style colour)
      (canvas/fill-rect dim)))

(defn client->canvas-coords
  "Transforms mouse coordinates from client space to canvas space.
Does not perform bounds checking."
  [canvas [x y]]
  (let [rect (.getBoundingClientRect canvas)
        left (.-left rect)
        top  (.-top rect)]
    [(- x left) (- y top)]))

(defn center-on [[x y] {:keys [w h] :as obj}]
  "Center obj on point (x, y)."
  (-> obj
      (assoc :x (- x (/ w 2)))
      (assoc :y (- y (/ h 2)))))

;;
;; Vector operations ------------------------------------------------------
;;
(defn clamp [x min-val max-val]
  (max min-val (min max-val x)))

(defn normalize [[x y]]
  (let [length (.sqrt js/Math (+ (* x x) (* y y)))]
    (if (pos? length)
      [(/ x length) (/ y length)]
      [x y])))
