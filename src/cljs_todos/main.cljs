(ns cljs-todos.main
  (:require 
    [dsann.utils.x.core :as u]    
    [dsann.utils.map :as um]
    
    [cljs-todos.apps.todos.main :as todos]
    [cljs-todos.apps.clock.main :as clock]
    [cljs-todos.apps.clock2.main :as clock2]
    [cljs-todos.apps.stopwatch.main :as stopwatch]
    
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
