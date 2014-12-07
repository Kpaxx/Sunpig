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
        // Turns out the easiest way to center something both vertically AND horizontally in
        // a JPanel is to make the panel a 1x1 GridLayout
        setLayout(new GridLayout(1,1));
        displayedImage = new ImageObject("/Users/Kpax/Desktop/CS 111/SunPig/src/sunpig/MonaLisa.jpg");
        drawImage(400,400);
        setPreferredSize(new Dimension(400,400));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        setBackground(Color.BLACK);
    }
    
    
    public void setDisplayedImage(ImageObject i){
        displayedImage = i;
        drawImage(getHeight(), getWidth());
    }
    
    public void drawImage(int height, int width){
        BufferedImage img;
        
        try{
            img = ImageIO.read(new File(displayedImage.getPath()));
        }
        catch(Exception e){
            System.out.println(displayedImage.getPath() + ", " + e.getMessage());
            img = new BufferedImage(410, 410, BufferedImage.TYPE_INT_RGB);
            Graphics g = img.getGraphics();
            g.drawString("I'm sorry, Dave, I can't find:",20,20);
            g.drawString(displayedImage.getPath(), 20, 40);
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
