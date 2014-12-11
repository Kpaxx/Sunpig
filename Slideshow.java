/**Chris Kepics
CIS111B Online - Fall2014
Group Members: Jessica Barwell, Michele Gravel**/

package sunpig;

public class Slideshow implements Runnable{
    
    private boolean slideshowIsRunning = false;
    private ImageTable table;
    
    public Slideshow(ImageTable it){
        table = it;
    }
    
    
    public void startSlideshow(){
        slideshowIsRunning = true;
    }
    
    
    public void stopSlideshow(){
        slideshowIsRunning = false;
    }
    
    
    public boolean isRunning(){
        return slideshowIsRunning;
    }
    
    
    public void run(){
        int i;
        while(true){
            
            /*
             * For some mysterious reason, if there is no code here, the program will never enter the 
             * while loop, even when slideshowIsRunning = true. There has to be SOMETHING here. If there is
             * no code, or if it's just a variable declaration, it doesnt work. As soon as you put in an
             * assignment operator, the program works. That's why this line of code is here.
             */
            i = 1;
            
            while(slideshowIsRunning) {
                synchronized(this){
                    selectNextImage();

                    try {
                        this.wait(5000);
                    }catch (InterruptedException interrupt){

                    }
                }
            }
        }
    }
    
    
    private void selectNextImage(){
        if(table.getSelectedRow() == table.getRowCount()-1)
            table.getSelectionModel().setSelectionInterval(0,0);
        else
            table.getSelectionModel().setSelectionInterval(table.getSelectedRow()+1, table.getSelectedRow()+1);
    }
    
}
