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
package ntorrent.torrentlabels.view;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import ntorrent.locale.ResourcePool;

/**
 * @author Kim Eik
 *
 */
public class LabelPopupMenu extends JMenu {
	public final static String[] MENU_ITEMS = {
		"label.none",
		"label.new"
		};
	
	public LabelPopupMenu(ActionListener listener) {
		super();
		setText(ResourcePool.getString("menu", "locale", this));
		for(String s : MENU_ITEMS){
			JMenuItem item = new JMenuItem(ResourcePool.getString(s, "locale", this));
			item.setActionCommand(s);
			item.addActionListener(listener);
			add(item);
		}
		addSeparator();
	}
}
