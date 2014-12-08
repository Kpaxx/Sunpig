/**Chris Kepics
CIS111B Online - Fall2014
Group Members: Jessica Barwell, Michele Gravel**/

package sunpig;

import java.awt.Component;
import java.awt.Color;
import javafx.scene.control.SelectionMode;
import javax.swing.*;
import javax.swing.table.*;

public class ImageTable extends JTable{
    private static String[] columnNames = { "Title", "Artist/Photographer", "Tags", "Rating", "Location", "Subject" };
    
    
    public ImageTable(ImageTableModel i){
        super(i);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setTransferHandler(new ImageTransferHandler());
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        setDragEnabled(true);
        
        adjustColumnWidth();
    }
    
    
    public void setModel(AbstractTableModel m){
        super.setModel(m);
        adjustColumnWidth();
    }
    
    
    private void adjustColumnWidth(){
        for (int column = 0; column < getColumnCount(); column++){
            TableColumn tableColumn = columnModel.getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < getRowCount(); row++)
            {
                TableCellRenderer cellRenderer = getCellRenderer(row, column);
                Component c = prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth+20, width);

                //  We've exceeded the maximum width, no need to check other rows
                if (preferredWidth >= maxWidth)
                {
                    preferredWidth = maxWidth;
                    break;
                }
            }

            tableColumn.setPreferredWidth( preferredWidth );
        }
    }
    
    
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
        Component comp = super.prepareRenderer(renderer, row, column);
        Color gray = new Color(240,240,240);
        Color white = Color.WHITE;
        if (!comp.getBackground().equals(getSelectionBackground())){
            Color bg;
            if(row % 2 == 0)
                bg = gray;
            else
                bg = white;
                        
            comp.setBackground(bg);
        }
        return comp;
    }
}
