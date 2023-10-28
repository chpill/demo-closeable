(ns demo-closeable.nested
  (:require [demo-closeable.deeply-nested :as deeply-nested]))

(defn routes []
  [["/ping" (fn [_req] {:status 200
                        :body "nested pong"})]
   ["/deeply-nested" (deeply-nested/routes)]])
