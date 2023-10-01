(ns demo-closeable.barebone-webserver
  (:require [ring.adapter.jetty :refer [run-jetty]]))

;; https://medium.com/@maciekszajna/reloaded-workflow-out-of-the-box-be6b5f38ea98

(defn closeable
  ([value] (closeable value identity))
  ([value close]
   (reify
     clojure.lang.IDeref (deref [_] value)
     java.io.Closeable (close [_] (close value)))))

(defn run-with-webserver [config f]
  (with-open [counter (closeable (atom 42))
              handler (closeable (fn [_req]
                                   {:status 200
                                    :body (str "Counter: " (swap! @counter inc))}))
              webserver (closeable (run-jetty @handler {:port (:port config)
                                                        :join? false})
                                   #(.stop %))]
    (f @webserver)))

(comment (run-with-webserver {:port 54321}
                             (fn [_webserver]
                               (println "The server is live:"
                                        (slurp "http://localhost:54321"))))
         (run-with-webserver {:port 54321}
                             #(.join %)))

