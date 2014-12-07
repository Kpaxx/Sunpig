/**Chris Kepics
CIS111B Online - Fall2014
Group Members: Jessica Barwell, Michele Gravel**/

package sunpig;

import javax.swing.table.AbstractTableModel;

public class ImageTableModel extends AbstractTableModel {
    
    private ImageList currentList;
    
    public ImageTableModel(ImageList il){
        currentList = il;
    }
    
    public void setCurrentList(ImageList il){
        currentList = il;
    }
    
    public ImageList getCurrentList(){
        return currentList;
    }
    
    public String getColumnName(int column){
        switch (column){
            case 0:
                return "Title";
            case 1:
                return "Artist/Photographer";
            case 2:
                return "Tags";
            case 3:
                return "Rating";
            case 4:
                return "Location";
            case 5:
                return "Subject";
            default:
                return "";
        }
    }
    
    
    public Object getValueAt(int row, int col){
        if(row >= currentList.getList().size())
            return "";
        
        ImageObject img = ((ImageObject)currentList.getList().get(row));
        
        switch (col){
            case 0:
                return img;
            case 1:
                return img.getArtist();
            case 2:
                return img.printTags();
            case 3:
                return img.printRating();
            case 4:
                return img.getLocation();
            case 5:
                return img.getSubject();
            default:
                return "";
        }
    }
    
    
    public void setValueAt(Object aValue,  int row, int col){
        
        ImageObject img = ((ImageObject)currentList.getList().get(row));
        
        switch (col){
            case 0:
                img.setTitle((String)aValue);
                break;
            case 1:
                img.setArtist((String)aValue);
                break;
            case 2:
                img.setTags((String)aValue);
                break;
            case 3:
                img.setRating((String)aValue);
                break;
            case 4:
                img.setLocation((String)aValue);
                break;
            case 5:
                img.setSubject((String)aValue);
                break;
        }
    }
    
    
    public int getColumnCount(){
        return 6;
    }
    
    
    public int getRowCount(){
        return currentList.getList().size();
    }
}
