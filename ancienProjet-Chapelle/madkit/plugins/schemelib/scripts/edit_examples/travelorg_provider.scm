;;
;; travelorg_provider.scm - The travel agency example in Scheme version, simplified...
;; Copyright (C) 2001 Jacques Ferber
;;
;; This program is free software; you can redistribute it and/or
;; modify it under the terms of the GNU General Public License
;; as published by the Free Software Foundation; either version 2
;; of the License, or any later version.
;;
;; This program is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU General Public License for more details.
;;
;; You should have received a copy of the GNU General Public License
;; along with this program; if not, write to the Free Software
;; Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

;; Note: Do not use it as a standard script. The "live" function is lacking.
;; This script should only be evaluated inside the SchemeEditor agent. 

;; tool functions to use ActMessages
(define (get-ActMessage-act m)
   (let ((msg :: <madkit.messages.ActMessage> m))
	(invoke msg 'getAct)
))

(define (get-ActMessage-content m)
   (let ((msg :: <madkit.messages.ActMessage> m))
	(invoke msg 'getContent)
))

(define (new-ActMessage action content)
	(make <madkit.messages.ActMessage> action content))


;; Variables used by a Provider agent

(define *competence* "plane")
(define *price* 120)

(define *last-broker* ())


;; Activate function

(define (activate)
  (create-group #t "trave" "travel-providers" #!null #!null)
  (request-role "travel" "travel-providers" "plane-provider")
  (println "I sell plane tickets")
)

;; get a string message, evaluate it, print the result and 
;; send back the result to the sender as a StringMessage
(define (handle-message m)
   (let ((res ())(pr ()))
     (if (instance? m <madkit.messages.ActMessage>)
	(let ((action (get-ActMessage-act m)))
		(println (string-append ":: received message: " action))
		(cond
		    ((string=? action "REQUEST-FOR-BID")(bid (get-message-sender m)))
		    ((string=? action "MAKE-CONTRACT")
			  (got-contract (get-ActMessage-content m)))
			((string=? action "VALIDATE") (validating-contract (get-message-sender m)))
		    (else (println "I don't understand this message"))))
		(println ":: Message unknown"))
   )
)



(define (bid ag)
	(println "received a call for bid") 
	(set! *last-broker* ag)
	(send-message ag (new-ActMessage "BID" (number->string *price*)))
)

(define (got-contract contract)
	(println (string-append "Received contract: " contract))
	(create-group #t "travel" contract #!null #!null)
	(request-role contract "service" #!null)
)

(define (validating-contract ag)
	(println "Validating contract")
	(send-message ag (new-ActMessage "ACCEPT-CONTRACT" (number->string *price*)))
)

(activate)
