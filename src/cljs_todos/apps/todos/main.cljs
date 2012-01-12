(ns cljs-todos.apps.todos.main
  (:require 
    [dsann.utils.x.core :as u]
    
    [goog.dom :as gdom]
    [piccup.html :as ph]
    
    [closeout.core :as co]

    [cljs-todos.apps.todos.templates :as templates]
    ;[cljs-todos.apps.count.data-changes :as dc]        
   ))

(defn go []
  (let 
    [fixtures {:todos (vec (for [i (range 2)]
                             {:id i
                              :desc (str "do something: " i)
                              :done? false
                              ;:due-date nil
                              }
                             ))
               }
     
     app-state (atom fixtures)
     ui-root   (gdom/getElement "app")
     ]   
    ; init the application
    (co/init! ::app app-state ui-root templates/templates)))


