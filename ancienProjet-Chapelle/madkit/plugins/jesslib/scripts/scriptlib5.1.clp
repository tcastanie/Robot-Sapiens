; -*- clips -*-

;; Jess standard script library
;; You can add functions to this file and they will be loaded in whenever
;; Jess starts up
;; (C) 1997 E.J.Friedman-Hill and Sandia National Laboratories

(deffunction show-deftemplates ()
  "Print deftemplates using Java reflection"
  (bind ?__e (call (engine) listDeftemplates))
  (while (call ?__e hasMoreElements)    
    (printout t crlf (call (call ?__e nextElement) toString) crlf)))

(deffunction show-jess-listeners ()
  "Print JessListeners using Java reflection"
  (bind ?__e (call (engine) listJessListeners))
  (while (call ?__e hasMoreElements)    
    (printout t crlf (call (call ?__e nextElement) toString) crlf)))

(deffunction show-deffacts ()
  "Print deffacts using Java reflection"
  (bind ?__e (call (engine) listDeffacts))
  (while (call ?__e hasMoreElements)    
    (printout t crlf (call (call ?__e nextElement) toString) crlf)))

(deffunction ppdefrule (?__rule)
  "Pretty-print a Jess rule"
  (bind ?__rule (call (engine) findDefrule ?__rule))
  (printout t (call ?__rule toString) crlf))

(deffunction ppdeffunction (?__function)
  "Pretty-print a Jess function"
  (bind ?__function (call (engine) findUserfunction ?__function))
  (printout t (call ?__function toString) crlf))

(deffunction ppdefglobal (?__global)
  "Pretty-print a Jess global"
  (bind ?__global (call (engine) findDefglobal ?__global))
  (printout t (call ?__global toString) crlf))

(deffunction ppdeftemplate (?__template)
  "Pretty-print a Jess template"
  (bind ?__template (call (engine) findDeftemplate ?__template))
  (printout t (call ?__template toString) crlf))

(deffunction facts ()
  "List all facts on the fact-list"
  (bind ?__e (call (engine) listFacts))
  (bind ?__fi 0)
  (while (call ?__e hasMoreElements)
    (bind ?__fact (call ?__e nextElement))
    (printout t "f-" (call ?__fact getFactId)
              "   " (call ?__fact toString) crlf)
    (bind ?__fi (+ ?__fi 1)))
  (printout t "For a total of " ?__fi " facts." crlf))

(deffunction rules ()
  "List all rules"
  (bind ?__e (call (engine) listDefrules))
  (bind ?__i 0)
  (while (call ?__e hasMoreElements)
    (bind ?__r (call ?__e nextElement))
    (printout t (call ?__r getName) crlf)
    (bind ?__i (+ ?__i 1)))
  (printout t "For a total of " ?__i " rules." crlf))

(deffunction fact-slot-value (?__fact-id ?__name)
  "Fetch the value from the named slot of the given fact"
  (bind ?__fact (call (engine) findFactByID ?__fact-id))
  (return (call ?__fact getSlotValue ?__name)))

(deffunction run-until-halt ()
  "Run until halt is called."
  (call (engine) runUntilHalt))

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
   (bind ?ag (new "madkit.models.jess.JessAgent"))
   (launchAgent ?ag ?nom)
   ;(pause 100)
   (sendMessage (getAddress ?ag) (new "madkit.lib.messages.ControlMessage" "load" ?file))
)

(defineMessageType ActMessage madkit.lib.messages.ActMessage)
(defineMessageType StringMessage madkit.lib.messages.StringMessage)
(defineMessageType ObjectMessage madkit.lib.messages.ObjectMessage)
(defineMessageType KQMLMessage madkit.lib.messages.KQMLMessage)
(defineMessageType ACLMessage madkit.lib.messages.ACLMessage)

(deffunction sendStringMessage(?x ?s)
   (sendMessage ?x (new "madkit.lib.messages.StringMessage" ?s)))


(deffunction sendActMessage(?x ?act ?content)
   (bind ?mess (new "madkit.lib.messages.ActMessage" ?act))
   (call ?mess setContent ?content)
   (sendMessage ?x ?mess) )

(deffunction sendControlMessage(?x ?act ?cont)
   (bind ?mess (new "madkit.lib.messages.ControlMessage" ?act ?cont))
   (sendMessage ?x ?mess))


(deffunction watchOutMessages()(call (me) toggleWatchOutMessages))
(deffunction watchInMessages()(call (me) toggleWatchInMessages))
(deffunction watchRestartEngine()(call (me) toggleWatchRestartEngine))

