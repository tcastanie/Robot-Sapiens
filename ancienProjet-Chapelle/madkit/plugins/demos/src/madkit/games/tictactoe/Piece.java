/*
* Piece.java - A generic piece for all "cases" based games.
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

import java.awt.Color;import java.awt.Graphics;

/**
 * <p>A class representing a generic piece for all "cases" based games.</p>
 * <p>Copyright (c) 2002 - MadKit Team. goto: www.madkit.org</p>
 * @author J. Ferber
 * @version 1.0
 */
public class Piece {
    static final int RED = 0;
    static final int BLUE = 1;
    int column = 0;
    int row = 0;
    int couleur = BLUE;
    GameCases jeu;
    GameCasesDisplay display;

    public Piece(GameCases j, GameCasesDisplay v, int coul) {
        jeu = j;
        couleur = coul;
        display = v;
    }

    boolean install(int c, int r) {
        boolean b = place(c, r);
        display.repaint();
        return b;
    }

    boolean place(int c, int r) {
        column = c;
        row = r;
        return (jeu.setPiece(this, c, r));
    }

    void delete() {
        jeu.deletePiece(this, column, row);
    }

    void moveTo(int c, int r) {
        delete();
        place(c, r);
    }

    void draw(Graphics g, int l, int h) {
        Color c = null;
        switch (couleur) {
            case RED:
                c = Color.red;
                break;
            case BLUE:
                c = Color.blue;
                break;
        }
        g.setColor(c);
        g.fillOval(column * l + 1, row * h + 1, l - 2, h - 2);
    }

    void dump() {
        switch (couleur) {
            case RED:
                System.out.print("R");
                break;
            case BLUE:
                System.out.print("B");
                break;
        }
    }

    boolean attempt(int n, int c, int r, int d) {
        if (jeu.isOccupied(c, r)) {
            Piece p = jeu.takePiece(c, r);
            if ((p != null) && (p.couleur == couleur) && (p.tryLine(n - 1, d)))
                return (true);
        }
        return (false);
    }

    boolean tryAllLines(int n) {
        if (attempt(n, column + 1, row, 1))
            return true;
        if (attempt(n, column - 1, row, 2))
            return true;
        if (attempt(n, column, row - 1, 3))
            return true;
        if (attempt(n, column + 1, row - 1, 4))
            return true;
        if (attempt(n, column - 1, row - 1, 5))
            return true;
        return false;
    }

    boolean tryLine(int n, int d) {
        if ((d != 0) && (n == 0))
            return (true);
        switch (d) {
            case 1:
                return attempt(n, column + 1, row, 1);
            case 2:
                return attempt(n, column - 1, row, 2);
            case 3:
                return attempt(n, column, row - 1, 3);
            case 4:
                return attempt(n, column + 1, row - 1, 4);
            case 5:
                return attempt(n, column - 1, row - 1, 5);
        }
        return false;
    }
}
