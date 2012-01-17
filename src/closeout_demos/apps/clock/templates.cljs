(ns closeout-demos.apps.clock.templates
  (:require  
    [dsann.utils.x.core :as u]
    [closeout.core                 :as co]
    [closeout.dom.template-helpers :as th]
    
    [goog.dom :as gdom]
    
    ))

; this is the static html markup (hiccup syntax) for the main app.
; the template contains a placeholder
;  placeholders are replaced by looking up their template
;  in the templates config (see below)
;  In this case, the :clock template will be looked up
;  The template will bind on the data path :time
;  The bound data path is relative to the data path of the parent template
;  In this case, the parent template will be bound to []
;    so the placeholder will get [:time]
(def main-template [:div.clock-app
                    [:div.title "Clock"]
                    [:div.placeholder {:data-template-name "clock"
                                       :data-template-bind-kw "time"}]])
  
; This is the static html for the clock
(def clock-template [:div.clock])

; This function will be called when the data on the path for the clock template
; changes. It can update the existing node or create a new one
;  In this case whne called, the data bath will be bound to [:time]
;   so t is a js/Date
(defn clock-update! [clock-node data-path old-app-state new-app-state]
  (let [t (get-in new-app-state data-path)]
    (gdom/setTextContent clock-node t)
    clock-node
    ))
 

; this is the template confing for the application
; each template is named here by its key. Thesekeys can be references in placeholders.
; each template has
;  static template - hiccup syntax
;  node-updater! - a function that will manage node update
;  behaviour-fn! - a function to attach behaviour to the node

; closeout implements several helper functions for node updaters!
; In this case clock-update will be called when the bound path 
;   ([:time] for clock in this case)
;  or any sub path changes in the app-state. (i.e [:time :x :y: z])
(def templates
  {
   :main
   {:static-template main-template
    :node-updater!   nil; no updates
    :behaviour-fn!   nil; no behaviour
    }
   
   :clock
   {:static-template clock-template
    :node-updater!   (co/update-on-ANY-data-path-change clock-update!)
    :behaviour-fn!   nil; no behaviour
    }
   
   })
  
            
      