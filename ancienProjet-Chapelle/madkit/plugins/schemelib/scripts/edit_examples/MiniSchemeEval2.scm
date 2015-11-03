;;
;; MiniSchemeEval2.scm - a simple SchemeAgent that evaluates the content of string messages 
;;                       display the result and sends back the result to the sender of the message.
;; Copyright (C) 2000 Jacques Ferber
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



(define (activate)
  (create-group #t "scheme" #!null #!null)
  (request-role "scheme" "eval")
  (println "MiniEval agent activated")
)


(define (eval-from-string str)
   (call-with-input-string str
		(lambda(port)
			(eval (read port))
		)
	)
)

(define (write-to-string exp)
   (call-with-output-string (lambda(port)(write exp port)))
)

;; get a string message, evaluate it, print the result and 
;; send back the result to the sender as a StringMessage
(define (handle-message m)
   (let ((res ())(pr ()))
     (when (instance? m <madkit.lib.messages.StringMessage>)
		(set! res (eval-from-string (symbol->string (get-string-message-content m))))
		(set! pr (write-to-string res))
		(println (string-append ":: " pr))
		(send-message (get-message-sender m)(new-string-message pr))
)))

(define (end)
  (println "MiniEval Scheme ended"))

;; start the agent when it receives a message...
(activate)

