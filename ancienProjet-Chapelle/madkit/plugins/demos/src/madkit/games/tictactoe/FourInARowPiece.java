/*
* FourInARowPiece.java - A piece which is used in a "four in a row" game
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
 * <p>A piece which is used in the Four in a row game</p>
 * <p>Copyright: Copyright (c) 2002 - MadKit Team. goto: www.madkit.org</p>
 * @author J. Ferber
 * @version 1.0
 */
public class FourInARowPiece extends Piece {
    public FourInARowPiece(GameCases j, GameCasesDisplay v, int coul) {
        super(j, v, coul);
    }

    void descendre() {
        while ((row < jeu.height - 1) && (!jeu.isOccupied(column, row + 1))) {
            moveTo(column, row + 1);
        }
    }

    boolean install(int c, int r) {
        boolean b = place(c, r);
        descendre();
        display.repaint();
        return b;
    }
}
