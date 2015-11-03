/*
* GameCasesDisplay.java - a generic graphic class
* to display "cases" based games in MadKit
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

import java.awt.Color;import java.awt.Dimension;import java.awt.Graphics;import java.awt.event.MouseEvent;import java.awt.event.MouseListener;import javax.swing.JComponent;

/**
 * a generic graphic class to display "cases" based games in MadKit    
 * <p>Copyright (c) 2002 - MadKit Team. goto: www.madkit.org</p>
 * @author J. Ferber
 * @version 1.0
 */
public class GameCasesDisplay extends JComponent implements MouseListener {
    GameCases jeu;

    public GameCasesDisplay(GameCases j) {
        jeu = j;
        addMouseListener(this);
        setPreferredSize(new Dimension(280, 280));
    }

    public void paint(Graphics g) {
        int largeurPion = (int)Math.floor((getWidth() - 2) / jeu.width);
        int hauteurPion = (int)Math.floor((getHeight() - 2) / jeu.height);
        drawGrille(g, largeurPion, hauteurPion);
        drawCases(g, largeurPion, hauteurPion);
    }

    void drawGrille(Graphics g, int l, int h) {
        g.setColor(Color.white);
        g.fillRect(0, 0, l * jeu.width, h * jeu.height);
        g.setColor(Color.darkGray);
        for (int i = 0; i <= jeu.width; i++) {
            g.drawLine(i * l, 0, i * l, h * jeu.height);
        }
        for (int j = 0; j <= jeu.height; j++) {
            g.drawLine(0, j * h, l * jeu.width, j * h);
        }
    }

    void drawCases(Graphics g, int l, int h) {
        for (int i = 0; i < jeu.width; i++)
            for (int j = 0; j < jeu.height; j++) {
                if (jeu.cases[i] [j] != null)
                    jeu.cases[i] [j].draw(g, l, h);
            }
    }

    public void mouseClicked(MouseEvent e) { }

    public void mouseEntered(MouseEvent e) { }

    public void mouseExited(MouseEvent e) { }

    public void mousePressed(MouseEvent e) {
        int largeurPion = getWidth() / jeu.width;
        int hauteurPion = getHeight() / jeu.height;
        jeu.doClick(e.getX() / largeurPion, e.getY() / hauteurPion);
        repaint();
    }

    public void mouseReleased(MouseEvent e) { }
}
