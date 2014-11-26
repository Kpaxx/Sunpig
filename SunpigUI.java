/**Michele Gravel
CIS111B Online - Fall2014
Group Members: Jessica Barwell, Chris Kepics**/

package sunpig;

import java.awt.*;

import javax.swing.*;
import javax.swing.JTable.*;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SunpigUI extends JFrame {

    
    public SunpigUI() {

        // Set up default frame attributes
        setTitle("Sunpig");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel sidebar = buildSidebar();
        JPanel main = buildMain();

        //Create a split pane with the two scroll panes in it.
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar, main);
        split.setOneTouchExpandable(true);
        split.setDividerLocation(150);

        //Provide minimum sizes for the two components in the split pane
        Dimension minimumSize = new Dimension(150, 50);
        sidebar.setMinimumSize(minimumSize);
        main.setMinimumSize(minimumSize);

        getContentPane().add(split);
        pack();
        setVisible(true);
    }

    
    private JPanel buildSidebar(){
        JPanel sidebar = new JPanel();
        JButton addPList = new JButton("(+) Add Playlist");
                
        //This part here is just to center the button with respect to the list above it.
        JPanel bottomBar = new JPanel();
        bottomBar.setLayout(new BorderLayout());
        bottomBar.add(new JPanel(), BorderLayout.WEST);
        bottomBar.add(addPList, BorderLayout.CENTER);
        
        sidebar.setLayout(new BorderLayout());
        sidebar.add(new JPanel(), BorderLayout.WEST); //This is just padding, so the list isn't right against the edge of the frame
        sidebar.add(new JPanel(), BorderLayout.NORTH); //This one too. There's probably a better way of doing this.
        
        sidebar.add(buildPlaylists(), BorderLayout.CENTER);
        sidebar.add(bottomBar, BorderLayout.SOUTH);

		// Register the action listeners.
        addPList.addActionListener(new AddPlayListListenerActionPerformed());
        
        return sidebar;
    }


    private JScrollPane buildPlaylists(){
        JList list = new JList();
		PlaylistList playlists =  PlaylistList.getInstance();
//		addPlaylist.getPlaylist();
        //String[] plNames = { "Batman - The Long Halloween", "Chichen Itza", "Chris's 21st Birthday",
         //   "Salvador Dali", "Studio Art 101", "Tennessee Vacation 2013"};
//       ArrayList plNames = addPlaylist.getPlaylist();;


        list.setListData(playlists.getPlaylist().toArray());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setAlignmentX(LEFT_ALIGNMENT);

        return listScroller;
    }

    
    private JPanel buildMain(){

        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildTable(), buildImageDisplay());

        main.add(buildTopBar(), BorderLayout.NORTH);
        main.add(split, BorderLayout.CENTER);

        return main;
    }


    private JPanel buildTopBar(){
        JPanel topBar = new JPanel();
        
        topBar.setLayout(new BorderLayout());
        topBar.add(buildControlPanel(), BorderLayout.CENTER);
        topBar.add(new JTextField("Search                   "), BorderLayout.EAST);
        
        return topBar;
    }
    
    
    private JPanel buildControlPanel(){
        JPanel controlPanel = new JPanel();
        
        ImageIcon pauseImage = new ImageIcon(getClass().getResource("control-pause.png"));
        ImageIcon playImage = new ImageIcon(getClass().getResource("control.png"));
        ImageIcon stopImage = new ImageIcon(getClass().getResource("control-stop-square.png"));

        JButton pause = new JButton(pauseImage);
        controlPanel.add(pause);
        JButton play = new JButton(playImage);
        controlPanel.add(play);
        JButton stop = new JButton(stopImage);
        controlPanel.add(stop);
        
        return controlPanel;
    }
    
    
    private JScrollPane buildTable(){
        ImageTable iTable = new ImageTable(ImageLibrary.getInstance());
        
        iTable.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        JScrollPane scrollPane = new JScrollPane(iTable);
        iTable.setFillsViewportHeight(true);
        
        return scrollPane;
    }
    
    
    private JPanel buildImageDisplay(){
        JPanel imageDisplay = new JPanel();
        
        ImageIcon image = new ImageIcon(getClass().getResource("MonaLisa.jpg"));
        JLabel imageContainer = new JLabel(image);
        
        /*
        // Might need this later, when figuring out how to resize the image 
        imageDisplay.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.weighty = 1.0;
        
        imageDisplay.add(imageContainer, c);
        */
        
        // Turns out the easiest way to center something both vertically AND horizontally in
        // a JPanel is to make the panel a 1x1 GridLayout
        imageDisplay.setLayout(new GridLayout(1,1));
        imageDisplay.add(imageContainer);
        
        imageDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        
        return imageDisplay;
    }
    
    
    private class AddPlayListListenerActionPerformed implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			String input = JOptionPane.showInputDialog("Enter the new PlayList name.");
			PlaylistList addPlaylist =  PlaylistList.getInstance();
			addPlaylist.addPlaylist(input);			
			buildSidebar();			
			addPlaylist.quit();			
		}
	}
}


