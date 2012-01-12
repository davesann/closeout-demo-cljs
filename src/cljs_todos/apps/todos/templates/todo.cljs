(ns cljs-todos.apps.todos.templates.todo
  (:require 
    [dsann.utils.x.core :as u]
    
    [dsann.utils.state.update :as us]
    [dsann.utils.state.mirror :as usm]
    [dsann.utils.state.read-notifier :as rn]
    
    [dsann.utils.protocols.identifiable :as p-id]
    
    [dsann.cljs-utils.dom.find :as udfind]
    
    [goog.dom :as gdom]
    [goog.dom.classes :as gcls]
    [goog.events :as gevents]
    [goog.events.EventType :as et]
    [goog.dom.forms :as gforms]
    )
  )

(def static
  [:div
   [:div.display
    [:input.check.bind    {:type "checkbox" :data-bind-id "check"}]
    [:div.todo-text.bind  {:data-bind-id "todo-text"}]
    [:span.todo-destroy]]
   [:div.edit
    [:input.todo-input.bind 
     {:type "text" :value "" :data-bind-id "todo-input"}]]
   [:div.last-update "Last-update: " 
    [:span.bind {:data-bind-id "last-update"}]]
   ])

(defn update! [todo-node data-path old-app-state new-app-state]
  (let [old-data (get-in old-app-state data-path)
        new-data (get-in new-app-state data-path)
       
        done? (rn/tget-in new-data [:done?])
        desc  (rn/tget-in new-data [:desc])
        check-box (udfind/by-bind-id "check" todo-node)]
    (if done?
      (do
        (gcls/add todo-node "done")
        (gforms/setValue check-box "checked"))
      (do
        (gcls/remove todo-node "done")
        (gforms/setValue check-box)))
    (when (not= (:desc old-data) desc)
      (let [todo-text  (udfind/by-bind-id "todo-text"  todo-node)
            todo-input (udfind/by-bind-id "todo-input" todo-node)]
        (gdom/setTextContent todo-text desc)
        (gforms/setValue todo-input desc)))
    (let [n (udfind/by-bind-id "last-update" todo-node)]
      (gdom/setTextContent n (js/Date.)))
    
    todo-node))

(defn behaviour! [application dom-node context]
  (do 
    ;(u/log "add events for todo")
    (let [ui-state  (:ui-state  application)
          app-state (:app-state application)
          id        (p-id/id dom-node)]
      
      ;; checkbox clicked
      ;(u/log-str "CHECK")
      (if-let [todo-check (udfind/first-by-class "check" dom-node)]
        (gevents/listen 
          todo-check et/CLICK
          (fn [evt]
            (let [data-path (usm/get-primary-data-path @ui-state id)]
              (us/update-in! app-state (conj data-path :done?) not))))
        (u/log-str "Warning" "check")
        )
       
      ;; destroy button clicked
      ;(u/log-str "DESTROY")
      (if-let [todo-destroy (udfind/first-by-class "todo-destroy" dom-node)]
        (gevents/listen 
          todo-destroy et/CLICK
          (fn [evt]
            (let [data-path (usm/get-primary-data-path @ui-state id)
                  idx (last data-path)]
              (us/remove-by-index-in! app-state (vec (butlast data-path)) #{idx}))))
        (u/log-str "Warning" "destroy")
        )

      ;; Edit todo
      ;(u/log-str "EDIT")
      (let [todo-text  (udfind/first-by-class "todo-text" dom-node)
            todo-input (udfind/first-by-class "todo-input" dom-node)]
        (gevents/listen 
          todo-text et/DBLCLICK
          (fn [evt] 
            (gcls/add dom-node "editing")
            (. todo-input (focus))
            ))
        ; (u/log-str "Warning" "text")
        )
      
      ;; Todo input editing
      ;(u/log-str "INPUT")
      (if-let [todo-input (udfind/first-by-class "todo-input" dom-node)]
        (do
          (gevents/listen todo-input et/KEYPRESS
                          (fn [evt] (when (= (.keyCode evt) 13)
                                      (. todo-input (blur)))))
          (gevents/listen todo-input et/BLUR
                          (fn [_evt]
                            (let [data-path (usm/get-primary-data-path @ui-state id)
                                  p (conj data-path :desc)
                                  v (.value todo-input)]
                              (gcls/remove dom-node "editing")
                              (us/assoc-in?! app-state p v)))          
                          ))
        ;(u/log-str "Warning" "input")
        )
      
      ;(u/log-str "DONE")
      )))
  
  
  
  