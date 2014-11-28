/**Chris Kepics
CIS111B Online - Fall2014
Group Members: Jessica Barwell, Michele Gravel**/

package sunpig;

import java.awt.Component;
import java.awt.Color;
import javax.swing.*;
import javax.swing.table.*;

public class ImageTable extends JTable{
    private static String[] columnNames = { "Title", "Artist/Photographer", "Tags", "Rating", "Location", "Subject" };
    
    
    public ImageTable(ImageList i){
        super(i);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setDragEnabled(true);
        
        //Demonstration! Plz remove later
        demo();
        
        
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
    
    private void demo(){
        //Note that no matter what order you add the images, they will default to sorting by Artist
        
        Image MonaLisa = new Image("/MonaLisa.jpg");
        MonaLisa.setTitle("Mona Lisa");
        MonaLisa.setArtist("Leonardo DaVinci");
        MonaLisa.setTags("oil paint, Louvre");
        MonaLisa.setRating("****");
        MonaLisa.setLocation("");
        MonaLisa.setSubject("");
        ImageLibrary.getInstance().addImage(MonaLisa);
        
        Image LastSupper = new Image("/LastSupper.png");
        LastSupper.setTitle("The Last Supper");
        LastSupper.setArtist("Leonardo DaVinci");
        LastSupper.setTags("oil paint, Jesus, fresco");
        LastSupper.setRating("2");
        LastSupper.setLocation("");
        ImageLibrary.getInstance().addImage(LastSupper);
        
        Image TN1 = new Image("/IMG_106.jpg");
        TN1.setTitle("IMG_106");
        TN1.setArtist("Chris Kepics");
        TN1.setTags("vacation, waterfall");
        TN1.setRating("*");
        TN1.setLocation("Tennessee");
        TN1.setSubject("Rachel Kepics");
        ImageLibrary.getInstance().addImage(TN1);
        
        Image TN2 = new Image("/IMG_105.jpg");
        TN2.setTitle("IMG_105");
        TN2.setArtist("Chris Kepics");
        TN2.setTags("vacation, mountains");
        TN2.setRating("5");
        TN2.setLocation("Tennessee");
        TN2.setSubject("");
        ImageLibrary.getInstance().addImage(TN2);
        
        Image SN = new Image("/StarryNight.jpg");
        SN.setTitle("Starry Night");
        SN.setArtist("Vincent VanGogh");
        SN.setTags("oil painting, mountains, wind, impressionism");
        SN.setRating("0");
        SN.setLocation("");
        SN.setSubject("");
        ImageLibrary.getInstance().addImage(SN);
        
        Image VM = new Image("/VitruvianMan.jpg");
        VM.setTitle("The Vitruvian Man");
        VM.setArtist("Leonardo DaVinci");
        VM.setTags("anatomy");
        VM.setRating("3");
        VM.setLocation("");
        VM.setSubject("");
        ImageLibrary.getInstance().addImage(VM);
        
        
        //Sort by other things here!
        //ImageLibrary.getInstance().setCurrentSort(ImageConstants.SORT_RATING);
    }
}
