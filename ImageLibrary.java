/**Chris Kepics
CIS111B Online - Fall2014
Group Members: Jessica Barwell, Michele Gravel**/

package sunpig;

import java.io.*;
import java.util.*;

public class ImageLibrary extends ImageList{
    private static final ImageLibrary singleton = new ImageLibrary();
    
    
    //Singleton Constructor
    private ImageLibrary(){
        super(loadList());
    }
    
    
    //Loads a previously saved ImageList, if able
    private static ArrayList loadList(){
        ArrayList<ImageObject> ll;
        try{
            FileInputStream fIn = new FileInputStream("ImageLibrary.dat");
            ObjectInputStream oIn = new ObjectInputStream(fIn);
            ll = (ArrayList<ImageObject>) oIn.readObject();
            oIn.close();
            fIn.close();
        }
        catch(Exception e){
            ll = new ArrayList<>();
        }
        
        return ll;
    }
    
    
    //For accessing the instance
    public static ImageLibrary getInstance(){
        return singleton;
    }
    
    
    
    @Override
    public String toString() {
    	return "Images";
    }
    
    
    //Before quitting, the program serializes the ImageLibrary
    public void save(){
        try{
            FileOutputStream fOut = new FileOutputStream("ImageLibrary.dat");
            ObjectOutputStream oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(imageList);
            oOut.close();
            fOut.close();
        }
        catch(Exception e){
            
        }
    }
}
