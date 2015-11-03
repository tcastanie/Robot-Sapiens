/*
* TicTacToeAgent.java - the Agent class which is used
* for the generic TicTacToe game
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

import madkit.kernel.Agent;import madkit.kernel.AgentAddress;import madkit.kernel.Message;import madkit.kernel.StringMessage;import madkit.messages.ActMessage;

/**
 * <p>The TicTacToe shows how to easily implement a distributed application in MadKit.
 * The TicTacToeAgent class is the agent class which encapsulates both the GUI (the GameCasesFrame class)
 * and the game class (GameCases).</p>
 * <p>Copyright: Copyright (c) 2002 - MadKit Team. goto: www.madkit.org</p>
 * @author J. Ferber
 * @version 1.0
 */
public class TicTacToeAgent extends Agent {
    GameCasesFrame gui;
    GameCases game;

    void setGame(GameCases j) { game = j; }

    String community = "demogames";
    String group = "tictactoe";
    boolean master = false;

    boolean getMaster() { return master; }

    void setMaster(boolean b) { master = b; }

    boolean living = true;

    public TicTacToeAgent() {
    }

    public TicTacToeAgent(String gr){
        group = gr;
    }

    public void initGUI() {
        gui = new GameCasesFrame(this);
        gui.newDefaultGame();
        game = gui.getGame();
        this.setGUIObject(gui);
    }

    public void activate() {
        // if the group does not existe
        if (this.createGroup(true, community, group, null, null) == 1) {
            this.requestRole(community, group, "player", null);
            gui.setColor(Piece.BLUE);
            master = true;
            gui.setMaster(master);
            gui.message("Hello I am the master");
            game.setWaiting(true);
        } else {
            AgentAddress[] agList = this.getAgentsWithRole(community, group, "player");
            if (agList.length > 1) {
                living = false;
            }
            this.requestRole(community, group, "player", null);
            gui.setColor(Piece.RED);
            game.setWaiting(true);
            sendOther(new ActMessage("joining"));
        }
    }

    void sendOther(Message m) {
        AgentAddress other = null;
        AgentAddress[] v = this.getAgentsWithRole(community, group, "player");
        for (int i = 0; i < v.length; i++) {
            AgentAddress agent = v[i];
            if (!agent.equals(getAddress())) {
                other = agent;
                break;
            }
        }
        if (other != null)
            sendMessage(other, m);
    }

    void sendPosition(int x, int y, int color) {
        int[] pos = new int[3];
        pos[0] = x; pos[1] = y; pos[2] = color;
        sendOther(new ActMessage("position", pos));
    }

    void sendNewGame(int size, int align, int type) {
        int[] pos = new int[3];
        pos[0] = size; pos[1] = align; pos[2] = type;
        sendOther(new ActMessage("newGame", pos));
    }

    void handleMessage(ActMessage m) {
        if (m.getAction().equals("joining")) {
            game.setWaiting(false);
            gui.newGame();
            gui.message("A player has joined, you may start to play");
        }
        if (m.getAction().equals("position")) {
            int[] arg = (int[]) m.getObject();
            game.receivePlay(arg[0], arg[1], arg[2]);
        }
        if (m.getAction().equals("newGame")) {
            int[] arg = (int[]) m.getObject();
            game.setWaiting(true);
            gui.newGame(arg[0], arg[1], arg[2]);
            // gui.message("A new game has started, wait the other one's play");
        }
    }

    public void live() {
        Message msg;
        while (living) {
            msg = this.waitNextMessage();
            if (msg instanceof ActMessage)
                handleMessage((ActMessage)msg);
            else if (msg instanceof StringMessage) {
                gui.message(((StringMessage)msg).getString());
            }
        }
    }
}
