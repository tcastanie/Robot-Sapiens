;; reflect functions

(deffunction createAgent(?class ?nom)
   (bind ?ag (new ?class))
   (launchAgent ?ag ?nom))


;(createAgent "madkit.jess.EditJessAgent" titi)
;(createAgent "madkit.jess.SchemeAgent" chose)
;(createAgent "madkit.demos.PingPong" chose)