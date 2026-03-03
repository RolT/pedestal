(ns hp.server
  (:gen-class) ; for -main method in uberjar
  (:require [io.pedestal.connector  :as conn]
            [io.pedestal.http.jetty :as jetty]
            [hp.service :as service]))

;; This is an adapted service map, that can be started and stopped
;; From the REPL you can call server/start and server/stop on this service
(defonce runnable-service
  (-> (conn/default-connector-map 8080)
      (merge service/service)
      (conn/with-default-interceptors)
      (conn/optionally-with-dev-mode-interceptors)
      (conn/with-routes service/routes)
      (jetty/create-connector nil)))

(defn run-dev
  "The entry-point for 'lein run-dev'"
  [& args]
  (println "\nCreating your [DEV] server...")
  (conn/start! runnable-service))

(defn -main
  "The entry-point for 'lein run'"
  [& args]
  (println "\nCreating your server...")
  (conn/start! runnable-service))

;; If you package the service up as a WAR,
;; some form of the following function sections is required (for io.pedestal.servlet.ClojureVarServlet).

;;(defonce servlet  (atom nil))
;;
;;(defn servlet-init
;;  [_ config]
;;  ;; Initialize your app here.
;;  (reset! servlet  (server/servlet-init service/service nil)))
;;
;;(defn servlet-service
;;  [_ request response]
;;  (server/servlet-service @servlet request response))
;;
;;(defn servlet-destroy
;;  [_]
;;  (server/servlet-destroy @servlet)
;;  (reset! servlet nil))

