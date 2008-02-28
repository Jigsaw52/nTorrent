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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ntorrent.env.Environment;
import ntorrent.gui.window.Window;
import ntorrent.io.rtorrent.Download;
import ntorrent.io.xmlrpc.XmlRpcConnection;
import ntorrent.profile.model.LocalProfileModel;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentSelectionListener;
import ntorrent.torrenttable.model.TorrentTableActionListener;
import ntorrent.torrenttable.model.TorrentTableModel;
import ntorrent.torrenttable.view.TorrentTable;
import ntorrent.torrenttable.view.TorrentTableJPopupMenu;
import ntorrent.viewmenu.ViewChangeListener;

import org.java.plugin.Plugin;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import org.java.plugin.PluginManager.EventListener;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;

import redstone.xmlrpc.XmlRpcArray;
import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcFault;

public class TorrentTableController implements TorrentTableInterface,Runnable, /*EventListener,*/ ListSelectionListener{
	
	private final TorrentTableModel ttm = new TorrentTableModel();
	private final TorrentTable table = new TorrentTable(ttm);
	private final Map<String,Torrent> torrents = new HashMap<String,Torrent>();
	private final XmlRpcConnection connection;
	private final Thread controllerTread = new Thread(this);
	
	//private final PluginManager pluginManager;
	
	private final Vector<String> download_variable = new Vector<String>();
	private final Vector<String> torrentTableExtensions = new Vector<String>();
	private final Vector<TorrentSelectionListener > torrentSelectionListeners = new Vector<TorrentSelectionListener>();
	
	public TorrentTableController(XmlRpcConnection connection) {
		this.connection = connection;
		
		//populate downloadlist
	    final String[] download_variable = {
                "", //reserved for view arg
                "d.get_hash=",  //ID
                "d.get_name=", //constant
                "d.get_state=",         //variable
                "d.get_completed_bytes=", //variable
                "d.get_up_total=",      //variable
                "d.get_peers_complete=",
                "d.get_peers_accounted=",
                "d.get_down_rate=", //variable
                "d.get_up_rate=", //variable
                "d.get_message=", //relative
                "d.get_priority=", //relative
                "d.get_size_bytes="
	    };
	    
	    for(String i : download_variable){
	    	this.download_variable.add(i);
	    }
	    
		//pluginManager = Environment.getPluginManager();
		//pluginManager.registerListener(this);
		
		//initTorrentTableExtensions();
		
		table.getTablePopup().addTorrentTableActionListener(this);
		table.getSelectionModel().addListSelectionListener(this);
		
		controllerTread.start();
	}
	
	public TorrentTable getTable() {
		return table;
	}
	
	public Map<String, Torrent> getTorrents() {
		return torrents;
	}
	
	public Vector<String> getDownloadVariable() {
		return download_variable;
	}
		
	public void run() {
	   XmlRpcClient client = connection.getClient();
		try {
			while(true){
				XmlRpcArray download_list = (XmlRpcArray)client.invoke("d.multicall", download_variable);
				//System.out.println(download_variable);
				//System.out.println(download_list);
				int rowsRecieved = download_list.size();
				int rowsPresent = ttm.getRowCount();
				
				for(int x = rowsPresent-1; x > rowsRecieved-1; x--){
					ttm.removeRow(x);
					//System.out.println("removing row: "+x);
				}
				
				for(int x = 0 ; x < rowsRecieved; x++){
					XmlRpcArray data = (XmlRpcArray)download_list.get(x);
					
					int dataCell = 0;
					
					Torrent tor = new Torrent(data.getString(dataCell));
					
					if(!torrents.keySet().contains(tor.getHash()))
						torrents.put(tor.getHash(),tor);
					else
						tor = torrents.get(data.getString(dataCell));
					
					tor.setName(data.getString(++dataCell));
					tor.setStarted(data.getLong(++dataCell) == 1);
					tor.setCompletedBytes(data.getLong(++dataCell));
					tor.setUpTotal(data.getLong(++dataCell));
					tor.setPeersComplete(data.getLong(++dataCell));
					tor.setPeersAccounted(data.getLong(++dataCell));
					tor.setDownRate(data.getLong(++dataCell));
					tor.setUpRate(data.getLong(++dataCell));
					tor.setMessage(data.getString(++dataCell));
					tor.setPriority(data.getLong(++dataCell));
					tor.setSizeBytes(data.getLong(++dataCell));
					
					//get additional data
					while(++dataCell < data.size()){
						//System.out.println("Setting additional data: "+download_variable.elementAt(dataCell+1)+" "+data.get(dataCell));
						tor.setProperty(download_variable.elementAt(dataCell+1), 
								data.get(dataCell));
						dataCell++;
					}
					
					if(rowsPresent > x){
						//System.out.println("updating row: "+x);
						ttm.setValueAt(tor, x);
					}else{
						//System.out.println("adding row: "+x);
						ttm.addRow(tor);
					}
				}
				
				if(rowsRecieved > rowsPresent){
					//System.out.println("inserted rows,"+rowsPresent+","+(rowsRecieved-1));
					ttm.fireTableRowsInserted(rowsPresent, rowsRecieved-1);
				}else if(rowsRecieved < rowsPresent){
					//System.out.println("deleted rows,"+rowsRecieved+","+(rowsPresent-1));
					ttm.fireTableRowsDeleted(rowsRecieved, rowsPresent-1);
				}
				//System.out.println("updated rows,"+0+","+(rowsRecieved-1));
				ttm.fireTableRowsUpdated(0, rowsRecieved-1);
				
				
				
				try {
					Thread.sleep(500);
					//ttm.removeRow(ttm.getRowCount()-1);
					//Thread.sleep(1000);
					//System.out.println(table.getSelectedRow());
				} catch (InterruptedException e) {
					
				}
			}
			
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlRpcFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void viewChanged(String view) {
		ttm.clear();
		ttm.fireTableDataChanged();
		download_variable.setElementAt(view, 0);
		controllerTread.interrupt();
	}

	/*private void initTorrentTableExtensions(){
		ExtensionPoint ext = pluginManager.getRegistry().getExtensionPoint("ntorrent.torrenttable","TorrentTableExtension");
		for(Extension e : ext.getAvailableExtensions()){
			torrentTableExtensions.add(e.getDeclaringPluginDescriptor().getId());
			initTorrentTableExtension(e.getDeclaringPluginDescriptor());
		}
	}
	
	private void initTorrentTableExtension(PluginDescriptor p){
		try{
			if(pluginManager.isPluginActivated(p)){
				Plugin plugin = pluginManager.getPlugin(p.getId());
				((TorrentTableExtension)plugin).init(this);
			}
		}catch(PluginLifecycleException x){
			x.printStackTrace();
		}
	}*/
		
	/*public void pluginActivated(Plugin plugin) {
		if(torrentTableExtensions.contains(plugin.getDescriptor().getId())){
			initTorrentTableExtension(plugin.getDescriptor());
		}
		
	}

	public void pluginDeactivated(Plugin plugin) {}
	public void pluginDisabled(PluginDescriptor descriptor) {}
	public void pluginEnabled(PluginDescriptor descriptor) {}*/

	public void torrentActionPerformed(final Torrent[] tor, final String command) {
		//invoking the commands asynchronously so the gui won't be blocked
        new Thread(){
        	String[] mitems = TorrentTableJPopupMenu.mitems;
        	String[] priorityMenu = TorrentTableJPopupMenu.priorityMenu;
            public void run(){
        		Download d = connection.getDownloadClient();
        		for(Torrent t : tor){
            		String hash = t.getHash();
        			if(command.equals(mitems[0])){
        				//start
        				d.start(hash);
        			}else if(command.equals(mitems[1])){
        				//stop
        				d.stop(hash);
        			}else if(command.equals(mitems[2])){
        				//erase
        				d.erase(hash);
        			}else if(command.equals(mitems[3])){
        				//check hash
        				d.check_hash(hash);
        			}else if(command.equals(priorityMenu[1])){
        				//priority high
        				d.set_priority(hash,3);
        				//d.update_priorities(hash); might not need this here, only on individual file priorities.
        			}else if(command.equals(priorityMenu[2])){
        				//priority normal
        				d.set_priority(hash,2);
        				//d.update_priorities(hash);
        			}else if(command.equals(priorityMenu[3])){
        				//priority low
        				d.set_priority(hash,1);
        				//d.update_priorities(hash);
        			}else if(command.equals(priorityMenu[0])){
        				//priority off
        				d.set_priority(hash,0);
        				//d.update_priorities(hash);
        			}
        		}
            }
        }.start();
	}

	public void valueChanged(ListSelectionEvent e) {
		if(!e.getValueIsAdjusting()){
			int[] rows = table.getSelectedRows();
			Torrent[] tor = new Torrent[rows.length];
			for(int i = 0; i < rows.length; i++)
				tor[i] = ttm.getRow(i);
			
			for(TorrentSelectionListener tsl : torrentSelectionListeners){
				tsl.torrentsSelected(tor);
			}
		}
		
	}

	public void addTorrentSelectionListener(TorrentSelectionListener listener) {
		torrentSelectionListeners.add(listener);
	}
}
