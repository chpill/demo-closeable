(ns demo-closeable.meatier-webserver
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [demo-closeable.handler :as handler]))

;; https://medium.com/@maciekszajna/reloaded-workflow-out-of-the-box-be6b5f38ea98

(defn closeable
  ([value] (closeable value identity))
  ([value close]
   (reify
     clojure.lang.IDeref (deref [_] value)
     java.io.Closeable (close [_] (close value)))))

(defn run-webserver [config f]
  (with-open [counter (closeable (atom 42))
              handler (closeable
                       (if (:dev config)
                         (handler/make-reloading)
                         (handler/make))
                       #_(fn [_req]
                                   {:status 200
                                    :body (str "Counter: " (swap! @counter inc))}))
              webserver (closeable (run-jetty @handler {:port (:port config)
                                                        :join? false})
                                   #(.stop %))]
    (f @webserver)))

(def live-server (future ::not-initialized-yet))

(defn start! []
  (def live-server (future (run-webserver {:port 54321 :dev true}
                                          #(.join %)))))
(defn stop! []
  (future-cancel live-server))

(comment (start!)
         (stop!)
         (slurp "http://localhost:54321/api/thing/more/bar"))

