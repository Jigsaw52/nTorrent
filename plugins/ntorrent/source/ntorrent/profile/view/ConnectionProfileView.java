package ntorrent.profile.view;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginRegistry;

import ntorrent.NtorrentApplication;
import ntorrent.gui.Window;

public class ConnectionProfileView extends JPanel implements ItemListener{
	
	private final static DefaultComboBoxModel boxModel = new DefaultComboBoxModel();
	private final JComboBox box = new JComboBox(boxModel);
	
	/**
	 * Log4j logger
	 */
	private final static Logger log = Logger.getLogger(ConnectionProfileView.class);
	
	public ConnectionProfileView() {
		
		if(boxModel.getSize() == 0){
			/** Get extensions but only if boxModel is empty**/
			PluginRegistry registry = NtorrentApplication.MANAGER.getRegistry();
			ExtensionPoint extension = registry.getExtensionPoint("ntorrent@ConnectionProfileExtension");
			for(Extension ext : extension.getAvailableExtensions()){
				log.info(ext.getId());
				boxModel.addElement(ext);
			}
		}
		box.addItemListener(this);
		add(box);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		log.info(e);
	}
}
