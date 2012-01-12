(ns cljs-todos.apps.clock.templates
  (:require  
    [dsann.utils.x.core :as u]
    [closeout.core                 :as co]
    [closeout.dom.template-helpers :as th]
    
    [goog.dom :as gdom]
    
    ))


(def main-template [:div.clock-app
                    [:div.title "Clock"] 
                    [:div.placeholder {:data-template-name "clock"
                                       :data-template-bind-kw "time"}]])
  
(def clock-template [:div.clock])


(defn clock-update! [clock-node data-path old-app-state new-app-state]
  (let [t (get-in new-app-state data-path)]
    (gdom/setTextContent clock-node t)
    clock-node
    ))
 
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
  
            
      