(ns cljs-todos.apps.count.main
  (:require 
    [dsann.utils.x.core :as u]
    
    [goog.dom :as gdom]
    [piccup.html :as ph]
    
    [closeout.template-utils :as co-tu]
    
    [cljs-todos.apps.count.templates :as templates]
    [cljs-todos.apps.count.data :as dc]        
   ))


(defn go []
  (let 
    [count-fixtures {:count {:value 0}}
     app-state (atom count-fixtures)
     ui-root (gdom/getElement "app")
     head (u/log (.head js/document))
     ]

    ;; insert required css    
    (gdom/appendChild 
      head 
      (first (ph/html 
               [:link {:href "/css/g-todos.css"
                       :rel "stylesheet"
                       :type"text/css"}])))
   
    (doseq [i (range 100)]
      (dc/update-count app-state [:count]))

    ;; clear the update status before initialisation
    (reset! app-state (with-meta @app-state {}))

    (co-tu/init! ::app app-state ui-root templates/templates)))


