(ns user
  (:require [demo-closeable.barebone-webserver :refer [run-with-webserver]]))


(defonce *live-server (atom (future ::not-initialized-yet)))

(defn start! []
  (reset! *live-server (future (run-with-webserver {:port 54321}
                                                   #(.join %)))))
(defn stop! []
  (future-cancel @*live-server))

(comment (start!)
         (stop!))
