/*
* GameCasesFrame.java - The general graphic component
* used as a GUI component from TicTactToeAgent
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

import java.awt.BorderLayout;import java.awt.Event;import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import java.awt.event.KeyEvent;import javax.swing.JButton;import javax.swing.JLabel;import javax.swing.JMenu;import javax.swing.JMenuBar;import javax.swing.JMenuItem;import javax.swing.JPanel;import javax.swing.JRootPane;import javax.swing.JTextField;import javax.swing.KeyStroke;

/**
 * <p>A general graphic component
 * used as a GUI component from the TicTactToeAgent</p>
 * <p>Copyright: Copyright (c) 2002 - MadKit Team. goto: www.madkit.org</p>
 * @author J. Ferber
 * @version 1.0
 */
public class GameCasesFrame extends JRootPane implements ActionListener {
    final public static int TICTACTOE = 0;
    final public static int FOURINAROW = 1;
    final public static String[] gameName = {"TicTacToe","4 in a row"};
    GameCases game;

    GameCases getGame() { return game; }

    JLabel msg = new JLabel();
    int gameType = TICTACTOE;
    TicTacToeAgent ag;
    int size = 5;
    int nbAlign = 5;
    JButton bNew;
    JTextField jtextSize;
    JTextField jtextNbAlign;
    JLabel jGameType;
    boolean master = false;

    void setMaster(boolean b) {
        master = b;
        if (b) {
            bNew.setEnabled(b);
            jtextSize.setEnabled(b);
            jtextNbAlign.setEnabled(b);
        }
    }

    GameCasesDisplay display;

    public GameCasesFrame(TicTacToeAgent _ag) {
        ag = _ag;
        getContentPane().setLayout(new BorderLayout());
        setSize(400, 400);
        bNew = new JButton("New");
        bNew.setEnabled(false);
        JLabel jlabSize = new JLabel("Size:");
        jtextSize = new JTextField("" + size, 3);
        jtextSize.setEnabled(false);
        JLabel jlabAlign = new JLabel("Number to align :");
        jtextNbAlign = new JTextField("" + nbAlign, 3);
        jtextNbAlign.setEnabled(false);
        jGameType = new JLabel(gameName[gameType]);
        JPanel p1 = new JPanel();
        p1.add(jlabSize);
        p1.add(jtextSize);
        //p1.add(b3);
        p1.add(bNew);
        p1.add(jlabAlign);
        p1.add(jtextNbAlign);
        p1.add(jGameType);
        getContentPane().add(p1, BorderLayout.NORTH);
        //b1.addActionListener(this);
        //b2.addActionListener(this);
        //b3.addActionListener(this);
        bNew.addActionListener(this);
        getContentPane().add(msg, BorderLayout.SOUTH);
        // les menus
        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar); // on installe le menu bar
        JMenu menuFile = new JMenu("Start"); // on installe le premier menu
        menubar.add(menuFile);
        addMenuItem(menuFile, "New Game", "New", KeyEvent.VK_N);
        addMenuItem(menuFile, "Quit", "Quit", KeyEvent.VK_Q);
        JMenu menuJeux = new JMenu("Games"); // on installe le premier menu
        menubar.add(menuJeux);
        addMenuItem(menuJeux, "TicTacToe", "TicTacToe", -1);
        addMenuItem(menuJeux, "4 in a Row ", "4InARow", -1);
        JMenu menuHelp = new JMenu("Aide"); // on installe le premier menu
        menubar.add(menuHelp);
        addMenuItem(menuHelp, "Help", "Help", -1);
        addMenuItem(menuHelp, "About", "About", -1);
        // show();
    }

    void newGame() {
        int t = Integer.parseInt(jtextSize.getText());
        int n = Integer.parseInt(jtextNbAlign.getText());
        int type = gameType;
        newGame(t, n, type);
        ag.sendNewGame(t, n, type);
    }

    void newDefaultGame() {
        newGame(size, nbAlign, gameType);
    }

    void newGame(int t, int n, int type) {
        size = t;
        nbAlign = n;
        gameType = type;
        jtextSize.setText("" + t);
        jtextNbAlign.setText("" + n);
        jGameType.setText(gameName[gameType]);
        if (nbAlign > size) nbAlign = size;
        switch (gameType) {
            case TICTACTOE:
                game = new TicTacToe(this, size, nbAlign);
                break;
            case FOURINAROW:
                game = new FourInARow(this, size, nbAlign);
                break;
        }
        if (display != null)
            getContentPane().remove(display);
        display = new GameCasesDisplay(game);
        game.setDisplay(display);
        game.setAgent(ag);
        ag.setGame(game);
        game.newGame();
        getContentPane().add(display, BorderLayout.CENTER);
        this.validate();
    }

    void message(String s) {
        msg.setText(s);
    }

    void setColor(int i) {
        game.setCurrentColor(i);
    }

    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals("New"))
            newGame();
        else if (s.equals("Quit"))
            System.exit(0);
        else if (s.equals("TicTacToe")) {
            if (gameType != TICTACTOE) {
                gameType = TICTACTOE;
                newGame();
            }
        }
        else if (s.equals("4InARow")) {
            if (gameType != FOURINAROW) {
                gameType = FOURINAROW;
                newGame();
            }
        }
    }

    void addMenuItem(JMenu m, String label, String command, int key) {
        JMenuItem menuItem;
        menuItem = new JMenuItem(label);
        m.add(menuItem);
        menuItem.setActionCommand(command);
        menuItem.addActionListener(this);
        if (key > 0) {
            if (key != KeyEvent.VK_DELETE)
                menuItem.setAccelerator(KeyStroke.getKeyStroke(key, Event.CTRL_MASK, false));
            else
                menuItem.setAccelerator(KeyStroke.getKeyStroke(key, 0, false));
        }
    }
}
