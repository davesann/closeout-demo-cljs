(ns closeout-demos.main
  (:require 
    [dsann.utils.x.core :as u]    
    [dsann.utils.map :as um]
    
    [closeout-demos.apps.todos.main :as todos]
    [closeout-demos.apps.clock.main :as clock]
    [closeout-demos.apps.clock2.main :as clock2]
    [closeout-demos.apps.stopwatch.main :as stopwatch]
    
    ;; ensure that protocols are loaded
    [closeout.dom.protocols.template-binding :as ptb]
    [dsann.cljs-utils.dom.protocols.identifiable :as pid]
    [dsann.cljs-utils.dom.protocols.mutable-tree :as pmt]
    
    
    [goog.dom.dataset :as gdata]
   ))

;(def *print-fn* u/log-str)
;(u/log "Starting")

(let [apps
      {:clock     clock/go
       :clock2    clock2/go
       :stopwatch stopwatch/go
       :todos     todos/go}
      body (.-body js/document)]
  (if-let [app-choice (gdata/get body "appChoice")]
    (if-let [app-go ((keyword app-choice) apps)]
      (app-go))))
