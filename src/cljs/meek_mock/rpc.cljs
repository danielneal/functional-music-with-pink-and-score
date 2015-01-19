(ns meek-mock.rpc
  (:require-macros
    [tailrecursion.javelin :refer [defc defc=]])
  (:require
   [tailrecursion.javelin :refer [cell]]
   [tailrecursion.castra :refer [mkremote]]))

(defc error nil)
(defc loading [])

;; -------------
;; Basic Demos
;; -------------

(def play-sine! (mkremote 'meek-mock.api/play-sine! (cell nil) error loading))

(def play-saw! (mkremote 'meek-mock.api/play-saw! (cell nil) error loading))

(def play-kit! (mkremote 'meek-mock.api/play-kit! (cell nil) error loading))

(def play-drum-loop! (mkremote 'meek-mock.api/play-drum-loop! (cell nil) error loading))

(def clear-engine! (mkremote 'meek-mock.api/clear-engine! (cell nil) error loading))

(def note-on! (mkremote 'meek-mock.api/note-on! (cell nil) error loading))

(def note-off! (mkremote 'meek-mock.api/note-off! (cell nil) error loading))

;; -------------
;; Pattern Player
;; -------------

(defc pattern {})

(def play-pattern! (mkremote 'meek-mock.api/play-pattern! pattern error loading))

(def flip! (mkremote 'meek-mock.api/flip! pattern error loading))

(def set-pattern-tempo! (mkremote 'meek-mock.api/set-pattern-tempo! pattern error loading))

(def stop-pattern! (mkremote 'meek-mock.api/stop-pattern! pattern error loading))


