/**Chris Kepics
CIS111B Online - Fall2014
Group Members: Jessica Barwell, Michele Gravel**/

package sunpig;

import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import org.imgscalr.Scalr;

public class DisplayPane extends JPanel{
    
    private ImageObject displayedImage;
    
    
    public DisplayPane(){
        // It turns out the easiest way to center something both vertically AND horizontally inside
        // a JPanel is to make the panel a 1x1 GridLayout
        setLayout(new GridLayout(1,1));
        displayedImage = null;
        drawImage(400,400);
        setPreferredSize(new Dimension(400,400));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        setBackground(Color.BLACK);
        setMinimumSize(new Dimension(0,0));
    }
    
    
    public void setDisplayedImage(ImageObject i){
        displayedImage = i;
        drawImage(getHeight(), getWidth());
    }
    
    
    //If no image is selected, draw a blank pane. If the file can't be found, draw an error message
    //in the pane. If the image can be found, draw the image, resized to fit in the pane.
    public void drawImage(int height, int width){
        BufferedImage img;
        
        if(displayedImage == null){
            img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        }else{
            try{
                img = ImageIO.read(new File(displayedImage.getPath()));
            }
            catch(Exception e){
                System.out.println(displayedImage.getPath() + ", " + e.getMessage());
                img = new BufferedImage(410, 410, BufferedImage.TYPE_INT_RGB);
                Graphics g = img.getGraphics();
                g.drawString("I'm sorry, Dave, I can't find:",20,20);
                g.drawString(displayedImage.getTitle(), 20, 40);
            }
        }
        
        ImageIcon rImage;
        try{
            rImage = new ImageIcon(Scalr.resize(img, Scalr.Mode.BEST_FIT_BOTH, width, height));
        }catch(Exception e){
            rImage = new ImageIcon(Scalr.resize(img, 0, 0));
        }
        JLabel imageContainer = new JLabel(rImage);
        
        try{
            remove(0);
            revalidate();
            repaint();
        }catch(Exception e){
            
        }
        
        add(imageContainer);
        revalidate();
        repaint();
    }
}
