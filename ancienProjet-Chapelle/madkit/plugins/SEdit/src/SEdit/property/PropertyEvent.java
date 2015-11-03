/*
* PropertyEvent.java - SEdit, a tool to design and animate graphs in MadKit
* Copyright (C) 1998-2002 Jacques Ferber, Olivier Gutknecht
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
package SEdit.property;

import java.awt.Component;
import java.util.EventObject;

public class PropertyEvent extends EventObject {

    public PropertyEvent(Component src, Object elt, String lab, String newValue, Class typ){
        super(src);
        element = elt;
        label =lab;
        value = newValue;
        type = typ;
    }

    protected Object element;
    protected String label;
    protected String value;
    protected Class type;

    public Object getElement() {
        return element;
    }
    public void setElement(Object newElement) {
        element = newElement;
    }
    public void setLabel(String newLabel) {
        label = newLabel;
    }
    public String getLabel() {
        return label;
    }
    public void setValue(String newValue) {
        value = newValue;
    }
    public String getValue() {
        return value;
    }
    public void setType(Class newType) {
        type = newType;
    }
    public Class getType() {
        return type;
    }

}
