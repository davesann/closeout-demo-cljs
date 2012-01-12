(ns cljs-todos.apps.clock.main
  (:require 
    [dsann.utils.x.core :as u]
    [dsann.cljs-utils.js :as ujs]
    
    [goog.dom :as gdom]
    [piccup.html :as ph]
    
    [closeout.core :as co]
    [closeout.state.update :as su]
    
    [cljs-todos.apps.clock.templates :as templates]
    ;[cljs-todos.apps.count.data-changes :as dc]  
    
   ))


(defn go []
  (let 
    [app-state (atom {:time (js/Date.)})
     ui-root   (gdom/getElement "app")
     ]

    ; init the application
    (co/init! ::app app-state ui-root templates/templates)
    
    (ujs/repeat-with-timeout
      (fn [] (su/assoc-in! app-state [:time] (js/Date.)))
      1000)
    ))


