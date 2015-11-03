/*
 * Created on 16 avr. 2004
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
 
package gnu.options;

import javax.swing.JTextField;

import jsynedit.AbstractEditorPanel;
import gnu.gui.AbstractOptionPane;

/**
 * @author sdaloz
 */

public class UndoOptions extends AbstractOptionPane
{
	private JTextField limit, sequence;
	
	
	public UndoOptions()
	{
		super("undo");
		
		limit = new JTextField();
		sequence = new JTextField();
		
		addComponent("Max operations number", limit);
		addComponent("Waiting delay between sequences in milliseconds", sequence);
		
		load();
	}
	
	
	public void load()
	{
		limit.setText(AbstractEditorPanel.getProperty("options.undo.limit"));
		sequence.setText(AbstractEditorPanel.getProperty("options.undo.sequence"));
	}
	
	
	public void save()
	{
		AbstractEditorPanel.setProperty("options.undo.limit", limit.getText());
		AbstractEditorPanel.setProperty("options.undo.sequence", sequence.getText());
	}
}