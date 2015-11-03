/*
* GameCases.java - a Generic "cases" based game in Java
* Author  Jacques Ferber - 2002
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package madkit.games.tictactoe;



 /**
 * a generic game class for "cases" based games.
 * <p>Copyright (c) 2002 - MadKit Team. goto: www.madkit.org</p>
 * @author J. Ferber
 * @version 1.0
 */
public abstract class GameCases {
    int width = 5;
    int height = 5;
    int numberAlignment = 5;
    boolean waiting = true;

    void setWaiting(boolean b) { waiting = b; }

    int currentColor = Piece.BLUE;

    void setCurrentColor(int i) { currentColor = i; };

    public static String[] noms = new String[] { "Red", "Blue" };

    Piece[] [] cases;
    GameCasesDisplay display;

    GameCasesDisplay getDisplay() { return display; }

    void setDisplay(GameCasesDisplay disp) { display = disp; }

    GameCasesFrame jf;
    TicTacToeAgent ag;

    void setAgent(TicTacToeAgent _ag) { ag = _ag; }


    public GameCases(GameCasesFrame _jf, int t, int n) {
        jf = _jf;
        height = t;
        width = t;
        numberAlignment = n;
    }

    Piece takePiece(int c, int r) {
        return (cases[c] [r]);
    }

    void newGame() {
        currentColor = Piece.BLUE;
        cases = new Piece[width] [height];
        //  continuer=true;
        if (ag.getMaster()) {
            message("You may start");
            waiting = false;
        } else {
            message("A new game has started, wait for the 'master' to play");
            waiting = true;
            currentColor = Piece.RED;
        }
    }

    void message(String s) {
        jf.message(s);
    }

    boolean isValid(int c, int r) {
        return (c >= 0) && (c < width) && (r >= 0) && (r < height);
    }

    boolean isOccupied(int c, int r) {
        return (isValid(c, r) && (cases[c] [r] != null));
    }

    void createPiece(int c, int r, int coul) {
        if (isValid(c, r)) {
        }
    }

    boolean setPiece(Piece p, int c, int r) {
        if (isValid(c, r) && !isOccupied(c, r)) {
            cases[c] [r] = p;
            return true;
        } else {
            return false;
        }
    }

    boolean deletePiece(Piece p, int c, int r) {
        if (isOccupied(c, r) && cases[c] [r] == p) {
            cases[c] [r] = null;
            return true;
        } else
            return false;
    }

    boolean lookForConfiguration() {
        for (int i = 0; i < width; i++)
            for (int j = height - 1; j >= 0; j--) {
                if (cases[i] [j] != null)
                    if (cases[i] [j].tryAllLines(numberAlignment - 1)) {
                        // System.out.println("trouve ligne chez les " + cases[i][j].couleur);
                        return (true);
                    }
            }
        return false;
        // System.out.println("fin recherche");
    }

    void receivePlay(int x, int y, int color) {
        installPosition(x, y, color);
        boolean b = lookForConfiguration();
        if (b) {
            waiting = true;
            message("Tt's sad, but you have lost..");
        } else {
            waiting = false;
            message("It's your turn to play");
        }
    }

    void doClick(int x, int y) {
        if (!waiting) {
            boolean r = installPosition(x, y, currentColor);
            if (r) {
                boolean b = lookForConfiguration();
                waiting = true;
                ag.sendPosition(x, y, currentColor);
                if (b)
                    message("Contratulations!! you win!!");
                else
                    message("Waiting the other one's play");
            }
        }
    }

    abstract boolean installPosition(int x, int y, int color);
}
