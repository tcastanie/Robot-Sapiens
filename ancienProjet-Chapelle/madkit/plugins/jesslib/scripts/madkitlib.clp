; -*- clips -*-

;; Standard library of Jess functions to interface Jess
;; You can add functions to this file and they will be loaded in whenever
;; a Jess agent starts up
;; (C) 1999-2002 Madkit Team
;; Author: J. Ferber



;; specific functions used by madkit (C) J. Ferber
(deffunction setName(?ag ?name)
   (call ?ag setName ?name))

(deffunction getName(?ag)
   (call ?ag setName))

(deffunction getAddress(?ag)
   (call ?ag getAddress))

(deffunction getAgentInformation(?ag)
   (call ?ag getAgentInformation))

(deffunction println(?s)
   (call (me) println ?s))


(deffunction defineMessageType (?x ?y)
   (defclass ?x ?y)
   (call (myController) setMessageType ?y ?x)
)

(deffunction getGUIObject ()
   (call (me) getGUIObject))

(deffunction hasGUI ()
   (call (me) hasGUI))
   
(deffunction isCommunity (?x)
   (call (me) isCommunity ?x)
)

(deffunction killAgent(?x)
	(call (me) killAgent ?x))


;; use a pause because of a (hum!!) feature of the kernel...
;; but this should have been solved...
(deffunction createJessAgent(?nom ?file)
   (bind ?ag (new "madkit.jess.JessAgent"))
   (launchAgent ?ag ?nom)
   ;(pause 100)
   (sendMessage (getAddress ?ag) (new "madkit.lib.messages.ControlMessage" "load" ?file))
)

(defineMessageType ActMessage madkit.messages.ActMessage)
(defineMessageType StringMessage madkit.kernel.StringMessage)
(defineMessageType ObjectMessage madkit.messages.ObjectMessage)
(defineMessageType KQMLMessage madkit.messages.KQMLMessage)
(defineMessageType ACLMessage madkit.messages.ACLMessage)

(deffunction sendStringMessage(?x ?s)
   (sendMessage ?x (new "madkit.kernel.StringMessage" ?s)))


(deffunction sendActMessage(?x ?act ?content)
   (bind ?mess (new "madkit.messages.ActMessage" ?act))
   (call ?mess setContent ?content)
   (sendMessage ?x ?mess) )

(deffunction sendControlMessage(?x ?act ?cont)
   (bind ?mess (new "madkit.messages.ControlMessage" ?act ?cont))
   (sendMessage ?x ?mess))


(deffunction watchOutMessages()(call (me) toggleWatchOutMessages))
(deffunction watchInMessages()(call (me) toggleWatchInMessages))
(deffunction watchRestartEngine()(call (me) toggleWatchRestartEngine))

