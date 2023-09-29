(ns demo-closeable.handler
  (:require [demo-closeable.thing :as thing]
            [reitit.core :as r]
            [reitit.ring :as rr]))

(defn routes []
  ["/api"
   ["/thing" (thing/routes)]
   ["/ping" (fn [_req]
              {:status 200
               :body "pong"})]])

(defn make []
  (rr/ring-handler (rr/router (routes))))

(defn make-reloading []
  (rr/reloading-ring-handler make))

