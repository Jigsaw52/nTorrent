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
package ntorrent.torrenttable;

import java.util.Map;
import java.util.Vector;

import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentSelectionListener;
import ntorrent.torrenttable.model.TorrentStateListener;
import ntorrent.torrenttable.model.TorrentTableActionListener;
import ntorrent.torrenttable.view.TorrentTable;
import ntorrent.torrenttable.viewmenu.ViewChangeListener;

/**
 * @author Kim Eik
 *
 */
public interface TorrentTableInterface extends ViewChangeListener,TorrentTableActionListener {
	public void viewChanged(String view);
	public Map<String, Torrent> getTorrents();
	public TorrentTable getTable();
	public Vector<String> getDownloadVariable();
	public void addTorrentSelectionListener(TorrentSelectionListener listener);
	public void setSelectionMethod(SelectionValueInterface i);
	public void removeTorrentSelectionListener(TorrentSelectionListener listener);
	public Torrent[] getSelectedTorrents();
	public void addTorrentStateListener(TorrentStateListener listener);
	public void removeTorrentStateListener(TorrentStateListener listener);
}
