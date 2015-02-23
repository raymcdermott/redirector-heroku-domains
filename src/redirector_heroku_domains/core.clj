(ns redirector-heroku-domains.core
  (:require [redirector-heroku-domains.mongo-helper :as mongo]
            [org.httpkit.client :as http]
            [clojure.string :as str]
            [cheshire.core :refer :all]
            [environ.core :refer [env]]))

(def heroku-api-endpoint "https://api.heroku.com")

(def api-token (or (env :heroku-api-token)
                   (throw (Exception. (str "You must set HEROKU_API_TOKEN")))))

(def app (or (env :heroku-app)
             (throw (Exception. (str "You must set HEROKU_APP")))))

(def default-heroku-options {:timeout     30000             ; ms -- 30 seconds
                             :oauth-token api-token
                             :headers     {"Accept: application/vnd.heroku+json; version=3"
                                           "Content-type: application/json"}})

(defn post-heroku-data
  "General function to POST data to the Heroku platform API"
  ([path json-data]
    (post-heroku-data path json-data nil))

  ([path json-data options]
    {:pre [(:oauth-token default-heroku-options)]}
    (let [options (merge {:form-params json-data} (merge options default-heroku-options))
          {:keys [body error]} @(http/post (str heroku-api-endpoint path) options)]
      (if error (throw (Exception. (str "Failed, exception: " error)))
                (parse-string body true)))))

(defn get-domains []
  "Obtains the domains from Mongo"
  (if-let [domain-maps (mongo/get-domain-maps-from-mongo)]
    domain-maps))

(defn set-domain [domain]
  (post-heroku-data (str "/apps/" app "/domains") {"hostname" domain}))

(defn set-domains [domains]
  (map #(set-domain %) domains))

(defn -main []
  (let [domains (get-domains)]
    (map #(set-domains (get-in % [:source :domain])) domains)))


