(ns cljs-todos.apps.todos.templates
  (:require 
    
    [closeout.template-utils      :as co-tu  ]
    [closeout.template-list-utils :as co-tlu ]
    ;[cljs-todos.x.closeout.template-maplist-utils :as co-tmlu]
    
    [cljs-todos.apps.todos.templates.todo-app   :as t-todo-app]
    [cljs-todos.apps.todos.templates.todo       :as t-todo]
    [cljs-todos.apps.todos.templates.todo-stats :as t-todo-stats]
    ))

(def templates
  {
   :main
   {:render-fn     (co-tu/make-basic-render-fn t-todo-app/static)
    :init-fn!      nil ; no updates required
    :behaviour-fn! t-todo-app/behaviour!
    }
   
   :todo 
   {:render-fn     (co-tu/make-basic-render-fn t-todo/static)
    :init-fn!      (co-tu/make-init-EXACT-identified-sub-paths t-todo/update!)
    :behaviour-fn! t-todo/behaviour! }
   
   :todo-stats 
   {:render-fn     (co-tu/make-basic-render-fn t-todo-stats/static)
    :init-fn!      (co-tu/make-init-ANY t-todo-stats/update!)
    :behaviour-fn! t-todo-stats/behaviour! 
    }
   
   :todo-list 
   {:render-fn     (co-tu/make-basic-render-fn [:ol.todo-list])
    :init-fn!      (co-tlu/make-init-list :todo)
    :behaviour-fn!  nil ; no behaviour required 
    }   
   })
  
            
      