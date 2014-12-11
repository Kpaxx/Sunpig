/**Chris Kepics
CIS111B Online - Fall2014
Group Members: Jessica Barwell, Michele Gravel**/

package sunpig;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class ImageTable extends JTable{
    
    
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
    
    
    //This basically figures out the cell in each column that has the most characters
    //And resizes that column to more or less fit the text in that cell
    private void adjustColumnWidth(){
        for (int column = 0; column < getColumnCount(); column++){
            TableColumn tableColumn = columnModel.getColumn(column);
            int stringWidth = 10;
            
            String s = getModel().getColumnName(column);
            if(s.length() > stringWidth)
                stringWidth = s.length();

            for (int row = 0; row < getRowCount(); row++)
            {
                s = getModel().getValueAt(row, column).toString();
                if(s.length() > stringWidth)
                    stringWidth = s.length()+1;
            }

            tableColumn.setPreferredWidth(stringWidth*7);
        }
    }
    
    
    //Alternates the color of each row of the table for easier reading
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
