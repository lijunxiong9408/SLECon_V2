package slecon.home.dashboard;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import logic.Dict;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import base.cfg.FontFactory;

import com.android.ninepatch.NinePatch;

import comm.Parser_Misc.OCSButton;

public class ControlSubPanel extends JPanel implements PropertyChangeListener {
    private static final long serialVersionUID = -5758521934211881609L;
    static HashMap<String, String>      styles                  = new HashMap<>();
    private MigLayout                  layout;
    
    static {
        styles.put( "lbl_control", "130 20 130 20 c" );
        styles.put( "jseparator_1", "130 45 130 1 c" );
        styles.put( "btnCHC", "130 51 130 32 c");
        styles.put( "btnDDO", "130 88 130 32 c" );
        styles.put( "lbl_shortcut", "520 20 130 20 c" );
        styles.put( "jseparator_2", "520 45 130 1 c" );
        styles.put( "btnTF", "520 51 130 32 c" );
        styles.put( "btnBF", "520 88 130 32 c" );
    }
    
    class OnOffButton extends JToggleButton {
        private NinePatch mPatch1;
        private NinePatch mPatch2;

        OnOffButton(String title) {
            super(title);
            this.setBorderPainted(false);
            this.setFocusPainted(false);
            this.setContentAreaFilled(false);
            
            try {
                mPatch1 = NinePatch.load(getClass().getClassLoader().getResourceAsStream("images/ShortCut_Button_On.png"), true, false);
                mPatch2 = NinePatch.load(getClass().getClassLoader().getResourceAsStream("images/ShortCut_Button_Off.png"), true, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            Rectangle clip = g2.getClipBounds();
            clip.setSize(getSize());

            if (getModel().isPressed()) {
                mPatch1.draw(g2, clip.x, clip.y, clip.width, clip.height);
                g2.setColor(StartUI.BORDER_COLOR);
                drawTextCenter(g2, getText(), clip);
            } else {
                if(isSelected()) {
                    mPatch1.draw(g2, clip.x, clip.y, clip.width, clip.height);
                    g2.setColor(StartUI.BORDER_COLOR);
                    drawTextCenter(g2, getText(), clip);
                } else {
                    mPatch2.draw(g2, clip.x, clip.y, clip.width, clip.height);
                    g2.setColor(Color.WHITE);
                    drawTextCenter(g2, getText(), clip);
                }
            }
        }
    }

    class SimpleButton extends JButton {
        private NinePatch mPatch1;
        private NinePatch mPatch2;

        SimpleButton(String title) {
            super(title);
            this.setBorderPainted(false);
            this.setFocusPainted(false);
            this.setContentAreaFilled(false);
            
            try {
                mPatch1 = NinePatch.load(getClass().getClassLoader().getResourceAsStream("images/ShortCut_Button_On.png"), true, false);
                mPatch2 = NinePatch.load(getClass().getClassLoader().getResourceAsStream("images/ShortCut_Button_Off.png"), true, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            Rectangle clip = g2.getClipBounds();
            clip.setSize(getSize());

            if (getModel().isPressed()) {
                mPatch1.draw(g2, clip.x, clip.y, clip.width, clip.height);
                g2.setColor(StartUI.BORDER_COLOR);
                drawTextCenter(g2, getText(), clip);
            } else {
            	mPatch2.draw(g2, clip.x, clip.y, clip.width, clip.height);
                g2.setColor(Color.WHITE);
                drawTextCenter(g2, getText(), clip);
            }
        }
    }
    
    final JButton btnTF = new SimpleButton("Top Floor");
    final JButton btnBF = new SimpleButton("Bottom Floor");
    final JToggleButton btnCHC = new OnOffButton("CHC");
    final JToggleButton btnDDO = new OnOffButton("DDO");
    final LiftStatusView parent;

    ControlSubPanel(LiftStatusView view) {
        this.parent = view;
        layout = new MigLayout("nogrid, w 780!, h 150!, gap 0", "[0!]", "[0!]");
        setLayout(layout);
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        JLabel lbl_control = new JLabel(LiftStatusView.TEXT.getString("AccessControl.text"));
        lbl_control.setForeground(Color.WHITE);
        lbl_control.setFont(FontFactory.FONT_12_PLAIN);
        JPanel jseparator_1 = new JPanel();
        jseparator_1.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR,1));
        add(lbl_control);
        add(jseparator_1);
        add(btnCHC);
        add(btnDDO);
        
        JLabel lbl_shortcut = new JLabel(LiftStatusView.TEXT.getString("CallShortcut.text"));
        lbl_shortcut.setForeground(Color.WHITE);
        lbl_shortcut.setFont(FontFactory.FONT_12_PLAIN);
        JPanel jseparator_2 = new JPanel();
        jseparator_2.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR,1));
        add(lbl_shortcut);
        add(jseparator_2);
        add(btnTF);
        add(btnBF);
        
        StartUI.setStyle(layout, styles, lbl_control, "lbl_control");
        StartUI.setStyle(layout, styles, jseparator_1, "jseparator_1");
        StartUI.setStyle(layout, styles, btnCHC, "btnCHC");
        StartUI.setStyle(layout, styles, btnDDO, "btnDDO");
        StartUI.setStyle(layout, styles, lbl_shortcut, "lbl_shortcut");
        StartUI.setStyle(layout, styles, jseparator_2, "jseparator_2");
        StartUI.setStyle(layout, styles, btnTF, "btnTF");
        StartUI.setStyle(layout, styles, btnBF, "btnBF");
        
        final ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (parent.misc != null) {
                    /* Permission */
                    if (!ToolBox.requestRole(parent.getConnectionBean(), LiftStatusView.WRITE_HOME_EXPRESSION)) {
                        ToolBox.showErrorMessage(Dict.lookup("NoPermission"));
                        return;
                    }

                    if (e.getSource() == btnTF) {
                        parent.misc.press(OCSButton.BS_TF, true);
                    } else if (e.getSource() == btnBF) {
                        parent.misc.press(OCSButton.BS_BF, true);
                    } else if (e.getSource() == btnCHC) {
                        parent.misc.press(OCSButton.BS_CHC, btnCHC.isSelected());
                    } else if (e.getSource() == btnDDO) {
                        parent.misc.press(OCSButton.BS_DDO, btnDDO.isSelected());
                    }
                }
            }
        };
        btnTF.addActionListener(listener);
        btnBF.addActionListener(listener);
        btnCHC.addActionListener(listener);
        btnDDO.addActionListener(listener);

        addPropertyChangeListener("enabled", this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        for (Component c : getComponents())
            c.setVisible(isEnabled());
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (!isEnabled()) {
            Font f = g.getFont();
            Color c = g.getColor();
            g.setColor(Color.GRAY);
            g.setFont(FontFactory.FONT_20_PLAIN);

            String str = Dict.lookup("Empty");
            Rectangle clip = g.getClipBounds();
            clip.setSize(getSize());
            drawTextCenter(g, str, clip);

            g.setFont(f);
            g.setColor(c);
        }
    }

    private static Rectangle2D drawTextCenter(Graphics g, String str, Rectangle bound) {
        final FontMetrics fontMetrics = g.getFontMetrics();
        Rectangle2D stringBounds = fontMetrics.getStringBounds(str, g);

        int stringW = (int) stringBounds.getWidth();
        int stringH = (int) stringBounds.getHeight();
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

        g.setFont(FontFactory.FONT_12_PLAIN);
        g.drawString(str, bound.x + (bound.width - stringW) / 2, bound.y+(bound.height - stringH) / 2 + fontMetrics.getAscent());
        return stringBounds;
    }
}
