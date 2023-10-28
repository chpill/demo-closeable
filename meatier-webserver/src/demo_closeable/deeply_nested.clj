(ns demo-closeable.deeply-nested)


(defn real-worldish-function [{:as _req :keys [counter]}]
  {:status 200
   :body (str "Real-worldish counter: " (swap! counter inc))})

(defn routes []
  ["/counter" real-worldish-function])
