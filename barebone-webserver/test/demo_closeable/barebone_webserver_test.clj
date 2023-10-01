(ns demo-closeable.barebone-webserver-test
  (:require [demo-closeable.barebone-webserver :refer [run-with-webserver]]
            [clojure.test :refer [deftest is run-tests]]))


(deftest test-webserver
  (let [port 12345 url (str "http://localhost:" port)]
    (is (thrown? java.net.ConnectException (slurp url)))
    (run-with-webserver {:port port}
                        (fn [_webserver]
                          (is (= (slurp url) "Counter: 43"))
                          (is (= (slurp url) "Counter: 44"))))
    (is (thrown? java.net.ConnectException (slurp url)))))

(comment (run-tests))
