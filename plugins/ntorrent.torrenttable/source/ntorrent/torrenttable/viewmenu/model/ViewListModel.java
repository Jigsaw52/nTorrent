/**
 *   nTorrent - A GUI client to administer a rtorrent process 
 *   over a network connection.
 *   
 *   Copyright (C) 2007  Kim Eik
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ntorrent.torrenttable.viewmenu.model;

import javax.swing.AbstractListModel;

public class ViewListModel extends AbstractListModel {
	private static final long serialVersionUID = 1L;
	
	private final View[] elements = {
			new ViewModel("main","main"),
			new ViewModel("started","started"),
			new ViewModel("stopped","stopped"),
			new ViewModel("complete","complete"),
			new ViewModel("incomplete","incomplete"),
			new ViewModel("hashing","hashing"),
			new ViewModel("seeding","seeding")
	};
	
	
	public Object getElementAt(int index) {
		return elements[index];
	}

	public int getSize() {
		return elements.length;
	}

}
