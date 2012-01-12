(ns cljs-todos.apps.todos.main
  (:require 
    [dsann.utils.x.core :as u]
    
    [goog.dom :as gdom]
    [piccup.html :as ph]
    
    [closeout.template-utils :as co-tu]
    
    [cljs-todos.apps.todos.templates :as templates]
    ;[cljs-todos.apps.count.data-changes :as dc]        
   ))

(defn go []
  (let 
    [fixtures {:todos (vec (for [i (range 7)]
                             {:id i
                              :desc (str "do something: " i)
                              :done? false
                              ;:due-date nil
                              }
                             ))
               }
     
     app-state (atom fixtures)
     ui-root   (gdom/getElement "app")
     head      (.head js/document)
     css       (first (ph/html 
                        [:link {:href "/css/g-todos.css"
                                :rel "stylesheet"
                                :type"text/css"}]))
     ]

    ;; insert required css - rather than have to change in src doc    
    (gdom/appendChild head css) 
   
    ; init the application
    (co-tu/init! ::app (u/log-str app-state) ui-root templates/templates)))


