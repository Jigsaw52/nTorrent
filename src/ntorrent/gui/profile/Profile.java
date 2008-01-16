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
package ntorrent.gui.profile;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;

import ntorrent.gui.GuiAction;
import ntorrent.io.settings.Constants;

public class Profile extends JPanel implements ActionListener {
	ProfileForm form;
	ProfileList list;
	
	public Profile() {
		JPanel buttonpanel = new JPanel();
		
		/** buttons **/
		Action[] buttons = {
			new GuiAction("connect",this),
			new GuiAction("profile.save",this),
			new GuiAction("profile.delete",this)
		};
		
		/** setting the layout mgr**/
        setLayout(new BorderLayout());
        
        form = new ProfileForm();
        list = new ProfileList(form);
        
        add(form,BorderLayout.CENTER);
        add(new JScrollPane(list),BorderLayout.EAST);
        add(buttonpanel,BorderLayout.SOUTH);
        
        for(int x = 0; x < buttons.length; x++)
        	buttonpanel.add(new JButton(buttons[x]));
	}

    public static void main(String[] args) {
    	JFrame f = new JFrame();
    	f.setContentPane(new JPanel());
    	f.getContentPane().add(new Profile());
    	f.pack();
    	f.setVisible(true);
    	f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("connect")){
		
		}else if(e.getActionCommand().equals("profile.save")){
			String name = JOptionPane.showInputDialog(this, Constants.messages.getString("profile.name"));
			form.getProfile(name);
		}else if(e.getActionCommand().equals("profile.delete")){
			list.deleteSelected();
		}
	}
}
