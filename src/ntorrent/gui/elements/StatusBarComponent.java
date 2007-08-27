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

package ntorrent.gui.elements;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class StatusBarComponent {
	//Statusbar component
	JPanel statusBar = new JPanel();
	
	public StatusBarComponent() {
		statusBar.setVisible(true);
	}
	
	public JLabel addStatusItem(String title, String value){
		JLabel label = new JLabel(title+": "+value);
		label.setVisible(true);
		statusBar.add(label);
		return label;
	}
	
	public Component getStatusBar() {
		return statusBar;
	}
}
