/*
 * Chris Kepics
 * CS 111 - Final Project
 */
package sunpig;

import java.io.*;
import java.util.ArrayList;

public class PlaylistList {
    private static final PlaylistList singleton = new PlaylistList();
    private ArrayList<ImagePlaylist> pList;
    
    
    //Singleton Constructor
    private PlaylistList(){
        try{
            FileInputStream fIn = new FileInputStream("ImagePlaylists.dat");
            ObjectInputStream oIn = new ObjectInputStream(fIn);
            pList = (ArrayList<ImagePlaylist>) oIn.readObject();
            oIn.close();
            fIn.close();
        }
        catch(Exception e){
            pList = new ArrayList<>();
        }
    }
    
    
    //For accessing the instance
    public static PlaylistList getInstance(){
        return singleton;
    }
    
    
    //Before quitting, the program serializes the ImageLibrary
    public void quit(){
        try{
            FileOutputStream fOut = new FileOutputStream("ImagePlaylists.dat");
            ObjectOutputStream oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(pList);
            oOut.close();
            fOut.close();
        }
        catch(Exception e){
            
        }
    }
    
    public void addPlaylist(String name){
        pList.add(new ImagePlaylist(name));
    }
    
    public void removePlaylist(int index){
        pList.remove(index);
    }
    
    public ArrayList<ImagePlaylist> getPlaylistList(){
        return pList;
    }
    
    public ImagePlaylist getPlaylist(int index){
        return pList.get(index);
    }
}
