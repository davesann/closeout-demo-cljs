(ns cljs-todos.main
  (:require 
    [dsann.utils.x.core :as u]
    
    ;[cljs-todos.apps.count.main :as m]
    [cljs-todos.apps.todos.main :as m]
    
   ))


(def *print-fn* u/log-str)

(m/go)


