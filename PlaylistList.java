/**Chris Kepics
CIS111B Online - Fall2014
Group Members: Jessica Barwell, Michele Gravel**/

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
            System.err.println("ERROR: Playlist not loaded");
            pList = new ArrayList<>();
        }
    }
    
    
    //For accessing the instance
    public static PlaylistList getInstance(){
        return singleton;
    }
    
    
    //Before quitting, the program serializes the PlaylistList
    public void save(){
        try{
            FileOutputStream fOut = new FileOutputStream("ImagePlaylists.dat");
            ObjectOutputStream oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(pList);
            oOut.close();
            fOut.close();
        }
        catch(Exception e){
            System.err.println("ERROR: Playlists not saved");
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
