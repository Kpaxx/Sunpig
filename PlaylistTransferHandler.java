/**Chris Kepics
CIS111B Online - Fall2014
Group Members: Jessica Barwell, Michele Gravel**/

package sunpig;

import javax.swing.*;
import java.util.ArrayList;
import java.awt.datatransfer.*;
import java.io.IOException;

public class PlaylistTransferHandler extends TransferHandler{
    
    DataFlavor imageListFlavor = new DataFlavor(ArrayList.class, "ArrayList");
    
    public boolean canImport(TransferSupport support){
        return true;/*
        if(!support.isDrop() || !(support.getComponent() instanceof ImageTable) || !(support.getComponent() instanceof JList))
            return false;
        
        return isSupportedType(support);*/
    }
    
    
    public boolean isSupportedType(TransferSupport support){
        DataFlavor[] iflavors = support.getDataFlavors();
        for(DataFlavor dFlavor : iflavors){
            if(dFlavor.match(imageListFlavor))
                return true;
        }
        return false;
    }
    
    
    public int getSourceActions(JComponent c){
        return TransferHandler.COPY;
    }
    
    
    protected Transferable createTransferable(JComponent c){
        Object playlist = ((ImagePlaylist)((JList)c).getSelectedValue()).getList();
        
        Transferable t = new Transferable(){
            public DataFlavor[] getTransferDataFlavors(){
                return new DataFlavor[] {imageListFlavor};
            }
            public boolean isDataFlavorSupported(DataFlavor flavor){
                return imageListFlavor.equals(flavor);
            }
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException{
                return playlist;
            }
        };
        
        return t;
    }
    
    
    public boolean importData(TransferSupport support){
        if(!canImport(support)){
            return false;
        }
        
        try{
            ArrayList<ImageObject> iList = (ArrayList)(support.getTransferable().getTransferData(imageListFlavor));
            
            JList.DropLocation dl = (JList.DropLocation)(support.getDropLocation());
            ImagePlaylist pList = PlaylistList.getInstance().getPlaylist(dl.getIndex());
            
            for(ImageObject img : iList){
                pList.addImage(img);
            }
            
            return true;
        }catch(UnsupportedFlavorException | IOException e){
            e.printStackTrace();
        }
        
        return false;
    }
}
