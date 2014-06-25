(ns cljs-pong.core
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [dommy.macros :refer [sel1]])
  (:require [cljs-pong.utils :refer [set-on-load! connect-repl client->canvas-coords]]
            [cljs-pong.entities :as entities]
            [big-bang.core :refer [big-bang!]]
            [big-bang.events.browser :refer [client-coords]]
            [monet.canvas :refer [get-context]]
            [dommy.core :as dom]))

(enable-console-print!)

(defn update-state
  "Update each entity if it has an update fn."
  [event world-state]
  (update-in world-state [:entities]
             #(mapv (fn [ent]
                      (if-let [{:keys [update]} ent]
                        (update event world-state ent)
                        ent))
                    %)))

(defn update-mouse
  "Canvas mouse-move handler. Updates the mouse coords in the world state."
  [canvas event world-state]
  (assoc world-state
    :mouse-coords (client->canvas-coords canvas (client-coords event))))

(defn render-scene
  "Loop over the entities calling each ones draw fn."
  [ctx {:keys [entities]}]
  (doseq [ent entities]
    (when-let [{:keys [value draw]} ent]
      (draw ctx value))))

(set-on-load!
 (fn []
   (connect-repl 9001)
   (let [canvas      (sel1 :#pong)
         canvas-size (vec (map (partial dom/attr canvas) [:width :height]))
         ctx         (get-context canvas "2d")]
     (go (big-bang!
          :initial-state {:canvas-size canvas-size
                          :mouse-coords [0 0]
                          :entities [(entities/bg canvas-size)
                                     (entities/paddle canvas-size :left)
                                     (entities/paddle canvas-size :right)]}
          :event-target canvas
          :on-tick update-state
          :on-mousemove (partial update-mouse canvas)
          :to-draw (partial render-scene ctx))))))
