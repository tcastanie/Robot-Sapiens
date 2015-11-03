/*
 * DownloadStatus.java - Created on Feb 1, 2004
 * 
 * Copyright (C) 2003-2004 Sebastian Rodriguez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Last Update: $Date: 2004/02/16 14:52:23 $
 */

package madkit.pluginmanager;

import madkit.kernel.Message;


/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.1 $
 */
class DownloadStatus extends Message {
	
	private String _plugin;
	private int _size;
	private int _downloaded;
	/**
	 * 
	 */
	public DownloadStatus(String plugin, int size, int downloaded) {
		super();
		_plugin=plugin;
		_size=size;
		_downloaded=downloaded;
	}
	

	/**
	 * @return Returns the _downloaded.
	 */
	public final int getDownloaded() {
		return _downloaded;
	}

	/**
	 * @return Returns the _plugin.
	 */
	public final String getPluginName() {
		return _plugin;
	}

	/**
	 * @return Returns the _size.
	 */
	public final int getSize() {
		return _size;
	}

}
