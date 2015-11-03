(defrule EvalStringMessage "Evaluate the content of a StringMessage"
	?m <- (StringMessage (sender ?sender)(string ?cont))
	=>
	(retract ?m)
	(printout t "=s=> " (eval ?cont) crlf)
)
