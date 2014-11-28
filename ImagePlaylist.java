/*
 * Chris Kepics
 * CS 111 - Final Project
 */
package sunpig;

import java.io.*;
import java.util.ArrayList;

public class ImagePlaylist extends ImageList{
    public String name;
    
    
    //Constructor
    public ImagePlaylist(String n){
        super(loadList(n));
        name = n;
    }
    
    //Copy Constructor
    public ImagePlaylist(ImageList i){
        super(i);
        name = "";
    }
    
    //No-Arg Constructor
    private ImagePlaylist(){
        super();
        name = "";
    }
    
    
    //Loads a previously saved ImageList, if able
    private static ArrayList loadList(String n){
        ArrayList<Image> ll;
        try{
            FileInputStream fIn = new FileInputStream("ImagePlaylist-" + n + ".dat");
            ObjectInputStream oIn = new ObjectInputStream(fIn);
            ll = (ArrayList<Image>) oIn.readObject();
            oIn.close();
            fIn.close();
        }
        catch(Exception e){
            ll = new ArrayList<>();
        }
        
        return ll;
    }
    
    
    //Returns list name
    public String getName(){
        return name;
    }
    
    @Override
    public String toString() {
    	return getName();
    }
    
    
    public static ImagePlaylist toPlaylist(ArrayList<Image> iList){
        ImagePlaylist list = new ImagePlaylist();
        for(Image i : iList)
            list.addImage(i);
        
        return list;
    }
}
