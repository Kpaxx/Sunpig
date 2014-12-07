/**Chris Kepics
CIS111B Online - Fall2014
Group Members: Jessica Barwell, Michele Gravel**/

package sunpig;

import javax.swing.*;
import javax.swing.table.*;
import java.util.ArrayList;
import java.awt.datatransfer.*;
import java.io.IOException;

public class ImageTransferHandler extends TransferHandler{
    
    DataFlavor imageListFlavor = new DataFlavor(ArrayList.class, "ArrayList");
    
    protected Transferable createTransferable(JComponent c){
        int[] selectedRows = ((JTable)c).getSelectedRows();
        ArrayList<ImageObject> iList = ((ImageTableModel)((JTable)c).getModel()).getCurrentList().getList();
        ArrayList<ImageObject> playlist = new ArrayList();
        
        for(int selected : selectedRows){
            playlist.add(iList.get(((JTable)c).convertRowIndexToModel(selected)));
        }
        
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
    
    
    public int getSourceActions(JComponent c){
        return TransferHandler.COPY;
    }
}
