;;
;; MiniSchemeEval.scm - a simple SchemeAgent that evaluates the content of string messages 
;; and display the results.
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
;;
;; Note: Should be used together with an EditorAgent to give it the strings to evaluate.


(define (activate)
  (create-group #t "scheme" #!null #!null)
  (request-role "scheme" "eval" #!null)
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

(define (live)
  (evaluate-string-messages))

(define (evaluate-string-messages)
    (let ((m (wait-next-message))
		  (res ()))
		(when (instance? m <madkit.kernel.StringMessage>)
			(set! res (eval-from-string (symbol->string (get-string-message-content m))))
		    (println (string-append "::" (write-to-string res)))
		))
	(evaluate-string-messages)
)

		

(define (end)
  (println "MiniEval Scheme ended"))
