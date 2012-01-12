(ns cljs-todos.apps.stopwatch.data
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
    
    ))

(defn elapsed-time [start stop]
  (if-not start 
    nil
    (/ (if stop
         (- stop start)
         (- (js/Date.) start))
       1000
       )))

(defn can-lap? [stopwatch]
  (and 
    (:start-time stopwatch)
    (not (:stop-time stopwatch))))

(defn start-stop! [app-state data-path]
  (let [t (get-in @app-state data-path)
        start (:start-time t)
        stop  (:stop-time t)
        new-t (merge t
                     (cond
                       (and start stop)
                       {:start-time (js/Date.)
                        :stop-time nil
                        :lap-time  nil
                        :next-state :stop}
                       
                       start
                       {:stop-time (js/Date.)
                        :next-state :start}
                       
                       :else
                       {:start-time (js/Date.)
                        :next-state :stop}
                       ))
        ]
    (su/assoc-in! app-state data-path new-t)))

(defn lap!
  [app-state data-path]
  (let [t (get-in @app-state data-path)]
    (if (can-lap? t)
      (let [start (:start-time t)]
        (su/assoc-in! app-state (conj data-path :lap-time) 
                      (elapsed-time start (js/Date.)))))))
  
  
            
      