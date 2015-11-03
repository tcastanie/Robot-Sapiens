/*
* FourInARow.java - A four in a row game
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
 * A class that represent a "four in a row" game in MadKit
 * <p>Copyright: Copyright (c) 2002 - MadKit Team. goto: www.madkit.org</p>
 * @author J. Ferber
 * @version 1.0
 */
public class FourInARow extends GameCases {
    public FourInARow(GameCasesFrame _jf, int t, int n) {
        super(_jf, t, n);
        //_jf.setTitle("Jeu de Puissance 4");
    }

    boolean installPosition(int x, int y, int color) {
        if (y == 0) {
            Piece p = new FourInARowPiece(this, display, color);
            p.install(x, y);
            return true;
        } else {
            message("click in the upper row, try again");
            return false;
        }
    }
}
