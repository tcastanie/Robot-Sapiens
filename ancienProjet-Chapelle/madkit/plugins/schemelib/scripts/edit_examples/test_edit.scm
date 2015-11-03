;;
;; test_edit.scm - Test of a script using the SchemeEditor agent.
;; Copyright (C) 2002 Jacques Ferber
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

(define *group* "toto")
(define *role* "eval")

(define (activate)
	(create-group #t *group* #!null #!null)
	(request-role *group* *role* #!null)
)

(define ed (get-agent-with-role *group* "editor"))

; (send-message ed (new-string-message "bonjour a toi"))
;; print received messages...
(define (handle-message m)
   (let ((res ())(pr ()))
     (cond
	 	((instance? m <madkit.kernel.StringMessage>)
			(set! res (get-string-message-content m))
			(println (string-append ":: " res)) )
		((instance? m <madkit.messages.ActMessage>)
			(set! res (invoke m 'getAct))
			(println (string-append ":: " res)) )
)))
