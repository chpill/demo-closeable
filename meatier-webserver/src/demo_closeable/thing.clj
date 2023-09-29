(ns demo-closeable.thing
  (:require [demo-closeable.nested-thing :as nested-thing]))

(defn routes []
  [["/ping" (fn [_req]
              {:status 200
               :body "thing ping"})]
   ["/more" (nested-thing/routes)]])
