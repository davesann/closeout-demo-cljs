(ns cljs-todos.apps.todos.templates.todo-app
  (:require
    [dsann.utils.x.core :as u]
    [dsann.utils.protocols.identifiable :as p-id]
    
    
    [closeout.state.update :as us]
    [closeout.state.mirror :as usm]
    
    [dsann.cljs-utils.dom.find :as udfind]
    [dsann.cljs-utils.dom.fx :as udfx]
    
    [goog.dom :as gdom]
    [goog.dom.classes :as gcls]
    [goog.events :as gevents]
    [goog.events.EventType :as et]
    
    )
  )


(def static
  [:div
   [:div.todoapp 
    [:div.title [:h1 "Todos"] ]
    [:div.content
     [:div.create-todo
      [:div [:input.new-todo {:placeholder "What needs to be done?" :type "text"}]]
      [:span.ui-tooltip-top {:style "display:none;"} "Press Enter to save this task"]]
     [:div.placeholder {:data-template-name "todo-stats"
                        :data-template-bind-kw "todos"    }] 
     [:div.placeholder {:data-template-name "todo-list"
                        :data-template-bind-kw "todos"    }]  
     ]
    ]
   [:div.postscript
    "This example is a close replica of this "
    [:a {:href "http://documentcloud.github.com/backbone/examples/todos/"}
     "backbone example application"]]
   ])

(defn behaviour! [application ui-element context]
  (do
    ;(u/log "add events for todo-app")
    (let [create-todo (udfind/first-by-class "create-todo" ui-element)
          new-todo    (udfind/first-by-class "new-todo" create-todo)
          tooltip     (udfind/first-by-class "ui-tooltip-top" create-todo)
          
          mirror-state  (:mirror-state  application)
          app-state (:app-state application)
          id        (p-id/id ui-element)
          ]

      ;; listen for Enter in the new todo box
      (gevents/listen 
        new-todo et/KEYPRESS 
        (fn [evt]
          (when (= (.-keyCode evt) 13)
            (let [new-todo-item {:desc (.-value new-todo) :done? false}
                  data-path (usm/get-primary-data-path @mirror-state id)]
              (us/update-in! app-state (conj data-path :todos) conj new-todo-item)
              (set! (.-value new-todo) "")
              ))))
      
      ;; listen for key up to show tooltip
      (gevents/listen 
        new-todo et/KEYUP 
        (let [timeout (atom nil)]
          (fn [evt]
            (udfx/fadeout-and-hide tooltip)
            (swap! timeout #(do (if % (js/clearTimeout %))))
            (let [v (.-value new-todo)]
              (if-not (or (= v "") (= v "placeholder"))
                (reset! timeout (js/setTimeout #(udfx/show-and-fadein tooltip) 1000)))))))
      )))
