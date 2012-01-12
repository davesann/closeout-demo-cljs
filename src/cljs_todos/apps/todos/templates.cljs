(ns cljs-todos.apps.todos.templates
  (:require     
    [closeout.core                 :as co]
    [closeout.dom.template-helpers :as th]
    
    [cljs-todos.apps.todos.templates.todo-app   :as t-todo-app]
    [cljs-todos.apps.todos.templates.todo       :as t-todo]
    [cljs-todos.apps.todos.templates.todo-stats :as t-todo-stats]
    
    [piccup.html :as ph]
    ))

(def templates
  {
   :main
   {:static-template t-todo-app/static
    :node-updater!   nil ; no updates required
    :behaviour-fn!   t-todo-app/behaviour!
    }
   
   :todo 
   {:static-template t-todo/static
    :node-updater!   (co/update-on-ANY-data-path-change t-todo/update!)
    :behaviour-fn!   t-todo/behaviour! }
   
   :todo-stats 
   {:static-template nil
    :node-updater!   (co/update-on-ANY-data-path-change t-todo-stats/update!)
    :behaviour-fn!   t-todo-stats/behaviour! 
    }
   
   :todo-list 
   {:static-template [:ol.todo-list]
    :node-updater!   (co/template-list (th/li-template-fn :todo))
    :behaviour-fn!   nil ; no behaviour required (for the list) 
    }   
   })
  
            
      