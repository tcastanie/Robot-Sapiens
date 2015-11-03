CLS
LSel = 9
DefX = 640
DefY = 480
DefFile$ = ""
bclgen:
LOCATE 1, 1
PRINT "   Raccourcis clavier   "
PRINT "-======= Menu =========-"
PRINT "       -======-         "
PRINT "   J‚r“me CHAPELLE      "
PRINT : PRINT
PRINT "Choix (tapez le chiffre, puis Return)"
LOCATE 9
COLOR 7, 0
PRINT "------------------------------"
IF LSel = 1 THEN COLOR 4, 1 ELSE COLOR 7, 0
PRINT " 1 - Cr‚ation d'environnement"
IF LSel = 2 THEN COLOR 4, 1 ELSE COLOR 7, 0
PRINT " 2 - Le probleme du puis      "
IF LSel = 3 THEN COLOR 4, 1 ELSE COLOR 7, 0
PRINT " 3 - Transfert des .env .sha  "
IF LSel = 4 THEN COLOR 4, 1 ELSE COLOR 7, 0
PRINT " 4 - Lancement Run G-Box      "

IF LSel = 0 THEN COLOR 4, 1 ELSE COLOR 7, 0
PRINT
PRINT " 0 - Quitter                  "
COLOR 7, 0
PRINT "------------------------------"

bclink:
r$ = INKEY$: IF r$ = "" THEN GOTO bclink:

IF r$ = "1" THEN LSel = 1
IF r$ = "2" THEN LSel = 2
IF r$ = "3" THEN LSel = 3
IF r$ = "4" THEN LSel = 4
IF r$ = "5" THEN LSel = 5
IF r$ = "6" THEN LSel = 6
IF r$ = "7" THEN LSel = 7
IF r$ = "0" THEN LSel = 0
IF r$ = CHR$(13) AND LSel = 1 THEN GOSUB CreaEnv
IF r$ = CHR$(13) AND LSel = 2 THEN GOSUB PbPuis
IF r$ = CHR$(13) AND LSel = 3 THEN GOSUB Transfert
IF r$ = CHR$(13) AND LSel = 4 THEN GOSUB RGBox
IF r$ = CHR$(13) AND LSel = 0 THEN END
IF r$ = CHR$(27) THEN END
GOTO bclgen:

CreaEnv:
PRINT
PRINT "Taille de la carte (x, Default="; DefX; "):"; : INPUT "", TailleX
PRINT "Taille de la carte (y, Default="; DefY; "):"; : INPUT "", TailleY
IF NOT ((TailleX > 0) AND (TailleX < 1280)) THEN TailleX = DefX
IF NOT ((TailleY > 0) AND (TailleY < 1024)) THEN TailleY = DefY
DefX = TailleX
DefY = TailleY
PRINT "Demarrage de l'‚titeur (Tx="; TailleX; ",Ty="; TailleY; ") ..."
Cde$ = "java CreateEnv " + STR$(TailleX) + " " + STR$(TailleY)
PRINT ">>>"; Cde$
SHELL Cde$
CLS
LSel = 9
GOTO bclgen:

PbPuis:
PRINT
PRINT "Liste des environnements:"
SHELL "DIR *.ENV /W"
PRINT "Donner le nom d'un fichier d'environnement (Default="; DefFile$; "): "; : INPUT "", FicEnv$
IF FicEnv$ <> "" AND RIGHT$(FicEnv$, 4) <> ".env" AND RIGHT$(FicEnv$, 4) <> ".ENV" THEN FicEnv$ = FicEnv$ + ".env"
IF FicEnv$ = "" THEN FicEnv$ = DefFile$
IF RIGHT$(FicEnv$, 4) <> ".env" AND RIGHT$(FicEnv$, 4) <> ".ENV" THEN FicEnv$ = FicEnv$ + ".env"
DefFile$ = FicEnv$
PRINT
PRINT "Demarrage de l'‚titeur du pb des puis (sur "; FicEnv$; ")..."
Cde$ = "java ShaftProblem " + FicEnv$
PRINT ">>>"; Cde$
SHELL Cde$
CLS
LSel = 9
GOTO bclgen:

Transfert:
PRINT
PRINT "Liste des environnements:"
SHELL "DIR *.ENV /W"
PRINT "Donner le nom d'un fichier d'environnement (Default="; DefFile$; "): "; : INPUT "", FicEnv$
IF FicEnv$ <> "" AND RIGHT$(FicEnv$, 4) <> ".env" AND RIGHT$(FicEnv$, 4) <> ".ENV" THEN FicEnv$ = FicEnv$ + ".env"
IF FicEnv$ = "" THEN FicEnv$ = DefFile$
IF RIGHT$(FicEnv$, 4) <> ".env" AND RIGHT$(FicEnv$, 4) <> ".ENV" THEN FicEnv$ = FicEnv$ + ".env"
DefFile$ = FicEnv$
IF LEN(DefFile$) < 5 THEN CLS : LSel = 9: RETURN
PRINT ">> Copie "; FicEnv$
SHELL "cd .."
Cde$ = "copy create\" + FicEnv$ + " ."

SHELL Cde$
PRINT
PRINT "Liste des ShaftProblem:"
SHELL "cd create"
SHELL "DIR *.SHA /W"
DefFile$ = LEFT$(DefFile$, LEN(DefFile$) - 4) + ".sha"
PRINT "Donner le nom d'un fichier Shaft (Default="; DefFile$; "): "; : INPUT "", FicEnv$
IF FicEnv$ <> "" AND RIGHT$(FicEnv$, 4) <> ".sha" AND RIGHT$(FicEnv$, 4) <> ".SHA" THEN FicEnv$ = FicEnv$ + ".sha"
IF FicEnv$ = "" THEN FicEnv$ = DefFile$
IF RIGHT$(FicEnv$, 4) <> ".sha" AND RIGHT$(FicEnv$, 4) <> ".SHA" THEN FicEnv$ = FicEnv$ + ".sha"
DefFile$ = FicEnv$
IF LEN(DefFile$) < 5 THEN CLS : LSel = 9: RETURN
PRINT ">> Copie "; FicEnv$
Cde$ = "copy create\" + FicEnv$ + " ."
SHELL "cd .."
SHELL Cde$
SHELL "cd create"
COLOR 2: PRINT " Termin‚ !": COLOR 7
SLEEP 1
CLS
LSel = 9

RETURN

RGBox:
PRINT "Lancement RunGBox"
PRINT
COLOR 1, 7
PRINT "Û";
SHELL "cd ..": PRINT "Û";
SHELL "cd ..": PRINT "Û";
SHELL "cd ..": PRINT "Û";
SHELL "cd ..": PRINT "Û";
SHELL "cd ..": PRINT "Û";
COLOR 7, 0
PRINT " .-";
SHELL "run-gbox.bat"
PRINT " . ";
SHELL "cd src\demo\agents\robot\create"
COLOR 0, 2: PRINT " Ok "
COLOR 7, 0
CLS
LSel = 9
RETURN

