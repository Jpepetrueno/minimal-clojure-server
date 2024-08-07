(ns web-dev.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [clojure.pprint :as pprint]
            [compojure.core :as comp]
            [compojure.route :as route]))

(defonce server
  (atom nil))

(comp/defroutes routes
  (comp/GET "/" [] {:status 200
                    :body "<h1>Homepage</h1>
                <ul>
                    <li><a href=\"/echo\">Echo request</a></li>
                    <li><a href=\"/greeting\">Greeting</a></li>
                </ul>"
                    :headers {"Content-type" "text/html; charset=UTF-8"}})
  (comp/ANY "/echo" req {:status 200
                         :body (with-out-str (pprint/pprint req))  ;; use the req binding
                         :headers {"Content-Type" "text/plain"}})
  (comp/GET "/greeting" [] {:status 200
                            :body "Hello, World!"
                            :headers {"Content-Type" "text/plain"}})
  (route/not-found {:status 404
                    :body "Not found."
                    :headers {"Content-Type" "text/plain"}}))

(def app
  (-> (fn [req] (routes req))
      wrap-keyword-params
      wrap-params))

(defn start-server
  []
  (reset! server
          (jetty/run-jetty (fn [req] (app req))
                           {:port 3001
                            :join? false})))

(defn stop-server
  []
  (when-some [s @server]
    (.stop s)
    (reset! server nil)))

(stop-server)

(start-server)
