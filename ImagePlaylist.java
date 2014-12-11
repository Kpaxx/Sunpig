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
    
    
    //Returns list name
    public String getName(){
        return name;
    }
    
    @Override
    public String toString() {
    	return getName();
    }
}
