package slecon.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import slecon.StartUI;
import slecon.home.dashboard.VerticalSrcollBarUI;

@SuppressWarnings("serial")
public class MyComboBox extends JComboBox{
	public MyComboBox() {
		init();
	}
	public MyComboBox(ComboBoxModel model){
		super(model);
		init();
	 }
	
	 public MyComboBox(Object[] items){
		 super(items);
		 init();
	 }
	 
	 public MyComboBox(Vector<?> items){
		 super(items);
		 init();
	 }
	 
	 public MyComboBox(Dimension d) {
		 super.setPreferredSize(d);
	 }
	 
	 private void init(){
		setOpaque(false);
	  	setUI(new IComboBoxUI());
	  	//setBorder(new SubtleSquareBorder(true, StartUI.BORDER_COLOR));
	  	setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
	  	setRenderer(new IComboBoxRenderer());
	  	setBackground(StartUI.SUB_BACKGROUND_COLOR);
	  	setForeground(Color.WHITE);
	  	setFocusable(false);
	 }
	 
	 public Dimension getPreferredSize(){
		 return super.getPreferredSize();
	 }
	 
	 public class IComboBoxRenderer implements ListCellRenderer {
		 private DefaultListCellRenderer defaultCellRenderer = new DefaultListCellRenderer();
		 public IComboBoxRenderer() {
			 super();
		 }

		 public Component getListCellRendererComponent(JList list, Object value,
				 int index, boolean isSelected, boolean cellHasFocus) {

			  JLabel renderer = (JLabel)defaultCellRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			  renderer.setForeground(Color.WHITE);
			  if(isSelected){
				  renderer.setBackground(StartUI.BORDER_COLOR);
			  }else{
				  renderer.setBackground(StartUI.SUB_BACKGROUND_COLOR);
			  }
			  
			  list.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
			  renderer.setFont(FontFactory.FONT_12_PLAIN);
			  renderer.setHorizontalAlignment(JLabel.CENTER);
			  return renderer;
		 }
	}
	 
	public class IComboBoxUI extends BasicComboBoxUI {
		private JButton arrow;
		private boolean boundsLight = false;
		private static final int ARCWIDTH = 15;
		private static final int ARCHEIGHT = 15;

		public IComboBoxUI() {
			super();
		}

		protected JButton createArrowButton() {
			arrow = new JButton();
			arrow.setIcon(ImageFactory.ARROW_COMBBOX_DOWN.icon(10, 10));
			arrow.setRolloverEnabled(true);
			//arrow.setRolloverIcon(ImageFactory.TRIANGLE_UP.icon());
			arrow.setBorder(null);
			arrow.setBackground(StartUI.SUB_BACKGROUND_COLOR);
			arrow.setOpaque(false);
			arrow.setContentAreaFilled(false);
			return arrow;
		}

		public void paint(Graphics g, JComponent c) {
			hasFocus = comboBox.hasFocus();
			Graphics2D g2 = (Graphics2D)g;
			if (!comboBox.isEditable()) {
				Rectangle r = rectangleForCurrentValue();
				paintCurrentValueBackground(g2, r, hasFocus);
				paintCurrentValue(g2, r, hasFocus);
			}
		  
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			int width = (int) this.getPreferredSize(c).getWidth() -2;
			int height = 0;
			int heightOffset = 0;
			if (comboBox.isPopupVisible()) {
				heightOffset = 5;
				height = (int) this.getPreferredSize(c).getHeight();
				arrow.setIcon(ImageFactory.ARROW_COMBBOX_DOWN.icon(10, 10));
			} else {
				heightOffset = 0;
				height = (int) this.getPreferredSize(c).getHeight() - 1;
				arrow.setIcon(ImageFactory.ARROW_COMBBOX_DOWN.icon(10, 10));
			}
			if (comboBox.isFocusable()) {
				g2.setColor(StartUI.BORDER_COLOR);
			}
			//g2.drawRoundRect(0, 0, width, height + heightOffset,ARCWIDTH,ARCHEIGHT);
		}

		public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
			Font oldFont = comboBox.getFont();
			comboBox.setFont(FontFactory.FONT_12_PLAIN);
			super.paintCurrentValue(g, bounds, hasFocus);
			comboBox.setFont(oldFont);
		}

		public Dimension getPreferredSize(JComponent c) {
			return super.getPreferredSize(c);
		}

		public boolean isBoundsLight() {
			return boundsLight;
		}

		public void setBoundsLight(boolean boundsLight) {
			this.boundsLight = boundsLight;
		}

		protected ComboPopup createPopup() {
			/*
			ComboPopup popup = new BasicComboPopup(comboBox) {
				
		   };*/
			MyComboPopup myPopup = new MyComboPopup(comboBox);
			myPopup.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
			return myPopup;
		}
	}
	
	class MyComboPopup extends BasicComboPopup{
		
		MyComboPopup(JComboBox combo) {
			super(combo);
		}

		@Override
		protected JScrollPane createScroller() {
			JScrollPane jScrollPane =  super.createScroller();
			jScrollPane.setHorizontalScrollBar(null);
			jScrollPane.getVerticalScrollBar().setUI(new VerticalSrcollBarUI());
			return jScrollPane;
		}
	}
	
}


