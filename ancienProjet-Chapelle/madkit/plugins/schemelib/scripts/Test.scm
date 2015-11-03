;;
;; Test.scm - a small test of Scheme, with a lot of different functions
;; Copyright (C) 2002 Olivier Gutknecht
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



(define (activate)
  (create-group #t "scheme" #!null #!null)
  (request-role "scheme" "test" #!null)
  (println "Test agent activated")
)

(define (terminate-process 10)
   (if (> n 0)
   	  (begin
   	  	(println (append-string "terminating... " (number->string n)))
   	  	(pause 2000)
   	  	(terminate-process (- n 1))
)))


(define (live)
	(test))

(define (test)
  (debug "should not be printed")
  (set-debug #t)
  (debug "It is deep magic ?")
  (println "My own agent-address is:")
  (println (get-address))
  (println "My own name is:")
  (println (get-name))
  (set-name "Foobar")
  (println "My own name is now:")
  (println (get-name))
  (pause 500)

  (println "My own name is now:")
  (println (get-name))
  (println (messagebox-empty?))
  (println (get-agent-information))
  (println "is gabu a group ?")
  (println (group? "gabu"))
  (println "founding group gabu")
  (create-group #t "gabu" #!null #!null)
  (println "is gabu a group ?")
  (println (group? "gabu"))
  (println "requesting roles zo & meu")
  (request-role "gabu" "zo" #!null)
  (request-role "gabu" "meu" #!null)
  (println "what roles in gabu")
  (println (get-roles "gabu"))
  (println "what agents with role meu")
  (println (get-agents-with-role "gabu" "zo"))
  (send-role-message "gabu" "zo" (new-string-message "Gee?"))
  (println (messagebox-empty?))
  (define mess (next-message))
  (println mess)
  (println (get-message-sender mess))
  (println (get-message-receiver mess))
  (println (get-string-message-content mess))
  (define reply 
    (new-string-message (string-append 
		     (get-string-message-content mess) 
		     "-end")))
  (send-message (get-message-sender mess) reply)
  (println (get-string-message-content (wait-next-message)))
  ;;(make-launch-agent "madkit.system.EditorAgent" "Editor")
  ;;(terminate-process 5)
)
