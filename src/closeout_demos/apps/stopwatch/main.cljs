(ns closeout-demos.apps.stopwatch.main
  (:require 
    [dsann.utils.x.core :as u]
    [dsann.cljs-utils.js :as ujs]
    
    [goog.dom :as gdom]
    [piccup.html :as ph]
    
    [closeout.core :as co]
    [closeout.state.update :as su]
    
    [closeout-demos.apps.stopwatch.templates :as templates]
    
   ))

(defn go []
  (let 
    [app-state (atom {})
     ui-root   (gdom/getElement "app")
     ]
    
    ; init the application
    (co/init! ::app app-state ui-root templates/templates)
    
    (ujs/repeat-with-timeout
      (fn [] (su/assoc-in! app-state [:stopwatch :time] (js/Date.)))
      100)
    ))
