(ns hp.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [ring.middleware.session.cookie :as cookie]
            [ring.util.response :as ring-resp]))

(defn about-page
  [request]
  (ring-resp/response (format "Clojure %s - served from %s"
                              (clojure-version)
                              (route/url-for ::about-page))))

(defn home-page
  [request]
  (ring-resp/response "Hello World!"))

(def routes
  ;; Defines "/" and "/about" routes with their associated :get handlers.
  #{["/" :get home-page]
    ["/about" :get [(body-params/body-params) http/html-body about-page]]})

;; Consumed by hp.server/create-server
;; See http/default-interceptors for additional options you can configure
(def service {:env :prod
              ;; You can bring your own non-default interceptors. Make
              ;; sure you include routing and set it up right for
              ;; dev-mode. If you do, many other keys for configuring
              ;; default interceptors will be ignored.
              ;; :interceptors []

              ;; Uncomment next line to enable CORS support, add
              ;; string(s) specifying scheme, host and port for
              ;; allowed source(s):
              ;;
              ;; "http://localhost:8080"
              ;;
              ;;:allowed-origins ["scheme://host:port"]

              ;; Root for resource interceptor that is available by default.
              :resource-path "/public"

              ;; Either :jetty or :tomcat (see comments in project.clj)
              :type :jetty
              ;;:host "localhost"
              :port 8080
              ;:enable-session {:cookie-name "PEDEX"
              ;                 :store (cookie/cookie-store)}
              ;:enable-csrf {}
              :container-options {:h2c? true
                                        :h2? true
                                        :ssl? true
                                        :ssl-port 8443
                                        :keystore "test/hp/keystore.jks"
                                        :key-password "password"}})

