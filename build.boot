#!/usr/bin/env boot

#tailrecursion.boot.core/version "2.5.1"

(set-env!
  :project      'meek-mock
  :version      "0.1.0-SNAPSHOT"
  :dependencies '[[tailrecursion/boot.task "2.2.4" :exclusions [org.clojure/clojure]]
                  [tailrecursion/hoplon "5.10.24" :exclusions [org.clojure/clojure]]
                  [org.clojure/clojure "1.6.0"]
                  [kunstmusik/pink "0.1.0-SNAPSHOT"]
                  [kunstmusik/score "0.2.0-SNAPSHOT"]
                  [io.hoplon.danielneal/reveal "0.1.0"]]
  :out-path     "resources/public"
  :src-paths    #{"src/hl" "src/cljs" "src/clj"})

(add-sync! (get-env :out-path) #{"assets"})

(require '[tailrecursion.hoplon.boot :refer :all]
         '[tailrecursion.castra.task :as c])

(deftask development
  "Build meek-mock for development."
  []
  (comp (watch) (hoplon {:prerender false}) (c/castra-dev-server 'meek-mock.api)))

(deftask dev-debug
  "Build meek-mock for development with source maps."
  []
  (comp (watch) (hoplon {:pretty-print true
                         :prerender false
                         :source-map true}) (c/castra-dev-server 'meek-mock.api)))

(deftask production
  "Build meek-mock for production."
  []
  (hoplon {:optimizations :advanced}))
