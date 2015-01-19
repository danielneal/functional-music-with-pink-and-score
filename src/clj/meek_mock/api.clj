(ns meek-mock.api
  (:require [tailrecursion.castra :refer [defrpc]]
            [meek-mock.sound :as sound]))

(defrpc play-sine! [f]
  (sound/play-sine! f)
  {})

(defrpc play-saw! [f]
  (sound/play-saw! f)
  {})

(defrpc clear-engine! []
  (sound/clear-engine!)
  {})

(defrpc play-kit! [instrument]
  (sound/play-kit! instrument)
  {})

(defrpc play-drum-loop! []
  (sound/play-drum-loop!)
  {})

(let [{:keys [play! stop! tempo pattern]} (sound/pattern-player)]
  (defrpc play-pattern! [new-pattern new-tempo]
    (reset! pattern new-pattern)
    (reset! tempo new-tempo)
    (play!)
    {:pattern @pattern :tempo @tempo})

  (defrpc stop-pattern! []
    (stop!)
    {:pattern @pattern :tempo @tempo})

  (defrpc flip! [instrument beat]
    (swap! pattern update-in [instrument beat] not)
    {:pattern @pattern :tempo @tempo})

  (defrpc set-pattern-tempo! [new-tempo]
    (reset! tempo new-tempo)
    {:pattern @pattern :tempo @tempo}))

(defrpc note-on! [k]
  (sound/note-on! k)
  {})

(defrpc note-off! [k]
  (sound/note-off! k)
  {})


