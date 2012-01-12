(ns cljs-todos.apps.stopwatch.main
  (:require 
    [dsann.utils.x.core :as u]
    [dsann.cljs-utils.js :as ujs]
    
    [goog.dom :as gdom]
    [piccup.html :as ph]
    
    [closeout.core :as co]
    [closeout.state.update :as su]
    
    [cljs-todos.apps.stopwatch.templates :as templates]
    ;[cljs-todos.apps.count.data-changes :as dc]  
    
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
