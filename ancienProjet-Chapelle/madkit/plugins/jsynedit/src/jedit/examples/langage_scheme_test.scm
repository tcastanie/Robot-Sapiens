; l'affichage
#t true text false 
#f #F 
#T
(define ecrirePositionEtat  (lambda (listeEt)
    (let* ((pi 3.14159265358979)
           (centre 230)
           (rayon 180)
           (htBoucle 50))
)))
(define test (lambda (a b)
(progn (cons 'a 'b)
 '(test begin car (3)))))

#|
 brique de commentaires
|#
(display "Chargement des fonctions Entrée Sortie d'automate")
(newline)
; AFFICHAGE d'un automate au format jflap
            
(define AF->jflap  (lambda (A fichier)
    (define ens->liste (lambda (E) 
        (cond ((vide? E) null) 
              (else (let* ((el (choix E)) (E2 (oter el E)))
                      (cons el (ens->liste E2)))))))

    (define numDeEtat (lambda (e) 
        (letrec ((ieme (lambda (e L) (if (equal? e (car L)) 1 (+ 1 (ieme e (cdr L)))))))
          (ieme e listeEtats))))

        (define ecrireTransition  (lambda (listeEt)
            (for-each 
             (lambda (et1)
               (begin (pourToutElem 
                       (lambda (et2)
                         (let* ((ensLet (mapens 
                                           (sousEnsDe (transitAF A) 
                                                      (lambda (t) (and (equal? (car t) et1)
                                                                       (equal? (caddr t) et2))))
                                           cadr))
                                (lett (choix ensLet))
                                (ensLet2 (oter lett ensLet)))
                           (begin 
                             (if (equal? lett 'epsilon) (display "null" p) (display lett p))
                             (pourToutElem
                              (lambda (l) 
                                (begin (display "," p)
                                       (if (equal? l 'epsilon) (display "null" p) (display l p))))
                              ensLet2)
                             (display " " p)
                             (display (numDeEtat et2) p)
                             (display " " p))))                    
                       (mapens (sousEnsDe (transitAF A) 
                                          (lambda (t) (equal? (car t) et1) )) caddr))
                      (display "EOL" p)
                      (newline p)))
               listeEt)))

	))

(display "Fin du chargement des fonctions Entrée Sortie d'automate")
(newline)
