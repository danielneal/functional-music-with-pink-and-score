(ns meek-mock.sound
  (:require
   [pink.simple :refer [start-engine stop-engine clear-engine]]
   [pink.oscillators :refer [sine blit-saw]]
   [pink.envelopes :refer [xar env]]
   [pink.util :refer [mul sum let-s]]
   [pink.filters :refer [butterlp butterhp butterbp]]
   [pink.noise :refer [white-noise]]
   [pink.event :refer [event]]))


;; --------------------------------------
;; Engine Utility Functions
;; --------------------------------------

(defn clear-engine!
  "Clear and restart the audio engine"
  []
  (clear-engine)
  (stop-engine)
  (start-engine))

;; --------------------------------------
;; Demo 1:
;; Sine wave
;; --------------------------------------

(defn play-sine! [f]
  (add-afunc (sine f)))

;; --------------------------------------
;; Demo 2:
;; Combining unit generators
;; --------------------------------------

(defn saw [f]
  (mul 0.33
       (butterlp
        (sum
         (blit-saw f)
         (blit-saw (mul f 1.01))
         (blit-saw (mul f 0.99)))
        1200)))

(defn play-saw! [f]
  (add-afunc (saw f)))

;; --------------------------------------
;; Demo 3:
;; Drum-kit
;; --------------------------------------

(def kit
  {:kick #(sum (mul (sine 60)
                    (env [0.0 2.5 0.05 0.1 0.1 0.0]))
               (mul (butterlp (white-noise) 1500)
                    (env [0.0 0.6 0.05 0.02 0.1 0.0])))

   :hat #(mul (butterhp (butterlp (white-noise) 6000) 3000)
              (env [0.0 1.0 0.3 0.0]))

   :snare #(let-s [snare (mul 3 (white-noise) (xar 0.01 0.2))]
                  (sum snare (butterbp (mul 3 snare) 100 100)))})

(defn play-kit! [instrument]
  (add-afunc ((kit instrument))))
