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
