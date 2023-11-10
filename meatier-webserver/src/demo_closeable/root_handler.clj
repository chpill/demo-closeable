(ns demo-closeable.root-handler
  (:require [demo-closeable.nested :as nested]
            [reitit.ring :as rr]))


(defn complete-routes []
  [""
   ["/nested" (nested/routes)]
   ["/ping" (fn [_req] {:status 200
                        :body "pong"})]])

(defn inject-counter [counter]
  (fn [handler]
    (fn inject-counter-middleware [req]
      (handler (assoc req :counter counter)))))

(defn make [counter]
  (rr/ring-handler (rr/router (complete-routes))
                   (rr/create-default-handler
                    {:not-found (constantly
                                 {:status 404
                                  :body "Real-worldish 404 page"})})
                   {:middleware [(inject-counter counter)]}))

(defn make-reloading [& args]
  (rr/reloading-ring-handler #(apply make args)))
