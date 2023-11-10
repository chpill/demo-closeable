(ns demo-closeable.deeply-nested)


(defn real-worldish-function [{:as _req :keys [counter]}]
  {:status 200
   :body (str "Real-worldish counter: " (swap! counter inc))})


(defn example-fn [_]
  {:status 200
   :body "Re-evaluate me if you can!"})

(defn smug-middleware [handler]
  (fn inner-function [req]
    (update (handler req)
            :body str " -- Sent from my server. I use Clojure btw.")))

(def captured-fn (smug-middleware example-fn))

(defn routes []
  [["/counter" real-worldish-function]
   ["/closure-issue" captured-fn]
   ["/alternative" {:middleware [smug-middleware]}
    ["" example-fn]]])
