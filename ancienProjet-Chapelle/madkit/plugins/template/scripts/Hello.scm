
;;
;; Hello.scm - A Hello agent to be used as a template for building your own scheme agents
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

(define myCommunity "myCommunity")
(define myGroup "myGroup")
(define myRole "myRole")

(define alive #t)

(define (activate)
  (println "Hello I'm an agent !")
  (let ((r (create-group #t myCommunity myGroup #!null #!null)))
	  (display "r = ")
	  (display r)
	  (if (not (equal? r 1))
		  (set! alive #f)
		  (request-role myCommunity myGroup myRole #!null)
)))

(define (live)
	(handle-message (wait-next-message))
)

(define (handle-message msg)
	(println (string-append "handling the message " msg))
	(handle-message (wait-next-message))
)

(define (end)
  (println "That's it !!! Bye ")
  (pause 2000)
)

