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
package ntorrent.torrenttrackers;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import ntorrent.io.rtorrent.Tracker;
import ntorrent.io.xmlrpc.XmlRpcConnection;
import ntorrent.locale.ResourcePool;
import ntorrent.session.ConnectionSession;
import ntorrent.session.SessionInstance;
import ntorrent.torrenttable.TorrentTableInterface;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentSelectionListener;
import ntorrent.torrenttrackers.model.TorrentTracker;
import ntorrent.torrenttrackers.model.TorrentTrackersListModel;
import ntorrent.torrenttrackers.view.TorrentTrackerList;
import redstone.xmlrpc.XmlRpcArray;
import redstone.xmlrpc.XmlRpcClient;

/**
 * @author Kim Eik
 *
 */
public class TorrentTrackersInstance implements SessionInstance, TorrentSelectionListener,TorrentTrackerActionListener {
	private final TorrentTableInterface tc;
	private final JTabbedPane tab;
	
	private final TorrentTrackersListModel trackerListModel = new TorrentTrackersListModel();
	private final TorrentTrackerList trackerList = new TorrentTrackerList(trackerListModel);
	private final JScrollPane scrollpane = new JScrollPane(trackerList);
	
	//Xmlrpc constants.
	private final XmlRpcConnection connection;
	private final XmlRpcClient client;
	
	private boolean started = false;
	
	public TorrentTrackersInstance(ConnectionSession session) {
		tc = session.getTorrentTableController();
		tab = session.getDisplay().getTabbedPane();
		connection = session.getConnection();
		client = connection.getClient();
		
		trackerList.setTorrentTrackerActionListener(this);
		
		//get and fire selection
		torrentsSelected(tc.getSelectedTorrents());
	}
	
	public void torrentsSelected(Torrent[] tor) {
		trackerListModel.clear();
		if(tor.length == 1){
			for(Torrent torrent : tor){
				String hash = torrent.getHash();
				try {
					XmlRpcArray result = (XmlRpcArray) client.invoke("t.multicall",
						new Object[]{
							hash,
							"", //dummy arg
							"t.url=",
							"t.group=", //unused?
							"t.id=", //unused?
							"t.min_interval=",
							"t.normal_interval=",
							"t.scrape_complete=",
							"t.scrape_downloaded=",
							"t.scrape_incomplete=",
							"t.scrape_time_last=",
							"t.is_open=",
							"t.is_enabled="
						}
					);
					
					for(int x = 0; x < result.size(); x++){
						XmlRpcArray row = (XmlRpcArray)result.get(x);
						TorrentTracker tt = new TorrentTracker(hash,row.getString(0),x);
						tt.setGroup(row.getLong(1).intValue());
						tt.setId(row.getString(2));
						tt.setMinIntervall(row.getLong(3).intValue());
						tt.setNormalIntervall(row.getLong(4).intValue());
						tt.setScrapeComplete(row.getLong(5).intValue());
						tt.setScrapeDownloaded(row.getLong(6));
						tt.setScrapeIncomplete(row.getLong(7).intValue());
						tt.setScrapeTimeLast(row.getLong(8)/1000);
						tt.setOpen(row.getLong(9) == 1);
						tt.setEnabled(row.getLong(10) == 1);
						
						//System.out.println(row);
						
						trackerListModel.add(tt);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		trackerListModel.fireContentsChanged(this);
	}

	public boolean isStarted() {
		return started;
	}

	public void start() {
		started = true;
		int preferredIndex = 2;
		if (preferredIndex > tab.getTabCount())
			preferredIndex = tab.getTabCount();

		tab.insertTab(ResourcePool.getString("tabname", "locale", this), null, scrollpane, null,preferredIndex);
		
		
		//add this as a listener
		tc.addTorrentSelectionListener(this);
	}

	public void stop() {
		started = false;
		//remove this as a listener
		tc.removeTorrentSelectionListener(this);
		tab.removeTabAt(tab.indexOfComponent(scrollpane));
	}

	public void setEnabled(final boolean b, final TorrentTracker tracker) {
		final Tracker t = connection.getTrackerClient();
		
		//send the command
		t.set_enabled(tracker.getHash() + ":t" + tracker.getLocalId(), b ? 1 : 0);
		
		//update the list.
		tracker.setEnabled(b);
		trackerListModel.fireContentsChanged(this);
	}

}
