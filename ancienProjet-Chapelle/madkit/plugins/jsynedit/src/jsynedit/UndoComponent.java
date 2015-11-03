/*
 * Created on 11 f?vr. 2004
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

/**
 * @author sdalozde
 */

package jsynedit;


import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import java.lang.System;


public class UndoComponent
	implements UndoableEditListener
{
	/* Attributs */
	private UndoManager undoManager;
	private CompoundEdit compoundEdit;

	private long time, delayTime;
	private int undoLimit;
	
	private boolean modified;
	private boolean textModified;
	
	public final static long SEQUENCE_DELAY = 500;
	public final static int MAX_UNDOLIMIT = 100;


	/* Constructeur */
	public UndoComponent()
	{
		undoManager = new UndoManager();
		compoundEdit = new CompoundEdit();

		time = System.currentTimeMillis();
		delayTime = SEQUENCE_DELAY;
		undoLimit = MAX_UNDOLIMIT;
		undoManager.setLimit( undoLimit ); 
		modified = false;
		textModified = false;
	}



	/* M?thodes */

	/**
	 * Executed each time UndoableEditEvent happened.
	 */
	public void undoableEditHappened(UndoableEditEvent e)
	{
		long undoableTime = System.currentTimeMillis();
		textModified = true;
		if ( (undoableTime - this.time) <  delayTime )
		{
			this.modified = true;
			compoundEdit.addEdit( e.getEdit() );
		}
		else
		{
			if ( this.modified )
			{
				compoundEdit.end();
				undoManager.addEdit( compoundEdit );
			
				compoundEdit = new CompoundEdit();
			}
			compoundEdit.addEdit( e.getEdit() );
			this.modified = true;
		}
		
		this.time = undoableTime;
	}
	

	/**
	 * Undo last change.
	 */
	public void undo()
	{
		if ( this.modified )
		{
			compoundEdit.end();
			undoManager.addEdit( compoundEdit );
		
			compoundEdit = new CompoundEdit();
		}
		
		if (undoManager.canUndo())
			undoManager.undo();
		
		this.modified = false;
	}
	
	
	/**
	 * Redo last undone change.
	 */
	public void redo()
	{
		if ( undoManager.canRedo() )
			undoManager.redo();
	}
	
	
	/**
	 * Empty the UndoComponent
	 */
	public void reset()
	{
		undoManager.discardAllEdits();
		compoundEdit.die();
		compoundEdit = new CompoundEdit();
		textModified = false;
	}
	
	
	// acesseurs
	/**
	 * Indicates if operations has been performed on text after last undo/redo operation
	 * @return true if text has been modified.
	 */
   public boolean getTextModified()
	{
		return textModified;
	}


	/**
	 * Forces UndoComponent to perform as if text has been modified (val is true) or
	 * hasn't been modified (val is false) after last undo/redo operation.
	 * @param val
	 */
	public void setTextModified(boolean val)
	{
		textModified=val;
	}
	
	
	/**
	 * Gets the delay authorized between two text transformations for a single undo/redo operation.
	 * This delay started after last operation and reset when a new transformation occurs.
	 * Default value is 500 ms.
	 * @return the maximum delay in milliseconds before creation of another undo/redo operation
	 */
	public long getSequenceDelay()
	{
		return delayTime;
	}


	/**
 	* Modifies the delay between two text transformations.
 	* @param delay the delay in milliseconds
 	*/	
	public void setSequenceDelay( long delay )
	{
		delayTime = delay;
	}
	
	
	/**
	 * Gets the maximum number of sequences UndoComponent will hold.
	 * @return the limit size
	 * @see UndoManager.getLimit()
	 */
	public int getLimit()
	{
		return undoLimit;
	}
	
	
	/**
	 * Sets the maximum number of sequences UndoComponent will hold.
	 * @param limit the limit size
	 * @see UndoManager.setLimit(int)
	 */
	public void setLimit( int limit )
	{
		undoLimit = limit;
		undoManager.setLimit(limit);
	}
}
