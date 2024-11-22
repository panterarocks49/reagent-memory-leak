(ns mem-leak.core
  (:require
   [reagent.core :as r]
   [reagent.dom :as d]))

;; -------------------------
;; Views

(defn create-large-object []
  (into-array
   (mapv
    (fn [i]
      {:id i
       :s  (str i)})
    (range 0 500000))))

(defn leak-memeory-sub-comp [props]
  [:div "I'm leaking"])

(defn leak-memory-comp []
  (let [large-object (create-large-object)]
    (fn []
      [leak-memeory-sub-comp {:large-object large-object}])))

(defn memory-leak-main []
  (let [*focused? (r/atom false)]
    (fn []
      [:div
       {:style {:padding-top "24px"
                :text-align "center"
                :font-size "36px"}}
       [:div
        ;; on-mouse-down causes it to leak memory from the sub components argv
        {:on-mouse-down #(swap! *focused? not)}
        ;; but on-click does not
        ;; {:on-click #(swap! *focused? not)}
        (if @*focused?
          [leak-memory-comp]
          [:div "click me!"])]])))

;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [memory-leak-main] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
