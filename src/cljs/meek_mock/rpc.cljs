(ns meek-mock.rpc
  (:require-macros
    [tailrecursion.javelin :refer [defc defc=]])
  (:require
   [tailrecursion.javelin]
   [tailrecursion.castra :refer [mkremote]]))

(defc state nil)
(defc error nil)
(defc loading [])

(def play-sine! (mkremote 'meek-mock.api/play-sine! state error loading))

(def play-saw! (mkremote 'meek-mock.api/play-saw! state error loading))

(def play-kit! (mkremote 'meek-mock.api/play-kit! state error loading))

(def clear-engine! (mkremote 'meek-mock.api/clear-engine! state error loading))

