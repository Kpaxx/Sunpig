/**Chris Kepics
CIS111B Online - Fall2014
Group Members: Jessica Barwell, Michele Gravel**/

package sunpig;

import javax.swing.table.AbstractTableModel;

public class ImageTableModel extends AbstractTableModel {
    
    private String[] columnTitles = new String[] {"Title","Artist/Photographer","Tags","Rating","Location","Subject","Date Added","Page"};
    
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
        return columnTitles[column];
    }
    
    
    //The first column in the table actually contains the ImageObjects themselves, to allow easier access to
    //the objects directly through the table. The ImageObject's toString method returns the title of the image.
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
            case 6:
                return img.getDate();
            case 7:
                return img.printPageNum();
            default:
                return "";
        }
    }

    //Date is not editable. Everything else is.
    public boolean isCellEditable(int row, int col) {
        if(col==6)
            return false;
        
        return true;
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
            case 7:
                img.setPageNum((String)aValue);
                break;
        }
        
        ImageLibrary.getInstance().save();
        PlaylistList.getInstance().save();
    }
    
    
    public int getColumnCount(){
        return columnTitles.length;
    }
    
    
    public int getRowCount(){
        return currentList.getList().size();
    }
}
