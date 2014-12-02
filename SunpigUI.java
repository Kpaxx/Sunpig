/**Michele Gravel
CIS111B Online - Fall2014
Group Members: Jessica Barwell, Chris Kepics**/

package sunpig;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;
import java.beans.*;
import java.awt.event.*;

public class SunpigUI extends JFrame {

    private JList list = new JList();
    private ImageTable iTable = new ImageTable(ImageLibrary.getInstance());
    private DisplayPane dPane = new DisplayPane();
    private ImageList displayedList = ImageLibrary.getInstance();
    private ImageList selectedList = ImageLibrary.getInstance();
    
    public SunpigUI() {

        // Set up default frame attributes
        setTitle("Sunpig");
        
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        WindowListener windowListener = new WindowListener(){
            @Override
            public void windowOpened(WindowEvent e){}
            @Override
            public void windowIconified(WindowEvent e){}
            @Override
            public void windowDeiconified(WindowEvent e){}
            @Override
            public void windowDeactivated(WindowEvent e){}
            @Override
            public void windowClosing(WindowEvent e){
                PlaylistList.getInstance().quit();
                ImageLibrary.getInstance().quit();
            }
            @Override
            public void windowClosed(WindowEvent e){}
            @Override
            public void windowActivated(WindowEvent e){}
        };
        
        window.addWindowListener(windowListener);
        
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
        addPList.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
                    String input = JOptionPane.showInputDialog("Enter the new PlayList name.");
                    PlaylistList.getInstance().addPlaylist(input);
                    list.setListData(PlaylistList.getInstance().getPlaylistList().toArray());
                    PlaylistList.getInstance().quit();
		}
	});
        
        return sidebar;
    }


    private JScrollPane buildPlaylists(){
        JPanel listPanel = new JPanel();
                
        //This puts a selector for the Library at the top of the sidebar
        JList library = new JList();
        ImageLibrary[] libList = {ImageLibrary.getInstance()};
        library.setListData(libList);
        library.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        library.setLayoutOrientation(JList.VERTICAL);
        library.setVisibleRowCount(-1);
        
        //This puts the the playlists into the sidebar
        list.setListData(PlaylistList.getInstance().getPlaylistList().toArray());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        list.setDropMode(DropMode.ON);
        
        listPanel.setLayout(new BorderLayout());
        listPanel.add(library, BorderLayout.NORTH);
        listPanel.add(list, BorderLayout.CENTER);
        
        JScrollPane listScroller = new JScrollPane(listPanel);
        listScroller.setAlignmentX(LEFT_ALIGNMENT);
        
        
	// Register the action listeners.
        library.addListSelectionListener(new ListSelectionListener(){
		public void valueChanged(ListSelectionEvent e) {
                    if(e.getValueIsAdjusting()){
                        list.clearSelection();
                        selectedList = ImageLibrary.getInstance();
                        setDisplayedList(selectedList);
                    }
		}
	});
        
        
        list.addListSelectionListener(new ListSelectionListener(){
		public void valueChanged(ListSelectionEvent e) {
                    if(list.getSelectedIndex() >= 0 && e.getValueIsAdjusting()){
                        library.clearSelection();
                        selectedList = PlaylistList.getInstance().getPlaylist(list.getSelectedIndex());
                        setDisplayedList(selectedList);
                    }
		}
	});
        
        
        list.addKeyListener(new KeyListener(){
		public void keyTyped(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {}
		public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_DELETE){
			PlaylistList.getInstance().removePlaylist(list.getSelectedIndex());
			list.setListData(PlaylistList.getInstance().getPlaylistList().toArray());
			PlaylistList.getInstance().quit();
                    }
		}
	});

        return listScroller;
    }

    
    private JPanel buildMain(){
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        
        dPane.setMinimumSize(new Dimension(0,0));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildTable(), dPane);
        split.addComponentListener(new ComponentListener(){
            public void componentHidden(ComponentEvent e){}
            public void componentShown(ComponentEvent e){}
            public void componentMoved(ComponentEvent e){}
            public void componentResized(ComponentEvent e){
                dPane.drawImage(dPane.getHeight(), split.getWidth()-split.getDividerLocation());
            }
        });
        split.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent e){
                dPane.drawImage(dPane.getHeight(), split.getWidth()-split.getDividerLocation());
            }
        });

        main.add(buildTopBar(), BorderLayout.NORTH);
        main.add(split, BorderLayout.CENTER);

        return main;
    }


    private JPanel buildTopBar(){
        JPanel topBar = new JPanel();
        
        topBar.setLayout(new BorderLayout());
        topBar.add(buildControlPanel(), BorderLayout.CENTER);
        topBar.add(buildSearchbar(), BorderLayout.EAST);
        
        return topBar;
    }
    
    
    private JPanel buildSearchbar(){
        final JTextField searchbar = new JTextField();
        
        searchbar.setText("Search                          ");
        searchbar.setEnabled(false);
        searchbar.addMouseListener(new MouseListener(){
            public void mouseEntered(MouseEvent e){
                searchbar.setEnabled(true);
            }
            public void mouseExited(MouseEvent e){}
            public void mousePressed(MouseEvent e){
                searchbar.setText("");
            }
            public void mouseReleased(MouseEvent e){}
            public void mouseClicked(MouseEvent e){}
        });
        
        searchbar.addKeyListener(new KeyListener(){
		public void keyTyped(KeyEvent e) {}
		public void keyPressed(KeyEvent e){}
		public void keyReleased(KeyEvent e){
                    String[] searchStrings = searchbar.getText().split("\\s*,\\s*");
                    ImageList sList = new ImagePlaylist(selectedList);
                    sList = ImagePlaylist.toPlaylist(sList.search(searchStrings));
                    setDisplayedList(sList);
		}
	});
        
        JPanel sBarPanel = new JPanel();
        sBarPanel.add(searchbar);
        sBarPanel.setPreferredSize(searchbar.getPreferredSize());
        return sBarPanel;
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
        
	// Register the action listeners.
        iTable.addKeyListener(new KeyListener(){
		public void keyTyped(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_DELETE){
                        for(int i : iTable.getSelectedRows())
                            displayedList.removeImage(i);
                        
                        updateDisplayedList();
			PlaylistList.getInstance().quit();
                    }
		}
		public void keyPressed(KeyEvent e) {}
	});
        
        iTable.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        JScrollPane scrollPane = new JScrollPane(iTable);
        iTable.setFillsViewportHeight(true);
        
        return scrollPane;
    }
    
    
    public void setDisplayedList(ImageList i){
        displayedList = i;
        iTable.setModel(i);
    }
    
    
    public void updateDisplayedList(){
        displayedList.fireTableDataChanged();
    }
}

