(ns user
  (:require [clojure.tools.namespace.repl :refer [set-refresh-dirs
                                                  refresh-all]]
            [demo-closeable.barebone-webserver :refer [run-with-webserver]]))

(set-refresh-dirs "src" "test")

(def *live-server (atom (future ::not-initialized-yet)))

(defn start! []
  (reset! *live-server (future (run-with-webserver {:port 54321}
                                                   #(.join %)))))
(defn stop! []
  (future-cancel @*live-server))

(comment (start!)
         (stop!)
         (refresh-all))
