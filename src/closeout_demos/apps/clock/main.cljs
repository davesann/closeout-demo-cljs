(ns closeout-demos.apps.clock.main
  (:require 
    [dsann.utils.x.core :as u]
    [dsann.cljs-utils.js :as ujs]
    
    [goog.dom :as gdom]
    [piccup.html :as ph]
    
    [closeout.core :as co]
    [closeout.state.update :as su]
    
    [closeout-demos.apps.clock.templates :as templates]
    
   ))

; call function is called to initialise the clock app
(defn go []
  (let 
    [
     ; this is the initial app-state
     app-state (atom {:time (js/Date.)})
     
     ; this is the root node that will be rendered
     ; all placeholders (see templates file) in this node will be expanded 
     ui-root   (gdom/getElement "app")     
     ]

    ; This function initialises the application.
    ; It will set up a watch for changes in the app-state and
    ; call update functions for your template nodes 
    ; when the data changes.
    (co/init! ::app app-state ui-root templates/templates)
    
    ; changing the app state evey second
    ; the use of su/assoc-in! updates the state
    ;  but also records the update path as metatdata for the update process
    ;  when you change data being monitored by closeout
    ;  you should use these functions
    ;  otherwise, the entire UI will update.
    (ujs/repeat-with-timeout
      (fn [] (su/assoc-in! app-state [:time] (js/Date.)))
      1000)
    ))


