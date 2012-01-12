(ns cljs-todos.apps.stopwatch.templates
  (:require  
    [dsann.utils.x.core :as u]
    [closeout.core                 :as co]
    [closeout.dom.template-helpers :as th]
    [closeout.state.mirror :as sm]
    [closeout.state.update :as su]
    
    [dsann.utils.protocols.identifiable :as p-id]
    
    [goog.dom :as gdom]
    [goog.dom.classes :as gcls]
    [goog.events :as gevents]
    [goog.events.EventType :as et]
    
    [dsann.cljs-utils.dom.find :as udfind]
    
    [cljs-todos.apps.stopwatch.data :as stopwatch]
    
    ))


(def main-template [:div.clock-app
                    [:div.title "Simple stopwatch"] 
                    [:div.placeholder {:data-template-name "stopwatch"
                                       :data-template-bind-kw "stopwatch"}]
                    ]
  )
  
(def stopwatch-template [:div.stopwatch
                          [:div.placeholder {:data-template-name "clock"
                                             :data-template-bind-kw "time"
                                             }]
                          [:div.hbox
                           [:div.pad1 ]
                           [:div.button.start-stop "Start"]
                           [:div.button.lap "Lap"]
                           [:div.pad1 ]
                           ]
                          
                          [:div.timing
                           [:table
                            [:tr [:td.label "start time: "]   [:td.start-time]]
                            [:tr [:td.label "stop time: "]    [:td.stop-time]]
                            [:tr [:td.label "elapsed time: "] [:td.elapsed-time]]
                            [:tr [:td.label "lap time: "]     [:td.lap-time]]]
                           ]
                          ])

(defn stopwatch-update! [node data-path old-app-state new-app-state]
  (let [t (get-in new-app-state data-path)
        start   (:start-time t)
        stop    (:stop-time t)
        elapsed (stopwatch/elapsed-time start stop)
        t (assoc t :elapsed-time elapsed)
        ]
    (doseq [k [:start-time :stop-time :elapsed-time :lap-time]]
      (let [n (udfind/first-by-class (name k) node)]
        (gdom/setTextContent n (k t))))
    
    (let [t-old (get-in old-app-state data-path)
          next-state     (:next-state t :start)
          old-next-state (:next-state t-old :start)]
      (when-not (= next-state old-next-state)
        (let [n (udfind/first-by-class "start-stop" node)
              txt (cond 
                    (= :start next-state)
                    "Start!"
                    (= :stop next-state)
                    "Stop!"
                    (= :reset next-state)
                    "Reset!")]
          (gdom/setTextContent n txt)
          (gcls/remove n (name old-next-state))
          (gcls/add n (name next-state)))))
    
    (let [n (udfind/first-by-class "lap" node)]
      (if (stopwatch/can-lap? t)
        (gcls/remove n "inactive")
        (gcls/add n "inactive")))
    node
    ))

(defn stopwatch-behaviour! [application dom-node context]
  (let [mirror-state (:mirror-state  application)
        app-state    (:app-state application)
        id           (p-id/id dom-node)]
    
      (let [n (udfind/first-by-class "start-stop" dom-node)]
        (gevents/listen 
          n et/CLICK
          (fn [evt]
            (let [data-path (sm/get-primary-data-path @mirror-state id)]
              (stopwatch/start-stop! app-state data-path)
              (.stopPropagation evt)
              false
              )))
        ;; prevent clicks - selecting stuff
        (gevents/listen 
          n et/SELECTSTART (fn [evt] (.preventDefault evt) false))
        )
      
      (let [n (udfind/first-by-class "lap" dom-node)]
        (gevents/listen 
          n et/CLICK
          (fn [evt]
            (let [data-path (sm/get-primary-data-path @mirror-state id)]
              (stopwatch/lap! app-state data-path)
              (.stopPropagation evt)
              false
              )))
        ;; prevent clicks - selecting stuff
        (gevents/listen 
          n et/SELECTSTART (fn [evt] (.preventDefault evt) false))
        )
      ))
  


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
   
   :stopwatch
   {:static-template stopwatch-template
    :node-updater!   (co/update-on-ANY-data-path-change stopwatch-update!)
    :behaviour-fn!   stopwatch-behaviour!
    }
   
   })
  
            
      