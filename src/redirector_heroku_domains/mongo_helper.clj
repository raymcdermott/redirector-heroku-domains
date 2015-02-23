(ns redirector-heroku-domains.mongo-helper
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.operators :refer :all]
            [environ.core :refer [env]]))

(defn get-domain-maps-from-mongo []
  (let [mongo-uri (or (env :mongo-url) "mongodb://localhost/test")
        {:keys [db]} (mg/connect-via-uri mongo-uri)
        mongo-collection (or (env :mongo-collection) "redirections")]
    (mc/find-maps db mongo-collection)))