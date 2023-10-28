(ns demo-closeable.meatier-webserver
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [demo-closeable.root-handler :as handler]))

;; https://medium.com/@maciekszajna/reloaded-workflow-out-of-the-box-be6b5f38ea98

(defn closeable
  ([value] (closeable value identity))
  ([value close]
   (reify
     clojure.lang.IDeref (deref [_] value)
     java.io.Closeable (close [_] (close value)))))

(defn run-with-webserver [config f]
  (with-open [counter (closeable (atom 42))
              handler (let [h (if (:dev config) handler/make-reloading handler/make)]
                        (closeable (h @counter)))
              webserver (closeable (run-jetty @handler {:port (:port config)
                                                        :join? false})
                                   #(.stop %))]
    (f @webserver)))
