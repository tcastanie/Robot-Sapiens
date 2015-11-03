
;;
;; Test.scm - A PingPong agent functionally :-) equivalent to the 
;;            demo.agent.PingPong Java version
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


; Sorry if the whole thing looks hacked and mixed with debug code
; but as the Scheme binding is not perfect yet, the PingPong 
; is my usual test case - Ol.

(define (find-partner agents)
  (cond ((null? agents) '())
	((equal? (car agents) (get-address)) (find-partner (cdr agents)))
	(else (car agents))))
	 
(define (get-partner i)
  (let ((other (find-partner (get-agents-with-role "ping-pong" "player"))))
    (if (null? other)
	(begin
	  (println (string-append "Trying to find a partner... " i))
	  (pause 1000)
	  (get-partner (+ i 1)))
	(begin
	    (debug (string-append "Found partner: " other 
				  " (I'm " (get-address) ")"))
	    (println (string-append "Other is:" other))
	    (send-message other (new-string-message "Bong"))
	    other))))

(define (talk other i)
  (begin 
    (println (string-append "Ok, now talking to " other i))
    (if (> i 0)
	(let 
	  ((m (wait-next-message)))
	  (begin
	    (debug (string-append "Talking... Me:" (get-address) "Him:" other))
	    (println (string-append "GEE ! My turn.."))  ; "  (get-address) ") ..."))
	    (pause 1000)
	    (send-message other (new-string-message "Balle"))
	    (talk other (- i 1)))))))
  
(define (activate)
  (display "Ping-pong Scheme agent activated")
  (set-debug #f)
  (debug (string-append "activated:" (get-address)))
  (pause 1000)
  (println "Looking for a ping-pong group...")
  (if (group? "ping-pong")
      (begin
		(println "Yeah, I join"))
      (begin
		(println "Nope, I create one")
		(create-group #t "ping-pong" #!null #!null)))
  (request-role "ping-pong" "player")
  )

(define (live)
  (talk (get-partner 0) 5))

(define (end)
  (println "PingPong Scheme ended"))

