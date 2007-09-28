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

package ntorrent.gui.main.file;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeListener;

import ntorrent.gui.status.InfoPanel;

/**
 * 
 * @author Kim Eik
 *
 */
public class FileTabComponent {
	JTabbedPane filePane = new JTabbedPane();
	JTextArea log = new JTextArea();
	InfoPanel infoPanel = new InfoPanel();
	FileList fileList = new FileList();

	
	public FileTabComponent(ChangeListener listener){
		log.setEditable(false);
		filePane.setName("file");
		//filePane.addTab("peer list", new JLabel("not supported by rtorrent"));
		filePane.addTab("info", infoPanel.getInfoPanel());
		filePane.addTab("file list", fileList.getFileList());
		filePane.addTab("log",new JScrollPane(log));
		filePane.setSelectedIndex(2);
		//filePane.addTab("tracker list", new JLabel("not supported by rtorrent"));
		//filePane.addTab("chunk list", new JLabel("not supported by rtorrent"));
		//filePane.addTab("chunks seen", new JLabel("not supported by rtorrent"));
		filePane.addChangeListener(listener);
		filePane.setVisible(true);
	}
	
	/**
	 * @return
	 * @uml.property  name="filePane"
	 */
	public JTabbedPane getFilePane() {
		return filePane;
	}
	
	public JTextArea getLog() {
		return log;
	}
	
	/**
	 * @return
	 * @uml.property  name="infoPanel"
	 */
	public InfoPanel getInfoPanel() {
		return infoPanel;
	}
	
	/**
	 * @return
	 * @uml.property  name="fileList"
	 */
	public FileList getFileList() {
		return fileList;
	}
}
