;;
;; fact.clp - A Jess example which computes the factorial number..
;;
;; Copyright (C) 1999-2002 Jacques Ferber
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

;; Note: this example does not include any agent


(deffacts Facto
    (fact 9)
    (fac 0 1)
)

(defrule Descente ""
     (fact ?n&:(> ?n 0))
 =>
     (assert (fact (- ?n 1)))
)

(defrule Montee ""
     ?f <- (fact ?n)
     (fac ?n-1&:(= ?n-1 (- ?n 1)) ?f-1)
  =>
     (printout t " factorielle de " ?n " =  " (* ?n ?f-1) crlf)
     (retract ?f)
     (assert (fac ?n (* ?n ?f-1)))
)

(defglobal ?*time* = (time))
(set-reset-globals FALSE)
(reset)
; (watch rules)
(run)
;(printout t "Elapsed time: " (- (time) ?*time*) crlf)
