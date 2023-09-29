(ns demo-closeable.nested-thing)

(defn routes []
  ["/bar" (fn [_req]
            {:status 200
             :body "bar"})])
