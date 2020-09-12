package slecon.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

import base.cfg.ImageFactory;
import slecon.home.action.PreferenceDialog.ItemChangedListener;

public class MyCheckBox extends JCheckBox{

	private ImageIcon	SELECT_ICON 	= ImageFactory.SELECTOR.icon(17, 17);
	private ImageIcon	DISSELECT_ICON 	= ImageFactory.DISSELECTOR.icon(17, 17);
	private ImageIcon	DEFAULTICON 	= ImageFactory.DISSELECTOR.icon(17, 17);
	
	private static final long serialVersionUID = 1063329959329038749L;

	public MyCheckBox() {
		this.setIcon(DEFAULTICON);
		this.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				JCheckBox cbx = (JCheckBox)e.getItem() ;
				if(cbx.isSelected())
		        {
					cbx.setIcon(SELECT_ICON) ;
		        }else {
		        	cbx.setIcon(DISSELECT_ICON) ;
		        } 
			}
		});
	}
	
	public MyCheckBox(String text) {
		super(text);
		this.setIcon(DEFAULTICON);
		this.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				JCheckBox cbx = (JCheckBox)e.getItem() ;
				if(cbx.isSelected())
		        {
					cbx.setIcon(SELECT_ICON) ;
		        }else {
		        	cbx.setIcon(DISSELECT_ICON) ;
		        } 
			}
		});
	}
	
}
