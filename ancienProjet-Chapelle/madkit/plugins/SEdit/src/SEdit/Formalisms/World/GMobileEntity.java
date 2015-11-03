/*
* GMobileEntity.java - A simple reactive agent library
* Copyright (C) 1998-2002 Jacques Ferber
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
package SEdit.Formalisms.World;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.lang.Math;
import java.io.*;

import javax.swing.*;

import SEdit.*;
import SEdit.Graphics.*;


public class GMobileEntity extends GIcon {

	 public void paint(Graphics g){
	 	super.paint(g);
	 	MobileEntity e = (MobileEntity)getSElement();
	 	if (e.isDetecting()){
	 		Point p = getCenter();
	 		int r = e.getDetectingRange();
	 		g.setColor(Color.red);
	 		g.drawOval(p.x-r,p.y-r,2*r,2*r);
	 	}
	 }
}
