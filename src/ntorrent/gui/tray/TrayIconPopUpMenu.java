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
package ntorrent.gui.tray;

import java.awt.Window;
import java.awt.event.MouseEvent;

import ntorrent.gui.core.AbstractPopupMenu;
import ntorrent.settings.Constants.Commands;

/**
 * @author Kim Eik
 */
public class TrayIconPopUpMenu extends AbstractPopupMenu {
	final static String[] menuItems = {
			Commands.ADD_TORRENT.toString(),
			Commands.ADD_URL.toString(),
			null,
			Commands.START_ALL.toString(),
			Commands.STOP_ALL.toString(),
			null,
			Commands.QUIT.toString()
			};
	Window rootWin;
	
	public TrayIconPopUpMenu(Window root) {
		super(menuItems);
		rootWin = root;
	}

	@Override
	protected void maybeShowPopup(MouseEvent e) {
		switch (e.getButton()){
		case MouseEvent.BUTTON1:
			if(!rootWin.isFocused()){
				rootWin.requestFocus();
				rootWin.toFront();
			}
			break;
		case MouseEvent.BUTTON3:
			popup.setLocation(e.getX(), e.getY());
			popup.setInvoker(rootWin);
			popup.setVisible(true);
			break;
		}
	}

}
