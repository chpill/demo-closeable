(ns demo-closeable.barebone-webserver
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [clojure.test :refer [deftest is run-tests]]))

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

(def live-server (future ::not-initialized-yet))

(defn start! []
  (def live-server (future (run-with-webserver {:port 54321}
                                               #(.join %)))))
(defn stop! []
  (future-cancel live-server))

(comment (start!)
         (stop!))

(deftest test-webserver
  (let [url "http://localhost:12345"]
    (is (thrown? java.net.ConnectException (slurp url)))
    (run-with-webserver {:port 12345}
                        (fn [_webserver]
                          (is (= (slurp url) "Counter: 43"))
                          (is (= (slurp url) "Counter: 44"))))
    (is (thrown? java.net.ConnectException (slurp url)))))

(comment (run-tests))
