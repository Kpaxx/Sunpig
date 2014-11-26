/**Chris Kepics
CIS111B Online - Fall2014
Group Members: Jessica Barwell, Michele Gravel**/

package sunpig;

import java.io.*;
import java.util.*;
import javax.swing.table.*;

public abstract class ImageList extends AbstractTableModel implements Serializable, ImageConstants{
    protected ArrayList<Image> imageList;
    protected int currentSort = SORT_ARTIST;
    
    
    //Constructor
    protected ImageList(ArrayList i){
        imageList = i;
    }
    
    
    //Constructor
    protected ImageList(){
        imageList = new ArrayList();
    }
    
    
    //Sets currentSort, which keeps track of how the ImageList is currently sorted
    public void setCurrentSort(int s){
        currentSort = s;
        Collections.sort(imageList, new ImageComparer(currentSort));
    }
    
    
    //Returns the full ImageList
    public ArrayList getList(){
        return imageList;
    }
    
    
    //Returns an ArrayList containing only the images that match the strings in s
    public ArrayList search(ArrayList<String> s){
        ArrayList<Image> found = new ArrayList<>();
        
        for (Image i : imageList){
            if(match(i, s))
                found.add(i);
        }
        
        return found;
    }
    
    
    //Returns true if all of the Strings in s match any of the string or tag fields in Image i
    private boolean match(Image i, ArrayList<String> s){
        /*
        *  matchCounter keeps track of how many of the Strings in s match one or more fields in Image i
        *  If, in the end, matchCounter >= s.size(), you know that all of the Strings in s matched fields in i.
        */
        int matchCounter = 0;
        for(String str : s){
            if(i.getTitle().contains(str))
                matchCounter++;
            else if(i.getArtist().contains(str))
                matchCounter++;
            else if(i.getLocation().contains(str))
                matchCounter++;
            else if(i.getSubject().contains(str))
                matchCounter++;
            else if(i.printPageNum().contains(str))
                matchCounter++;
        }
        
        ArrayList<String> tags = i.getTags();
        for(String str : s)
            for(String t : tags)
                if(t.contains(str))
                    matchCounter++;
        
        if(matchCounter >= s.size())
            return true;
        
        return false;
    }
    
    
    //Adds an image to the imageList
    public void addImage(Image i){
        imageList.add(i);
        Collections.sort(imageList, new ImageComparer(currentSort));
    }
    
    
    //Removes an image from the imageList
    public void removeImage(int index){
        imageList.remove(index);
    }
    
    
    //PAST THIS POINT - ABSTRACTTABLEMODEL METHODS
    
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
    
    
    public String getValueAt(int row, int col){
        if(row >= imageList.size())
            return "";
        
        switch (col){
            case 0:
                return imageList.get(row).getTitle();
            case 1:
                return imageList.get(row).getArtist();
            case 2:
                return imageList.get(row).printTags();
            case 3:
                return imageList.get(row).printRating();
            case 4:
                return imageList.get(row).getLocation();
            case 5:
                return imageList.get(row).getSubject();
            default:
                return "";
        }
    }
    
    
    public void setValueAt(Object aValue,  int rowIndex, int columnIndex){
        switch (columnIndex){
            case 0:
                imageList.get(rowIndex).setTitle((String)aValue);
                break;
            case 1:
                imageList.get(rowIndex).setArtist((String)aValue);
                break;
            case 2:
                imageList.get(rowIndex).setTags((String)aValue);
                break;
            case 3:
                imageList.get(rowIndex).setRating((String)aValue);
                break;
            case 4:
                imageList.get(rowIndex).setLocation((String)aValue);
                break;
            case 5:
                imageList.get(rowIndex).setSubject((String)aValue);
                break;
        }
    }
    
    
    public int getColumnCount(){
        return 6;
    }
    
    
    public int getRowCount(){
        return imageList.size();
    }
}
