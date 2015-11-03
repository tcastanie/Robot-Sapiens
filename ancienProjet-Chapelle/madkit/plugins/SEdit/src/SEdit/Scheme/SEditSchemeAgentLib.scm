;; Copyright (C) 1999-2002 by the MadKit Team
;; 
;; Author: Olivier Gutknecht, Jacques Ferber <gutkneco@lirmm.fr>
;; Adaptation for StructureAgents: Jacques Ferber <ferber@lirmm.fr>
;;
;; Description: Scheme bindings for StructureAgent in SEdit
;;
;; Notes: This file should come pre-compiled, but as it is still in
;;        heavy testing and development, I prefer to keep as a Scheme
;;        source. It is also quite dependent on the StructureAgent agent. In
;;        the future the whole thing should move in a madkit.lib.agents
;;        package.

;; StringMessage
(define (new-string-message m)
 ((primitive-constructor "madkit.lib.messages.StringMessage" 
   (<String>))
  m))

(define (get-string-message-content m)
  ((primitive-virtual-method "madkit.lib.messages.StringMessage" 
			     "getString" <String> ())
   m))

(define (new-act-message m)
 ((primitive-constructor "madkit.lib.messages.ActMessage" 
   (<String>))
  m))

;; ActMessage
(define (get-act-message-action m)
  ((primitive-virtual-method "madkit.lib.messages.ActMessage" 
			     "getAction" <String> ())
   m))

(define (get-act-message-content m)
  ((primitive-virtual-method "madkit.lib.messages.ActMessage" 
			     "getContent" <String> ())
   m))

(define (set-act-message-content m s)
  ((primitive-virtual-method "madkit.lib.messages.ActMessage" 
			     "setContent" "void" (<String>))
   m s))
   
(define (set-act-message-object m s)
  ((primitive-virtual-method <madkit.lib.messages.ActMessage>
			     "setContent" <void> (<java.lang.Object>))
   m s))
   
(define (get-act-message-object m)
  ((primitive-virtual-method "madkit.lib.messages.ActMessage" 
			     "getContent" <java.lang.Object> ())
   m))

(define (set-act-message-field m f o)
  ((primitive-virtual-method "madkit.lib.messages.ActMessage" 
			     "setField" "void" (<String> "java.lang.Object"))
   m s o))

(define (get-act-message-field m f o)
  ((primitive-virtual-method "madkit.lib.messages.ActMessage" 
			     "getField" "java.lang.Object" (<String>))
   m s))



(define (get-message-sender m)
  ((primitive-virtual-method "madkit.kernel.Message" 
			     "getSender" "madkit.kernel.AgentAddress" ())
   m))

(define (get-message-receiver m)
  ((primitive-virtual-method "madkit.kernel.Message" 
			     "getReceiver" "madkit.kernel.AgentAddress" ())
   m))


(define (debug message)
  ((primitive-virtual-method "madkit.kernel.Agent" 
			     "debug" "void" (<String>))
   %current-agent%
   message))

(define (set-debug flag)
  ((primitive-virtual-method "madkit.kernel.Agent" 
			     "setDebug" "void" ("boolean"))
   %current-agent%
   flag))

(define (get-debug)
  ((primitive-virtual-method "madkit.kernel.Agent" 
			     "getDebug" "boolean" ())
   %current-agent%))

(define (get-address)   
	(invoke (as <madkit.kernel.AbstractAgent> self) 'getAddress))

(define (get-agent-information)   
	(invoke (as <madkit.kernel.AbstractAgent> self) 'getAgentInformation))

; Workaround for a Kawa Bug
;(define (get-name)   
;  ((primitive-virtual-method "madkit.kernel.AbstractAgent" 
;			     "getName" <String> ())
;   %current-agent%))

(define (get-name)   
	(invoke (as <madkit.kernel.AbstractAgent> self) 'getName))

(define (set-name name)  
	(invoke (as <madkit.kernel.AbstractAgent> self) 'setName name))

(define (pause milli)   
	(invoke (as <madkit.kernel.Agent> self) 'pause milli))

(define (print obj)
	(invoke (as <SEdit.StructureAgent> self) 'print obj))
	
(define (println obj)
	(invoke (as <SEdit.StructureAgent> self) 'println obj))
	
	
(define (kill-agent ag)
	(invoke (as <madkit.kernel.AbstractAgent> self) 'killAgent ag))

(define (messagebox-empty?)
	(invoke (as <madkit.kernel.AbstractAgent> self) 'isMessageBoxEmpty))


(define (next-message)
	(invoke (as <madkit.kernel.AbstractAgent> self) 'nextMessage))


(define (wait-next-message)
	(invoke (as <madkit.kernel.Agent> self) 'waitNextMessage))


(define (send-message address message)
  ((primitive-virtual-method "madkit.lib.agents.SchemeAgent" 
			     "sendMessage" 
			     "void" ("madkit.kernel.AgentAddress" "madkit.kernel.Message"))
   %current-agent%
   address message))

(define (send-role-message group role message)
  ((primitive-virtual-method "madkit.lib.agents.SchemeAgent" 
			     "sendMessage" "void" (<String> <String> "madkit.kernel.Message"))
   %current-agent%
   group role message))


(define (broadcast-message a1 a2 a3 . lst)
   (if (null? lst)
	(invoke (as <madkit.kernel.AbstractAgent> self) 'broadcastMessage a1 a2 a3)
	(invoke (as <madkit.kernel.AbstractAgent> self) 'broadcastMessage a1 a2 a3 (car lst))))

;(define-syntax primitive-array-new
;  (syntax-rules ()
;    ((primitive-array-new element-type)
;     (constant-fold
;      (primitive-constructor
;       <kawa.lang.PrimArrayNew> (<gnu.bytecode.Type>))
;      element-type))))

(define (make-launch-agent class name)
	(invoke (as <SEdit.StructureAgent> self) 'makeLaunchAgent class name))

(define (launch-agent ag name b)
	(invoke (as <SEdit.StructureAgent> self) 'launchAgent ag name b))
   
; usage: for a distributed group "bar" created in the community "foo" with
; no descriptor and no admission procedure:
; (create-group #t "foo" "bar" #!null #!null)
(define (create-group a1 a2 a3 a4 . lst)
   (if (null? lst)
	(invoke (as <madkit.kernel.AbstractAgent> self) 'createGroup a1 a2 a3 a4)
	(invoke (as <madkit.kernel.AbstractAgent> self) 'createGroup a1 a2 a3 a4 (car lst))))

(define (group? a1 . lst)
   (if (null? lst)
	(invoke (as <madkit.kernel.AbstractAgent> self) 'isGroup a1)
	(invoke (as <madkit.kernel.AbstractAgent> self) 'isGroup a1 (car lst))))
	
	
(define (role? a1 a2 . lst)
   (if (null? lst)
	(invoke (as <madkit.kernel.AbstractAgent> self) 'isRole a1 a2)
	(invoke (as <madkit.kernel.AbstractAgent> self) 'isRole a1 a2 (car lst))))
	
(define (community? comm)
	(invoke (as <madkit.kernel.AbstractAgent> self) 'isCommunity comm))

(define (connected-with-community? comm)
	(invoke (as <madkit.kernel.AbstractAgent> self) 'isCommunity comm))

(define (leave-group a1 . lst)
   (if (null? lst)
	(invoke (as <madkit.kernel.AbstractAgent> self) 'leaveGroup a1)
	(invoke (as <madkit.kernel.AbstractAgent> self) 'leaveGroup a1 (car lst))))
	
(define (leave-role a1 a2 . lst)
   (if (null? lst)
	(invoke (as <madkit.kernel.AbstractAgent> self) 'leaveRole a1 a2)
	(invoke (as <madkit.kernel.AbstractAgent> self) 'leaveRole a1 a2 (car lst))))

; use (request-role community group role autorization) use the new API instead (usually autorization is #!null)
(define (request-role a1 a2 . lst)
   (cond 
	((null? lst) (invoke (as <madkit.kernel.AbstractAgent> self) 'requestRole a1 a2))
	((= (length lst) 1)(invoke (as <madkit.kernel.AbstractAgent> self) 'requestRole a1 a2 (car lst)))
	((> (length lst) 1)(invoke (as <madkit.kernel.AbstractAgent> self) 'requestRole a1 a2 (car lst) (cadr lst) ))
 ))


(define (get-agents-with-role a1 a2 . lst)
   (if (null? lst)
	(invoke (as <SEdit.StructureAgent> self) 'schemeGetAgentsWithRole a1 a2)
	(invoke (as <SEdit.StructureAgent> self) 'schemeGetAgentsWithRole a1 a2 (car lst))))

(define (get-agent-with-role a1 a2 . lst)
   (if (null? lst)
	(invoke (as <SEdit.StructureAgent> self) 'getAgentsWithRole a1 a2)
	(invoke (as <SEdit.StructureAgent> self) 'getAgentsWithRole a1 a2 (car lst))))

(define (get-roles a1 . lst)
   (println "Warning: deprecated primitive : get-roles, use get-my-roles or get-existing-roles instead")
   (if (null? lst)
	(invoke (as <SEdit.StructureAgent> self) 'schemeGetRoles a1)
	(invoke (as <SEdit.StructureAgent> self) 'schemeGetRoles a1 (car lst))))
	
(define (get-existing-roles a1 . lst)
   (if (null? lst)
	(invoke (as <SEdit.StructureAgent> self) 'schemeGetExistingRoles a1)
	(invoke (as <SEdit.StructureAgent> self) 'schemeGetExistingRoles a1 (car lst))))
	
	(define (get-my-roles a1 . lst)
   (if (null? lst)
	(invoke (as <SEdit.StructureAgent> self) 'schemeGetMyRoles a1)
	(invoke (as <SEdit.StructureAgent> self) 'schemeGetMyRoles a1 (car lst))))
	
(define (get-my-groups . lst)
   (if (null? lst)
	(invoke (as <SEdit.StructureAgent> self) 'schemeGetMyGroups)
	(invoke (as <SEdit.StructureAgent> self) 'schemeGetMyGroups (car lst))))
	
(define (get-existing-groups . lst)
   (if (null? lst)
	(invoke (as <SEdit.StructureAgent> self) 'schemeGetExistingGroups)
	(invoke (as <SEdit.StructureAgent> self) 'schemeGetExistingGroups (car lst))))
	
(define (get-available-communities)
	(invoke (as <SEdit.StructureAgent> self) 'schemeGetAvailableCommunities))

(define (internal-debug message)
  ((primitive-virtual-method "SEdit.StructureAgent" 
			     "schemeDebug" "void" (<String>))
   %current-agent%
   message))

;; deprecated API
(define (join-group group)
  (println "Warning: deprecated primitive : join-group, use create-group and request-role instead")
  ((primitive-virtual-method "SEdit.StructureAgent" 
			     "joinGroup" "void" (<String>))
   %current-agent%
   group))
   
(define (found-group group)
  (println "Warning: deprecated primitive : found-group, use create-group instead")
  ((primitive-virtual-method "SEdit.StructureAgent" 
			     "foundGroup" "void" (<String>))
   %current-agent%
   group))
   
(define (get-groups . lst)  
    (println "Warning: deprecated primitive : get-groups, use get-my-groups or get-existing-groups instead")
   (if (null? lst)
	(invoke (as <SEdit.StructureAgent> self) 'schemeGetExistingGroups)
	(invoke (as <SEdit.StructureAgent> self) 'schemeGetExistingGroups (car lst))))



(define (set-recipient agent)
  ((primitive-virtual-method "SEdit.StructureAgent" 
			     "setRecipient" "void" ("madkit.kernel.AgentAddress"))
   %current-agent%
   agent))
