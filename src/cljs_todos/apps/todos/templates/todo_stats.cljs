(ns cljs-todos.apps.todos.templates.todo-stats
  (:require
    [dsann.utils.x.core :as u]
    [closeout.state.update :as us]
    [closeout.state.mirror :as usm]
    
    [dsann.utils.protocols.identifiable :as p-id]
    
    [dsann.cljs-utils.dom.find :as udfind]
    
    [piccup.html :as ph]
    
    [goog.dom :as gdom]
    [goog.dom.classes :as gcls]
    [goog.events :as gevents]
    [goog.events.EventType :as et]
    
    [goog.dom.dataset :as gdata]
    
    
    )
  )

;; helper function
(defn todo-stats [todos-list]
  (let [t (count todos-list)
        d (count (filter :done? todos-list))
        r (- t d)]
    {:total t
     :done  d
     :remaining r}))


(defn render [data]
  (let [{:keys [total remaining done]} data]
    [:div.todo-stats-box
     [:div.todo-stats
      (remove nil?
              (if (> total 0)
                (list
                  [:span.todo-count
                   [:span.number (str remaining " ")]
                   [:span.word (if (= 1 remaining) "item" "items")] " left."]
                  (if (> done 0)
                    [:span.todo-clear
                     [:a {:href "#"}
                      "Clear " [:span.number-done (str done)] " completed " 
                      [:span.word-done (if (= done 1) "item" "items")]]]))))
      ]
     (cond
       (>= remaining 15)
       [:div.comment [:em "Its going to be a very busy day :!"]]
       (>= remaining 7)
       [:div.comment "Its going to be a busy day..."]
       )
     [:div.last-update "Last-update: " [:span (str (js/Date.))]]
     ]))


;; no static template - the node is re-rendered on every update.
(defn update! [ui-element data-path old-app-state new-app-state]
  (let [new-data (get-in new-app-state data-path)
        new-node (first (ph/html (render (todo-stats new-data))))]
    new-node))

(defn behaviour! [application dom-node context]
  (do
    ;(u/log "add events for todo-stats")
    (let [mirror-state  (:mirror-state  application)
          app-state (:app-state application)
          id        (p-id/id dom-node)
          ]
      
      ;; clear todos
      (if-let [todo-clear (udfind/first-by-class "todo-clear" dom-node)]
        (gevents/listen 
          todo-clear et/CLICK 
          (fn [evt]
            (let [data-path (usm/get-primary-data-path @mirror-state id)]
              (us/update-in! app-state data-path #(vec (remove :done? %))))))
        ;(u/log-str "Warning" "todo-clear")
        )
      ))
  )

