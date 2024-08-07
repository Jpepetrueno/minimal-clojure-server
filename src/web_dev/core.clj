(ns web-dev.core
  (:require [ring.adapter.jetty :as jetty]
            [aleph.http :as aleph]))

(defonce server (atom nil))

(defn app
  [req]
  {:status 200 :body "Greetings, human!" :headers {}})

(defn start-server
  []
  (reset! server
          #_(aleph/start-server (fn [req] (app req)) {:port 3001})
          (jetty/run-jetty (fn [req] (app req))
                             {:port 3001
                              :join? false})))

(defn stop-server
  []
  (when-some [s @server]
    ;; (.close s)
    (.stop s)
    (reset! server nil)))

(stop-server)

(start-server)
