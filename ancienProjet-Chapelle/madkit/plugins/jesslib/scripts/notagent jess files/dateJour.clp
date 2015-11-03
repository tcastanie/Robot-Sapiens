;;
;; dateJour.clp - A Jess example which does not include agents. 
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

;; Note: this example computes the date of tomorrow, given the date of today.

(defrule init ""
 =>
  	(printout t "On veut calculer la date du lendemain" crlf)
  	(printout t "en tenant compte des annees bissextiles" crlf)
    (assert (dateJour 31 1 1996))
    (assert (dateJour 28 2 1994))
	(assert (dateJour 31 12 1999))
    (assert (mois 1 31))
    (assert (mois 2 29))
    (assert (mois 3 31))
    (assert (mois 4 30))
    (assert (mois 5 31))
    (assert (mois 6 30))
    (assert (mois 7 31))
    (assert (mois 8 31))
    (assert (mois 9 30))
    (assert (mois 10 31))
    (assert (mois 11 30))
    (assert (mois 12 31))
    (assert (bissextile 1994))
    (assert (bissextile 1998))
)


(defrule standard ""
     (dateJour ?j ?m ?a)
     (mois ?m ?n&:(< ?j ?n))
     (not (dateJour 28 2 ?a))
	=>
     (assert (dateLendemain (+ ?j 1) ?m ?a))
     (printout t "date du lendemain du " ?j "/" ?m "/" ?a " = " (+ ?j 1) "/" ?m "/" ?a crlf)
)

(defrule finDeMois ""
     (dateJour ?j ?m ?a)
     (mois ?m&~12 ?j)
  =>
     (assert (dateLendemain 1 (+ ?m 1) ?a))
     (printout t "date du lendemain du " ?j "/" ?m "/" ?a " = " 1 "/" (+ ?m 1) "/" ?a crlf)
)

(defrule finAnnee ""
   (dateJour 31 12 ?a)
    =>
     (assert (dateLendemain 1 1 (+ ?a 1)))
     (printout t "date du lendemain du 32/12/" ?a " = 1/1/" (+ ?a 1) crlf)
)

(defrule 28FevrierA ""
     (dateJour 28 2 ?a)
     (bissextile ?a)
  =>
     (assert (dateLendemain 29 2 ?a))
     (printout t "date du lendemain du 28/2/" ?a " = 29/2/" ?a crlf)
)

(defrule 28FevrierB ""
     (dateJour 28 2 ?a)
     (not (bissextile ?a))
  =>
     (assert (dateLendemain 1 3 ?a))
     (printout t "date du lendemain du 28/2/" ?a " = 1/3/" ?a crlf)
)

(defglobal ?*time* = (time))
(set-reset-globals FALSE)
;(reset)
;(run)
(printout t "Elapsed time: " (- (time) ?*time*) crlf)