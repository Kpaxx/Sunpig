/**Chris Kepics
CIS111B Online - Fall2014
Group Members: Jessica Barwell, Michele Gravel**/

package sunpig;

import java.io.*;
import java.util.*;
import javax.swing.table.*;

public abstract class ImageList implements Serializable, ImageConstants{
    protected ArrayList<ImageObject> imageList;
    protected int currentSort = SORT_ARTIST;
    
    
    //Constructor
    protected ImageList(ArrayList i){
        imageList = i;
    }
    
    //Copy Constructor
    protected ImageList(ImageList i){
        imageList = i.getList();
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
    
    
    //Adds an image to the imageList
    public void addImage(ImageObject i){
        imageList.add(i);
        Collections.sort(imageList, new ImageComparer(currentSort));
    }
    
    
    //Removes an image from the imageList
    public void removeImage(int index){
        imageList.remove(index);
    }
    
    
}
