package slecon.home.dashboard;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;

import logic.Dict;
import logic.evlog.ErrorLog;
import slecon.StartUI;
import base.cfg.FontFactory;




public final class LogSubView extends JList<ErrorLog> {
    private static final long serialVersionUID = 5660685132385463957L;




    public LogSubView ( ListModel<ErrorLog> dataModel ) {
        super( dataModel );
        setCellRenderer( new DefaultListCellRenderer() {
            private static final long serialVersionUID = -1848412915194664258L;
            @Override
            public Component getListCellRendererComponent ( JList<?> list, Object value, int index, boolean isSelected,
                                                            boolean cellHasFocus ) {
                JLabel label = ( JLabel )super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
                if ( value instanceof ErrorLog ) {
                    label.setText( ( ( ErrorLog )value ).toSimpleString() );
                    label.setForeground( Color.WHITE );
                }
                if ( isSelected || cellHasFocus ) {
                    label.setBackground( StartUI.BORDER_COLOR );
                }else {
                	label.setBackground( StartUI.SUB_BACKGROUND_COLOR );
                }
                return label;
            }
        } );
    }
    

    public void paint ( Graphics g ) {
        super.paint( g );
        if ( getModel().getSize() == 0 ) {
            Font f = g.getFont();
            Color c = g.getColor();
            g.setColor( StartUI.SUB_BACKGROUND_COLOR );
            g.fillRect(0,0,getSize().width,getSize().height);
            
            g.setColor( Color.WHITE);
            g.setFont( FontFactory.FONT_20_PLAIN );
            String str = Dict.lookup( "Empty" );
            final FontMetrics fontMetrics = g.getFontMetrics();
            Rectangle2D stringBounds = fontMetrics.getStringBounds( str, g );
            int stringW = ( int )stringBounds.getWidth();
            int stringH = ( int )stringBounds.getHeight();
            g.drawString( str, ( getWidth() - stringW ) / 2, ( getHeight() - stringH ) / 2 + fontMetrics.getAscent() );

            g.setFont( f );
            g.setColor( c );
        }
    }

/*
    public String getToolTipText ( MouseEvent evt ) {
        int index = locationToIndex( evt.getPoint() );
        if ( index == -1 )
            return null;

        ErrorLog     item = getModel().getElementAt( index );
        StringBuffer sb   = new StringBuffer();
        sb.append( "<html><head><style type='text/css'>body { font-family: " );
        sb.append( Font.MONOSPACED );
        sb.append( "; } </style></head>" );
        sb.append( item.toDetailString().replaceAll( " ", "&nbsp;" ).replaceAll( "\n", "<br/>" ) );
        return sb.toString();
    }
*/
}
