/**Chris Kepics
CIS111B Online - Fall2014
Group Members: Jessica Barwell, Michele Gravel**/

package sunpig;

import java.io.*;
import java.util.*;

public abstract class ImageList implements Serializable{
    protected ArrayList<ImageObject> imageList;
    
    
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
    
    
    //Returns the full ImageList
    public ArrayList getList(){
        return imageList;
    }
    
    
    //Adds an image to the imageList
    public void addImage(ImageObject i){
        imageList.add(i);
    }
    
    
    //Removes an image from the imageList
    public void removeImage(int index){
        imageList.remove(index);
    }
    
    
}
