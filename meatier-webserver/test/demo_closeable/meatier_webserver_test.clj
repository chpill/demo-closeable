(ns demo-closeable.meatier-webserver-test
  (:require [clojure.test :refer [deftest is run-tests]]
            [demo-closeable.deeply-nested :as dn]
            [demo-closeable.meatier-webserver
             :refer [run-with-webserver]]))


(deftest test-reloading-webserver
  (let [port 12345
        url (str "http://localhost:" port
                 "/nested/deeply-nested/counter")]
    (run-with-webserver
     {:port port :dev true}
     (fn [_webserver]
       (is (= "Real-worldish counter: 43" (slurp url)))

       (let [original-function-value dn/real-worldish-function]
         (alter-var-root
          #'dn/real-worldish-function
          (fn [_f] (fn [_req] {:status 200
                               :body "LOCALLY RELOADED!"})))

         (is (= "LOCALLY RELOADED!" (slurp url)))

         (alter-var-root #'dn/real-worldish-function
                         (fn [_f] original-function-value))

         (is (= "Real-worldish counter: 44" (slurp url))))))))

(comment (run-tests))
