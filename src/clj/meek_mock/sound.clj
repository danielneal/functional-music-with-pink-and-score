(ns meek-mock.sound
  (:require
   [pink.engine :refer [engine-create engine-start engine-clear engine-add-afunc engine-stop engine-add-events audio-events engine-remove-afunc engine-add-post-cfunc engine-remove-post-cfunc]]
   [pink.oscillators :refer [sine blit-saw]]
   [pink.envelopes :refer [xar env]]
   [pink.util :refer [mul sum let-s]]
   [pink.filters :refer [butterlp butterhp butterbp]]
   [pink.noise :refer [white-noise]]
   [pink.event :refer [event]]
   [pink.control :refer [create-clock]]))


;; --------------------------------------
;; Starting and stopping an audio engine
;; Note:
;; I'm not using the 'pink.simple' global engine
;; in case an engine error occurs.
;; At the end of each demo, I destroy the old
;; engine and create a new one.
;; --------------------------------------

(def e (atom nil))

(defn start-engine! []
  (reset! e (engine-create :nchls 1))
  (engine-start @e))

(defn clear-engine! []
  (when @e
    (engine-clear @e)
    (engine-stop @e))
  (start-engine!))

(start-engine!)

;; --------------------------------------
;; Demo 1:
;; Sine wave
;; --------------------------------------

(defn play-sine! [f]
  (engine-add-afunc @e (sine f)))

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
  (engine-add-afunc @e (saw f)))

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
  (engine-add-afunc @e ((kit instrument))))

;; --------------------------------------
;; Demo 4:
;; Temporal recursion
;; --------------------------------------

(defn play-drum-loop! []
  (engine-add-events
   @e
   (audio-events
    @e
    (event (kit :kick) 0.0)
    (event (kit :snare) 0.5)
    (event (kit :kick) 1.0)
    (event (kit :snare) 1.5)
    (event (kit :hat) 1.75)
    (event play-drum-loop! 2.0))))

;; --------------------------------------
;; Demo 5:
;; Pattern player
;; --------------------------------------

(defn pattern-player []
  (let [n (atom 0)
        pattern (atom nil)
        tempo (atom 240)
        playing (atom false)
        trigger-fn (fn []
                     (when @playing
                       (let [length (some-> @pattern vals first count)]
                         (doseq [[inst beats] @pattern :when (beats @n)]
                           (engine-add-afunc @e ((kit inst))))
                         (swap! n #(rem (inc %) length)))))
        clock (create-clock tempo trigger-fn)]
    {:play! (fn []
              (when-not @playing
                (engine-add-post-cfunc @e clock)
                (reset! playing true)))
     :stop! (fn []
              (engine-remove-post-cfunc @e clock)
              (reset! playing false))
     :pattern pattern
     :tempo tempo}))

;; --------------------------------------
;; Demo 6:
;; Keyboard Player
;; --------------------------------------

(defn power-saw [f]
  (mul 0.33
       (sum
        (saw f)
        (saw (mul f 1.5))
        (saw (mul f 2.0)))))

(let [f 110
      key-map (into {}
                (map #(-> [(str (inc %2)) (double (* f %1))])
                     [1 17/16 9/8 6/5 4/3 3/2 8/5 5/3 7/425]
                     (range)))
      notes (atom {})]

  (defn note-on! [k]
    (let [afn (power-saw (key-map k))]
      (when-not (get @notes k)
        (engine-add-afunc @e afn)
        (swap! notes assoc k afn))))

  (defn note-off! [k]
    (when-let [afn (get @notes k)]
      (swap! notes dissoc k)
      (engine-remove-afunc @e afn))))



