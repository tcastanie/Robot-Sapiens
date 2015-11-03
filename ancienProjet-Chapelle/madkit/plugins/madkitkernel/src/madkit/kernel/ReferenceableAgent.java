/*
* ReferenceableAgent.java - Kernel: the kernel of MadKit
* Copyright (C) 1998-2002 Olivier Gutknecht, Fabien Michel, Jacques Ferber
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.

* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.

* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/
package madkit.kernel;

/**The ability for a MadKit agent to be spied or scheduled in
   synchronous mode is enabled by implementing the AccessibleAgent
   interface. Class that do not implement this interface will not be
   returned by the kernel to Watchers or Schedulers, and thus will
   not be able to play in reactive/synchronous systems.

   Note that this interface is empty. It is the responsability of the
   developer to decide if a MadKit agent can appear in reactive
   systems. As reactive system MadKit implementation envolves direct
   references manipulation, that could be a potential security hole
   for "classical" agents. 

   Developers of reactive systems should not inherit from Agent or a
   subclass but from AbstractAgent. The Agent class defines a threaded
   implementation for agents, which is a definitive overkill for
   reactive systems in most cases. Just use a AbstractAgent subclass
   and appropriate Schedulers.

  @version 1.0
  @author Olivier Gutknecht */

public interface ReferenceableAgent{}
