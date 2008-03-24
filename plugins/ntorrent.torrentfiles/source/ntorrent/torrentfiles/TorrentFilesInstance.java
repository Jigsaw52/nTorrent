package ntorrent.torrentfiles;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.tree.TreeNode;

import ntorrent.io.rtorrent.Download;
import ntorrent.io.rtorrent.File;
import ntorrent.io.xmlrpc.XmlRpcConnection;
import ntorrent.locale.ResourcePool;
import ntorrent.session.ConnectionSession;
import ntorrent.session.SessionInstance;
import ntorrent.session.view.SessionFrame;
import ntorrent.torrentfiles.model.TorrentFile;
import ntorrent.torrentfiles.model.TorrentFilesTreeTableModel;
import ntorrent.torrentfiles.model.TreeTableModelAdapter;
import ntorrent.torrentfiles.view.JTreeTable;
import ntorrent.torrentfiles.view.TorrentFilesPopupMenu;
import ntorrent.torrenttable.TorrentTableInterface;
import ntorrent.torrenttable.model.Priority;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentSelectionListener;
import redstone.xmlrpc.XmlRpcArray;
import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcFault;

/**
 * @author Kim Eik
 *
 */
public class TorrentFilesInstance implements SessionInstance,TorrentSelectionListener, ActionListener {

	 private TorrentFilesTreeTableModel treeModel = new TorrentFilesTreeTableModel();
	final private JTreeTable treeTable = new JTreeTable(treeModel);
	final private JScrollPane scrollpane = new JScrollPane(treeTable);
	final private TorrentFilesPopupMenu popup = new TorrentFilesPopupMenu(this,treeTable);
	
	final private JTabbedPane container;
	final private TorrentTableInterface tableController;
	
	final private XmlRpcClient client;
	final private File f;
	final private Download d;
	
	
	private boolean started;
	
	public TorrentFilesInstance(ConnectionSession session) {
		SessionFrame frame = session.getDisplay();
		
		//init needed variables
		container = frame.getTabbedPane();
		tableController = session.getTorrentTableController();
		
		XmlRpcConnection connection = session.getConnection();
		client = connection.getClient();
		f = connection.getFileClient();
		d = connection.getDownloadClient();
		
		//add mouselistener for popup
		treeTable.addMouseListener(popup);
	}
	
	public void start(){
		started = true;
		int preferredIndex = 1;
		if (preferredIndex > container.getTabCount())
			preferredIndex = container.getTabCount();
		
		container.insertTab(ResourcePool.getString("tabname", "locale", this), null, scrollpane,null,preferredIndex);
		
		//add this as a selection listener
		tableController.addTorrentSelectionListener(this);
	}
	
	public void stop(){
		started = false;
		int index = container.indexOfComponent(scrollpane);
		container.removeTabAt(index);
		
		//remove this as a selection listener
		tableController.removeTorrentSelectionListener(this);
	}

	public void torrentsSelected(Torrent[] tor) {
		if(tor.length == 1){
			treeModel = new TorrentFilesTreeTableModel();
			String hash = tor[0].getHash();
			try {
				XmlRpcArray result = (XmlRpcArray) client.invoke("f.multicall", 
						new Object[]{
						hash,
						"",
						"f.get_path_components=",
						"f.get_priority=",
						"f.get_completed_chunks=",
						"f.get_size_chunks=",
						"f.get_size_bytes=",
						/*"f.get_is_created=", this api was changed in 0.8.0, must therefore be removed until a more stable api is out.
						"f.get_is_open=",*/  
						"f.get_last_touched="
						});
			
				//System.out.println(result);
				for(int row = 0 ; row < result.size(); row++){
				XmlRpcArray rowArray = (XmlRpcArray) result.get(row);
					XmlRpcArray paths = (XmlRpcArray) rowArray.get(0);
					TorrentFile parent = (TorrentFile) treeModel.getRoot();
					for(int x = 0; x < paths.size(); x++){
						String name = paths.getString(x);
						TorrentFile tf = getNode(name,x+1);
						if(tf == null){
							//System.out.println("make the node where parent="+parent);
							tf = new TorrentFile(name,hash);
							//filter out directories.
							if(x+1 == paths.size()){
								//set the priority
								tf.setPriority(rowArray.getLong(1));
								//set the precent
								long complete = rowArray.getLong(2);
								long done = rowArray.getLong(3);
								tf.setPercent((int)(complete*100/done));
								//set size
								tf.setSize(rowArray.getLong(4));
								//set created and open
								//tf.setCreated(rowArray.getLong(5) == 1 ? true : false);
								//tf.setOpen(rowArray.getLong(6) == 1 ? true : false);
								//set last touched
								tf.setLastTouched(""+new Date(rowArray.getLong(5)/1000));
								//set offset
								tf.setOffset(row);
							}
						}
						
						//System.out.println("inserting "+tf+" into "+parent);
						if(!parent.isNodeChild(tf))
							parent.insert(tf, parent.getChildCount());
						parent = tf;
						
					}
				}

			} catch (XmlRpcException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlRpcFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			treeTable.setModel(treeModel);
			TreeTableModelAdapter model = (TreeTableModelAdapter)treeTable.getModel();
			model.fireTableDataChanged();
			treeTable.setWidths();
		}	
	}
	
	private TorrentFile getNode(String name, int depth){
		Enumeration<TreeNode> children = ((TreeNode)treeModel.getRoot()).children();
		while(children.hasMoreElements()){
			TorrentFile tf = (TorrentFile) children.nextElement();
			//System.out.println("name="+name+" tfname="+tf.getName()+" depth="+depth+" tdepth="+tf.getDepth());
			if(tf.getName().equals(name) && tf.getDepth() == depth)
				return tf;
		}
		return null;
	}
	
	public boolean isStarted() {
		return started;
	}

	public void actionPerformed(ActionEvent e) {
		final String[] pri = TorrentFilesPopupMenu.priority;
		final String cmd = e.getActionCommand();
		
		new Thread(){
			final HashSet<TorrentFile> hashset = new HashSet<TorrentFile>();
			
			public void run(){
				for(int x = 0; x < pri.length; x++){
					if(pri[x].equals(cmd)){
						for(int row : treeTable.getSelectedRows()){
							TorrentFile tf = (TorrentFile)treeTable.getValueAt(row, 2);
							setPriority(tf, x);
						}
						
						String hash = null;
						for(TorrentFile tf : hashset){
							if(hash == null)
								hash = tf.getParentHash();
							//System.out.println(hash+" "+x+" "+tf.getOffset());
							f.set_priority(hash,tf.getOffset(), x);
							((Priority)treeModel.getValueAt(tf, 0)).setPriority(x);
						}
						
						d.update_priorities(hash);
						((TreeTableModelAdapter)treeTable.getModel()).fireTableDataChanged();
						
						break;
					}

				}
				
			}
			private final void setPriority(TorrentFile tf, int pri){
				if(!tf.isLeaf()){
					Enumeration<TorrentFile> children = tf.children();
					while(children.hasMoreElements()){
						setPriority(children.nextElement(), pri);
					}
				}else{
					hashset.add(tf);
				}
			}
		}.start();
	}
}
