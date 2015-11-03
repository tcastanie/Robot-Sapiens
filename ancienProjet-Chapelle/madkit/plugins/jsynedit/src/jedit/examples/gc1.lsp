;GENERATEUR DE CODE


;on suppose que les valeurs de retour sont dans R0 


;---------------------------------------------------------------;
;                COMPTEUR POUR LES ETIQUETTES                   ;
;---------------------------------------------------------------;
;creation d'un compteur pour les etiquettes
(let ((n 0))
  (defun counter () n)
  (defun resetcounter () (setf n 0))
  (defun incrcounter () (setf n (+ 1 n))))

(resetcounter)

;---------------------------------------------------------------;
;                   CONSTANTES & VARIABLES                      ;
;---------------------------------------------------------------;
;CompileCste
(defun CompileCste (const env fenv)
  `((MOVE ($ ,const) :R0)))

;CompileVar
(defun CompileVar (var env fenv)
    (valeur-var var env 0))
   
; met dans R0 la valeur de la variable var
(defun valeur-var (var env n) ;n=niveau d'emboitement de var dans env
  (labels ((res (deca d)
		(if (= 0 d)
		    `((MOVE :FP :R2)(SUB ($ ,deca) :R2)(MOVE (* :R2) :R0))
		  `((ADD ($ 1) :FP) (MOVE (* :FP) :FP) ,@(res deca (- d 1))))))
   (if (null env)
	(error "variable ~S non définie" var)
     (if (assoc var (car env))
	 (let ((decalage (cdr (assoc var (car env)))))
	   `(,@(if (> n 0) '((PUSH :FP)))
	       ,@(res decalage n)
	       ,@(if (> n 0) '((POP :FP)) )))
       (valeur-var var (cdr env) (+ 1 n))))))


;----------------------------------------------------------------;
;                  OPERATIONS ELEMENTAIRES                       ;
;----------------------------------------------------------------;
;CompileOperateur: +,-,*,/
(defun CompileOperateur (op)
  `((POP :R0)
    (POP :R1)
    ,(cond 
      ((eq op '+) `(ADD :R1 :R0))
      ((eq op '-) `(SUB :R1 :R0))
      ((eq op '/) `(DIV :R1 :R0))
      ((eq op '*) `(MULT :R1 :R0)))))

;CompileOpBin: Operations binaires [(Op <expr> <expr>)]
(defun CompileOpBin (expr env fenv)
  `(,@(CompileExpr (caddr expr) env fenv)
      (PUSH :R0)
      ,@(CompileExpr (cadr expr) env fenv)
      (PUSH :R0)
      ,@(CompileOperateur (car expr))))

;CompileOpNaire: Operations n-aires [(Op <expr>...<expr>)]
(defun CompileOpN (expr env fenv)
  (let ((etiq_op (incrcounter))
	(etiqfin_op (incrcounter)))
    (if (eq (length expr) 3)
	`(,@(CompileOpBin expr env fenv))
      (progn
	`(,@(PushArgs (reverse (cdr expr)) 0 env fenv)
	    (POP :R2)
	    (JEQ :R2 (@ ,etiqfin_op))
	    (@ ,etiq_op)
	    (DECR :R2) 
	    (JEQ :R2 (@ ,etiqfin_op))
	    ,@(CompileOperateur (car expr))
	    (PUSH :R0)
	    (JMP (@ ,etiq_op))
	    (@ ,etiqfin_op)
	    (POP :R0))))))



;---------------------------------------------------------------;
;                            TEST                               ;
;---------------------------------------------------------------;
;CompileT: pour vrai
(defun CompileT ()
  `((MOVE ($ 1) :R0)))

;CompileF: pour faux
(defun CompileF () 
  `((MOVE ($ 0) :R0)))

;CompileNot
(defun CompileNot (expr env fenv)
  `(,@(CompileExpr (cadr expr) env fenv)
      (NOT :R0 :R0)))

;CompileTest: <, <=, >, >=, =, /= expression binaire
(defun CompileTest (expr env fenv)
  (let ((etiqvrai_test (incrcounter))
	(etiqfin_test (incrcounter)))
    `(,@(CompileExpr (cadr expr) env fenv)
	(PUSH :R0)
	,@(CompileExpr(caddr expr) env fenv)
	(PUSH :R0)
	(POP :R1)
	(POP :R0)
	(SUB :R1 :R0)    ;resultat dans R0
	(,(case (car expr)
		(= 'JEQ)
		(/= 'JNE)
		(> 'JG)
		(>= 'JGE)
		(< 'JL)
		(<= 'JLE)
		(t (error "erreur~s~%" (car expr))))
	 :R0 (@ ,etiqvrai_test))
	(MOVE ($ 0) :R0)    ;on met 0 dans R0 si c'est faux
	(JMP (@ ,etiqfin_test))
	(@ ,etiqvrai_test)
	(MOVE ($ 1) :R0)    ;on met 1 dans R0 si c'est vrai
	(@ ,etiqfin_test))))



;CompileOr  [(or <expr1> <expr2>)]
(defun CompileOr (expr env fenv)
  (if (atom (cdr expr))
      `((MOVE ($ 0) :R0))
    (let ((etiqvrai_or (incrcounter))
	  (etiqfin_or (incrcounter)))
      `(,@(CompileExpr (cadr expr) env fenv)
	  (JNE :R0 (@ ,etiqvrai_or))
	  ,@(CompileExpr (caddr expr) env fenv)
	  (JNE :R0 (@ ,etiqvrai_or))
	  (MOVE ($ 0) :R0)
	  (JMP (@ ,etiqfin_or))
	  (@ ,etiqvrai_or)
	  (MOVE ($ 1) :R0)
	  (@ ,etiqfin_or))))) 


;CompileAnd  [(and <expr1> <expr2>)]
(defun CompileAnd (expr env fenv)
  (if (atom (cdr expr))
      `((MOVE ($ 1) :R0))
    (let ((etiqfaux_and (incrcounter))
	  (etiqfin_and (incrcounter)))
      `(,@(CompileExpr (cadr expr) env fenv)
	  (JEQ :R0 (@ ,etiqfaux_and))
	  ,@(CompileExpr (caddr expr) env fenv)
	  (JEQ :R0 (@ ,etiqfaux_and))
	  (MOVE ($ 1) :R0)
	  (JMP (@ ,etiqfin_and))
	  (@ ,etiqfaux_and)
	  (MOVE ($ 0) :R0)
	  (@ ,etiqfin_and)))))



;---------------------------------------------------------------;
;                            SETF                               ;
;---------------------------------------------------------------;

; met dans R0 l'adresse de la variable var
(defun adresse-var (var env n) ;n=niveau d'emboitement de var dans env
  (labels ((res (deca d)
		(if (= 0 d)
		    `((MOVE :FP :R0) (SUB ($ ,deca) :R0))	
		  `((ADD ($ 1) :FP) (MOVE (* :FP) :FP) ,@(res deca (- d 1))))))
    (if (null env)
	(error "variable ~S non définie" var)
      (if (assoc var (car env))
	  (let ((decalage (cdr (assoc var (car env)))))
	    `(,@(if (> n 0) '((PUSH :FP)))
		,@(res decalage n)
		,@(if (> n 0) '((POP :FP)))))
	(adresse-var var (cdr env) (+ 1 n))))))


;CompileSetf
(defun CompileSetf (expr env fenv)
  (progn
    `(,@(CompileExpr (caddr expr)  env fenv)
	(PUSH :R0)
	,@(adresse-var (cadr expr) env 0)
	(POP :R1)
	(MOVE :R1 (* :R0)))))	


;---------------------------------------------------------------;
;                     LET  &  LET*                              ;
;---------------------------------------------------------------;
; transformation d'un let en lambda-expression
;(LET ((x 0)(y 1)) <corps>) -> ((LAMBDA (x y) <corps>) 0 1)
(defun translet (larg)
  (labels ((list-var (l)
		     (if (null l)
			 l
		       (if (atom (car l))
			   (cons (car l) (list-var (cdr l)))
			 (cons (caar l) (list-var (cdr l))))))
	   (list-val (l)
		     (if (null l)
			 l
		       (if (atom (car l))
			   (cons () (list-val (cdr l)))
			 (cons (cadar l) (list-val (cdr l)))))))
	  (let ((x (car larg)))
	    (cons
	     (list* 'lambda (list-var x) (cdr larg))
	     (list-val x)))))


; transformation d'un let* en lambda-expression
;(LET ((x 0)(y 1)) <corps>) -> ((LAMBDA (x) ((LAMBDA (y) <corps>) 1)) 0)
(defun translet* (larg)
  (let ((x (car larg)))
    (if (null x)
	(cdr larg)
      (if (null (cdr x))
	  (if (atom (car x))
	      (cons 
	       (list* 'lambda (list (car x)) (cdr larg))
	       (list ()))
	    (cons 
	     (list* 'lambda (list (caar x)) (cdr larg))
	     (list (cadar x))))
	(if (atom (car x))
	    (cons 
	     (list* 'lambda (list (car x))
		    (list (translet* (cons (cdr x) (cdr larg)))))
	     (list ()))
	  (cons 
	   (list* 'lambda (list (caar x))
		  (list (translet* (cons (cdr x) (cdr larg)))))
	   (list (cadar x))))))))


;----------------------------------------------------------------;
;                           LAMBDA                               ;
;----------------------------------------------------------------;
;CompileLambda
(defun CompileLambda (expr env fenv)
  `(,@(PushArgs (cdr expr) 0 env fenv)
      (MOVE :FP :R0)              
      (MOVE :SP :FP)             
      (MOVE :SP :R1)              
      (SUB ($ ,(+ (length (cadar expr)) 1)) :R1)
      (PUSH :R1)                 
      (PUSH :R0)                   
      ,@(compil-liste-expr (cddar expr)  (cons (make_env (cadar expr)) env) fenv)
      (POP :FP)                  
      (POP :SP)))

;----------------------------------------------------------------;

;macro définies
;     ((get (car expr) :macro) (compil-macro-definies expr env fenv))
; compilation des macros définies
(defun compil-macro-definies (expr env fenv)
  (CompileExpr (macroexpand expr) env fenv))

;defmacro
;     ((eq (car expr) 'defmacro) (compil-defmacro expr env fenv))
; compilation de defmacro
(defun compil-defmacro (expr env fenv)
  (eval expr)
  (setf (get (cadr expr) :macro) '(t))
  ())


;---------------------------------------------------------------;
;                  STRUCTURES DE CONTROLE                       ;
;---------------------------------------------------------------;
;CompileProgn [(progn (...) (...) ...)]
(defun CompileProgn (expr env fenv)
  (if (atom (cdr expr))
      `(,@(CompileExpr (car expr) env fenv))
    `(,@(CompileExpr (car expr) env fenv)
	,@(CompileProgn (cdr expr) env fenv))))

;CompileIf
(defun CompileIf (expr env fenv)
  (let ((etiqsinon_if (incrcounter))
	(etiqfin_if (incrcounter)))
    (if (eq 3 (length expr))         ;[(if <test> <alors> <sinon>)]
	`(,@(CompileExpr (car expr) env fenv)
	    (JEQ :R0 (@ ,etiqsinon_if))        ; si R0=0 alors test faux 
	    ,@(CompileExpr (cadr expr) env fenv)
	    (JMP (@ ,etiqfin_if))
	    (@ ,etiqsinon_if)
	    ,@(CompileExpr (caddr expr) env fenv)
	    (@ ,etiqfin_if))
      `(,@(CompileExpr (car expr) env fenv)    ;[(if <test> <alors>)]
	  (JEQ :R0 (@ ,etiqfin_if)) 
	  ,@(CompileExpr (cadr expr) env fenv)
	  (@ ,etiqfin_if)))))


;CompileCond [(cond ((...) (...)) ((...) (...)) ...)]
(defun CompileCond (exp en fen)
  (let ((etiqfin_cond (incrcounter)))
    (labels (
	     (aux (expr env fenv)
		  (if (atom (cdr expr))
		      `(,@(CompileExpr (caar expr) env fenv)  
			  (JEQ :R0 (@ ,etiqfin_cond)) 
			  ,@(CompileExpr (cadar expr) env fenv)
			  (@ ,etiqfin_cond))
		    (let ((etiqsuiv_cond (incrcounter)))
		      (progn
			`(,@(CompileExpr (caar expr) env fenv)  
			    (JEQ :R0 (@ ,etiqsuiv_cond)) 
			    ,@(CompileExpr (cadar expr) env fenv)
			    (JMP (@ ,etiqfin_cond))
			    (@ ,etiqsuiv_cond)
			    ,@(aux (cdr expr) env fenv)))))))
	    (aux exp en fen))))


;CompileWhile   [(loop while <test> do <expr>)] 
(defun CompileWhile (expr env fenv)
  (let ((etiqboucle_while (incrcounter))
	(etiqfin_while (incrcounter)))
    `((@ ,etiqboucle_while)
      ,@(CompileExpr (car expr) env fenv)
      (JEQ :R0 (@ ,etiqfin_while))
      ,@(compil-liste-expr (cddr expr) env fenv)
      (JMP (@ ,etiqboucle_while))
      (@ ,etiqfin_while))))



;---------------------------------------------------------------;
;                     QUOTE, CAR, CDR                           ;
;---------------------------------------------------------------;
(defun CompileQuote (expr env fenv)
   `((MOVE ,(cadr expr) :R0)))


(defun CompileCar (expr env fenv)
  `(,@(CompileExpr expr env fenv)            
      (CAR :R0 :R0))) 


(defun CompileCdr (expr env fenv)
  `(,@(CompileExpr expr env fenv)            
      (CDR :R0 :R0))) 



;---------------------------------------------------------------;
;                           RETURN                              ;
;---------------------------------------------------------------;
;valeur de retour
;CompileReturn  [(return ...)]
(defun CompileRnt (expr env fenv)
  `(,@(CompileExpr (car expr) env fenv)
      (HALT)))


;----------------------------------------------------------------;
;                      SOUS-PROGRAMMES                           ;
;----------------------------------------------------------------;
;DEFUN

;construire un environnement pour le defun
(defun make_env (l)
    (labels ((aux (ll nn)
		  (if (atom ll)  
		      ()		
		    (cons (cons (car ll) nn)
			  (aux (cdr ll) (- nn 1))))))
	    (aux l (+ (length l) 1))))
;(make_env '(x y z))
;((X . 4) (Y . 3) (Z . 2))


; compilation d'une liste d'expressions
(defun compil-liste-expr (lexpr env fenv)
  (if (null lexpr)
      ()
    (append (CompileExpr (car lexpr)  env fenv) (compil-liste-expr (cdr lexpr) env fenv))))


;CompileDefun     [(defun f (x y z) (...))]
(defun CompileDefun (expr env fenv)
  (let ((saut (incrcounter))) 
      `((JMP (@ ,saut))               ;sauter la definition de fonction
	(@ ,(car expr))             ;etiquette de la fontion	
	,@(compil-liste-expr (cddr expr)
			 (cons (make_env (cadr expr)) env) fenv)
	(RTN)
	(@ ,saut))))


;--------------------------------------------------------------;

;APPEL DE FONCTION
;fonction PushArgs:compile et empile les arguments d'une fonction; a la fin empile le nombre d'arguments
(defun PushArgs (xn len env fenv)
  (if (atom (cdr xn))
      `(,@(CompileExpr (car xn) env fenv)  
	  (PUSH :R0)
	  (PUSH ($ ,(+ len 1))))
      `(,@(CompileExpr (car xn) env fenv)   
	  (PUSH :R0)
	  ,@(PushArgs (cdr xn) (+ len 1) env fenv))))


;CompileAppel
(defun CompileAppel (expr env fenv)
  `(,@(PushArgs (cdr expr) 0 env fenv)    
      (MOVE :FP :R0)               ;on recupere l'ancien :FP
      (MOVE :SP :FP)              ;le nouveau FP=:SP
      (MOVE :SP :R1)               ;on recupere l'ancien SP
      (SUB ($ ,(+ (length (cdr expr)) 1)) :R1)
      (PUSH :R1)                   ;sauve l'ancien SP
      (PUSH :R0)                   ;sauve l'ancien FP
      (JSR (@ ,(car expr)))
      (POP :FP)                   ;restauration du contexte d'execution        
      (POP :SP)))




;----------------------------------------------------------------;
;                           APPLY                                ;
;----------------------------------------------------------------;

;fait une liste des expressions de exp en enlevant leur quote s'ils en ont une 
(defun enleve (exp)
  (if (atom (cdr exp))
      (if (eq 'quote (caar exp))
	  (cadar exp)
	(car exp))
    (if (listp (car exp))
	(if (eq 'quote (caar exp))
	    (cons (cadar exp) (enleve (cdr exp)))  
	  (cons (car exp) (enleve (cdr exp))))
      (cons (car exp) (enleve (cdr exp))))) )


;CompileApply  [(apply (function foo) (3 4 5))] ou [(apply (function foo)(quote(3 4 5)))]
(defun CompileApply (exp env fenv)
  (if (eq (length exp) 3)     ;un seul param et ca doit etre une liste
      (if (listp (caddr exp))
	  (CompileExpr (cons (cadadr exp) (enleve (cddr exp))) env fenv)
	(error "trop peu de parametres pour la fonction ~S" (cadadr exp)))
    ;plusieurs param et le dernier doit etre une liste: (apply (function f) '1 2 '(3))
    (if (listp (car (renverse (cddr exp))))
	(CompileExpr (cons (cadadr exp) (enleve (cddr exp))) env fenv)
      (error "trop peu de parametres pour la fonction ~S" (cadadr exp)))))




;----------------------------------------------------------------;
;                           LABELS                               ;
;----------------------------------------------------------------;
;cree une liste avec les noms des fonctions
(defun  make_env_fon (l) 
  (if (null l)
      nil
    (cons (caar l) (make_env_fon (cdr l)))))
	  

(defun CompileLabels (larg env fenv)
  (labels ((loc (l env fenv) ;compile les fonctions locales une par une a la suite
		(if (null l)
		    l
		  `((@ ,(caar l))            
		    ,@(compil-liste-expr (cddar l)
					 (cons (make_env (cadar l)) env) fenv)
		    (RTN)
		    ,@(loc (cdr l) env fenv)))))
	  (let ((etiq_labels (incrcounter)))
	    `((JMP (@ ,etiq_labels))
	      ,@(loc (car larg) env fenv)
	      (@ ,etiq_labels)
	      ,@(compil-liste-expr (cdr larg)  env  (cons (make_env_fon (car larg)) fenv))))))


;----------------------------------------------------------------;
;                     FONCTIONS PRINCIPALES                      ;
;----------------------------------------------------------------;
;CompileExpr
(defun CompileExpr (expr env fenv)
  (cond
   ((or (eq expr 't) (eq expr 'T))
    (CompileT))
   ((eq expr '())
    (CompileF))
   ((constantp expr)
    (CompileCste expr env fenv))
   ((symbolp expr)
    (CompileVar expr env fenv))
   ((and (consp expr) (eq (car expr) 'quote))
    (CompileQuote expr env fenv))
   ((eq (car expr) 'atom)
    (CompileAtom (cdr expr) env fenv))
   ((eq (car expr) 'defun)
    (CompileDefun (cdr expr) env fenv))
   ((eq (car expr) 'labels)
    (CompileLabels (cdr expr) env fenv))
   ((and (consp (car expr)) (eq (caar expr) 'lambda))
    (CompileLambda expr env fenv))
   ((eq (car expr) 'let)
    (CompileLambda (translet (cdr expr)) env fenv))
   ((eq (car expr) 'let*)
    (CompileLambda (translet* (cdr expr)) env fenv))
;defmacro
;     ((eq (car expr) 'defmacro) (compil-defmacro expr env fenv))
;macro définies
;     ((get (car expr) :macro) (compil-macro-definies expr env fenv))
   ((eq (car expr) 'setf)
    (CompileSetf expr env fenv))
   ((eq (car expr) 'setq)
    (CompileSetf expr env fenv))
   ((and (eq (car expr) 'loop) (eq (cadr expr) 'while))
    (CompileWhile (cddr expr) env fenv))
   ((eq (car expr) 'cond)
    (CompileCond (cdr expr) env fenv))
   ((eq (car expr) 'return)
    (CompileRnt (cdr expr) env fenv))
   ((and (eq (car expr) 'or) (eq (length expr) 3))
    (CompileOr expr env fenv))
   ((and (eq (car expr) 'and) (eq (length expr) 3))
    (CompileAnd expr env fenv))
   ((eq (car expr) 'car)
    (CompileCar (cdr expr) env fenv))
   ((eq (car expr) 'cdr)
    (CompileCdr (cdr expr) env fenv))
   ((and (< 2 (length expr))
	 (eq (car expr) 'apply)
	 (eq (caadr expr) 'function))
    (CompileApply expr env fenv))
   ((or (eq (car expr) '+)
	(eq (car expr) '-)
	(eq (car expr) '*)
	(eq (car expr) '/))
    (CompileOpN expr env fenv))
   ((or (eq (car expr) '>)
	(eq (car expr) '>=)
	(eq (car expr) '<)
	(eq (car expr) '<=)
	(eq (car expr) '=)
	(eq (car expr) '/=))
    (CompileTest expr env fenv))
   ((eq (car expr) 'if)
    (CompileIf (cdr expr) env fenv))
   ((eq (car expr) 'not) (CompileNot expr env fenv))
   ((eq (car expr) 'progn)
    (CompileProgn (cdr expr) env fenv))
   ;macros de lisp 
   ((macro-function (car expr)) (CompileExpr (macroexpand expr)  env fenv))
   ((symbolp (car expr))
    (CompileAppel expr env fenv))
))

;----------------------------------------------------------------;
;CodeGenere

(defun CodeGenere (expr env fenv)
;  (let ((code 
	 `(,@(CompileExpr expr env fenv)
	     (HALT))); )
;    (affiche-code code)))

(defun affiche-code (code)
  (if (not (null code))
      (progn 
	(format t " ~% ~s" (car code))
	(affiche-code (cdr code)))))

					;run_fic
(defun run_fic(nom &optional maxmem dbg)
  (progn
    (with-open-file (ifile nom 
			   :direction :input
			   :if-does-not-exist nil)
		    (CodeGenere (read ifile)()())
		    )
    )
  )




()
