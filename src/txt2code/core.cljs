(ns txt2code.core
  (:require
   [reagent.core :as reagent :refer [atom]]))

(def blockly (js* "Blockly"))
(def blockly-js (js* "Blockly.JavaScript"))
(def blockly-events (js* "Blockly.Events"))
(def blockly-xml (js* "Blockly.Xml"))
(def blockly-blocks (js* "Blockly.Blocks"))

(def workspace (reagent/atom {}))

(defn get-e [s]
  (js/document.getElementById s))

(defn text->dom
  [xml-str]
  (.textToDom blockly-xml xml-str))

(defn append-dom->workspace
  [dom workspace]
  (.appendDomToWorkspace blockly-xml dom workspace))

(defn get-element-by-id
  [ename]
  (.getElementById js/document "blockly"))

(defn dom->workspace
  [saved-blocks workspace]
  (let [xml (.textToDom blockly-xml saved-blocks)]
    (.domToWorkspace blockly-xml xml workspace)))

(defn block->dom
  [block]
  (.blockToDom blockly-xml block))

(def blockly-style {:height 600
                    :width "100%"})

(defn generate-js-button []
  [:button {:id "generate-js"
            :type "button"
            :on-click #(let [code (.workspaceToCode blockly-js @workspace)]
                         (if (empty? code)
                           (js/alert "请输入blockly代码(*@ο@*) 哇～")
                           (js/alert code)))}
   "生成JavaScript"])

(defn blockly-semantic-input-method []
  [:form {:class "form", :novalidate ""}
   [:div {:class "form-group row no-label"}
    [:label {:class "control-label col-sm-3", :data-required "false", :for "frc-q-1088"}]
    [:div {:class "col-sm-9"}
     [:input {:type "text", :class "input", :aria-label "搜索", :name "q", :placeholder "输入文本生成代码", :id "frc-q-1088", :value ""}]
     [:span {:class "help-block"
             :style {:font-size "25px"}}
      "Txt2Code"]
     (generate-js-button)]]])

(defn ui
  []
  [:div
   [:div#menu {:style {:position "relative"
                       :top 0
                       :height 100
                       :width "100%"}}
    [:center (blockly-semantic-input-method)]]
   [:div#blocklyDiv {:style blockly-style}]])

(defn start []
  (js/console.log "start...")
  (reagent/render
   [ui]
   (get-e "app"))
  (reset! workspace
          (.inject blockly
                   (get-e "blocklyDiv")
                   (clj->js {:toolbox (get-e "toolbox")}))))

(defn ^:export init []
  (start))

(defn stop []
  (js/console.log "stop..."))
