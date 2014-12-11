/**Michele Gravel
CIS111B Online - Fall2014
Group Members: Jessica Barwell, Chris Kepics**/

package sunpig;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableRowSorter;
import javax.swing.event.*;
import java.beans.*;
import java.awt.event.*;
import java.util.*;
import java.io.File;

public class SunpigUI extends JFrame {

    private JList list = new JList();
    private String[] currentSearch = new String[] {""};
    private ImageList currentList = ImageLibrary.getInstance();
    private ImageTableModel tModel = new ImageTableModel(currentList);
    private ImageTable iTable = new ImageTable(tModel);
    private DisplayPane dPane = new DisplayPane();
    private boolean canAdjustDisplay = true;
    private String currentSort = "Artist/Photographer";
    private boolean orderToggle = true;
    private TableRowSorter<ImageTableModel> sorter = new TableRowSorter<>(tModel);
    private final RowFilter searchFilter = new RowFilter(){
        public boolean include(Entry entry){
            return match((ImageObject)entry.getValue(0));
        }
    };
    Slideshow slideshow = new Slideshow(iTable);
    
    public SunpigUI() {

        // Set up default frame attributes
        setTitle("Sunpig");
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                //Automatically aves the playlists and the library before closing!
                PlaylistList.getInstance().save();
                ImageLibrary.getInstance().save();
            }
            @Override
            public void windowClosed(WindowEvent e){}
            @Override
            public void windowActivated(WindowEvent e){}
        };
        
        addWindowListener(windowListener);
        
        //The slideshow runs on a separate thread because it involves a while loop that runs forever
        Thread t = new Thread(slideshow);
        t.start();
        
        sorter.setRowFilter(searchFilter);
        

        //Create a split pane with the two scroll panes in it. On the left, the sidebar, on the right, everything else.
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildSidebar(), buildMain());
        split.setOneTouchExpandable(true);
        split.setDividerLocation(150);

        
        //Provide a minimum size for the split pane
        Dimension minimumSize = new Dimension(150, 50);
        split.setMinimumSize(minimumSize);

        
        getContentPane().add(split);
        pack();
        setVisible(true);
    }
    
    
    //Assembles the sidebar, complete with the actionListener for the button
    private JPanel buildSidebar(){
        JPanel sidebar = new JPanel();
        JButton addPList = new JButton("(+) Add Playlist");
                
        //This part here is just to center the button with respect to the list above it.
        JPanel bottomBar = new JPanel();
        bottomBar.setLayout(new BorderLayout());
        bottomBar.add(new JPanel(), BorderLayout.WEST);
        bottomBar.add(addPList, BorderLayout.CENTER);
        
        
        //This part is just padding too, so that the list isn't right against the edge of the frame.
        sidebar.setLayout(new BorderLayout());
        sidebar.add(new JPanel(), BorderLayout.WEST); 
        sidebar.add(new JPanel(), BorderLayout.NORTH);
        
        
        sidebar.add(buildPlaylists(), BorderLayout.CENTER);
        sidebar.add(bottomBar, BorderLayout.SOUTH);

        
	// The "Add Playlist" button adds a new playlist to the PlaylistList
        addPList.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
                    String input = JOptionPane.showInputDialog("Enter the new PlayList name.");
                    PlaylistList.getInstance().addPlaylist(" - " + input);
                    list.setListData(PlaylistList.getInstance().getPlaylistList().toArray());
                    PlaylistList.getInstance().save();
		}
	});
        
        return sidebar;
    }


    //Assembles the list of playlists in the sidebar
    private JScrollPane buildPlaylists(){
                
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
        //list.setDragEnabled(true);  //Enabling dragging in the list somehow breaks the actionListeners. Will look into this later.
        list.setTransferHandler(new PlaylistTransferHandler());
        
        
        //For the Image Library and playlist header in the sidebar list
        JPanel tPan = new JPanel();
        tPan.setLayout(new GridLayout(3,1));
        tPan.add(library);
        tPan.add(new JLabel());
        tPan.add(new JLabel("Playlists"));
        tPan.setBackground(Color.WHITE);
        
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());
        listPanel.add(tPan, BorderLayout.NORTH);
        listPanel.add(list, BorderLayout.CENTER);
        
        JScrollPane listScroller = new JScrollPane(listPanel);
        listScroller.setAlignmentX(LEFT_ALIGNMENT);
        
        
	// Register the action listeners.
        library.addListSelectionListener(new ListSelectionListener(){
		public void valueChanged(ListSelectionEvent e) {
                    if(e.getValueIsAdjusting()){
                        if(slideshow.isRunning())
                            slideshow.stopSlideshow();
                        
                        list.clearSelection();
                        setCurrentList(ImageLibrary.getInstance());
                    }
		}
	});
        
        
        list.addListSelectionListener(new ListSelectionListener(){
		public void valueChanged(ListSelectionEvent e) {
                    if(list.getSelectedIndex() >= 0 && e.getValueIsAdjusting()){
                        if(slideshow.isRunning())
                            slideshow.stopSlideshow();
                        
                        library.clearSelection();
                        setCurrentList(PlaylistList.getInstance().getPlaylist(list.getSelectedIndex()));
                    }
		}
	});
        
        
        list.addKeyListener(new KeyListener(){
		public void keyTyped(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {}
		public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_DELETE){
                        if(slideshow.isRunning())
                            slideshow.stopSlideshow();
                        
			PlaylistList.getInstance().removePlaylist(list.getSelectedIndex());
			list.setListData(PlaylistList.getInstance().getPlaylistList().toArray());
			PlaylistList.getInstance().save();
                    }
		}
	});

        return listScroller;
    }

    
    //Assembles everything that's NOT in the sidebar
    private JPanel buildMain(){
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        

        //Sets up a split pane with the table on the left and the display pane on the right
        //When the display pane is resized, it scales the image to fit
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
        split.setResizeWeight(1);
        split.setSize(1000, 500);

        main.add(buildTopBar(), BorderLayout.NORTH);
        main.add(split, BorderLayout.CENTER);

        return main;
    }


    //Assembles the strip at the top with the buttons and search bar
    private JPanel buildTopBar(){
        JPanel topBar = new JPanel();
        
        topBar.setLayout(new BorderLayout());
        topBar.add(buildAddButton(), BorderLayout.WEST);
        topBar.add(buildControlPanel(), BorderLayout.CENTER);
        topBar.add(buildSearchbar(), BorderLayout.EAST);
        
        return topBar;
    }


    //The button that you click to add images to the library
    private JPanel buildAddButton(){
        JPanel pButtonPanel = new JPanel();
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        
        ImageIcon plusImage = new ImageIcon(getClass().getResource("plus.png"));
        JButton plus = new JButton(plusImage);
        pButtonPanel.add(plus);
        
        //Click th button to open the File Chooser window
        plus.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(slideshow.isRunning())
                    slideshow.stopSlideshow();
                int dVal = fc.showOpenDialog(list);
                if(dVal == JFileChooser.APPROVE_OPTION){
                    File[] files = fc.getSelectedFiles();
                    for(File f : files){
                        String n = f.getName();
                        if(n.toLowerCase().endsWith(".jpg") ||
                                n.toLowerCase().endsWith(".jpeg") ||
                                n.toLowerCase().endsWith(".png") ||
                                n.toLowerCase().endsWith(".gif") ||
                                n.toLowerCase().endsWith(".bmp")){
                            ImageObject img = new ImageObject(f.getAbsolutePath());
                            img.setTitle(f.getName());
                            ImageLibrary.getInstance().addImage(img);
                        }else{
                            System.err.println("UNSUPPORTED FILE TYPE: File " + f.getName() + " could not be opened.");
                        }
                    }
                }
            }
        });
        
        return pButtonPanel;
    }
    
    
    //Sets up the search bar
    private JPanel buildSearchbar(){
        final JTextField searchbar = new JTextField();
        
        //The searchbar starts out saying "Search", but it's grayed out.
        //When you click in the bar, the word "Search" goes away and the stuff you type isn't grayed out.
        searchbar.setText("Search                          ");
        searchbar.setEnabled(false);
        searchbar.addMouseListener(new MouseListener(){
            public void mouseEntered(MouseEvent e){
                searchbar.setEnabled(true);
            }
            public void mouseExited(MouseEvent e){}
            public void mousePressed(MouseEvent e){
                if(slideshow.isRunning())
                    slideshow.stopSlideshow();
                
                searchbar.setText("");
            }
            public void mouseReleased(MouseEvent e){}
            public void mouseClicked(MouseEvent e){}
        });
        
        searchbar.addKeyListener(new KeyListener(){
		public void keyTyped(KeyEvent e) {}
		public void keyPressed(KeyEvent e){}
		public void keyReleased(KeyEvent e){
                    if(!iTable.isEditing()){
                        if(slideshow.isRunning())
                            slideshow.stopSlideshow();
                        
                        iTable.getSelectionModel().clearSelection();
                        currentSearch = searchbar.getText().trim().split("\\s*,\\s*");
                        iTable.setRowSorter(sorter);
                        tModel.fireTableDataChanged();
                    }
		}
	});
        
        JPanel sBarPanel = new JPanel();
        sBarPanel.add(searchbar);
        sBarPanel.setPreferredSize(searchbar.getPreferredSize());
        return sBarPanel;
    }
    
    
    //Sets up the "<<", ">", and ">>" buttons at the top of the window
    private JPanel buildControlPanel(){
        JPanel controlPanel = new JPanel();
        
        ImageIcon prevImage = new ImageIcon(getClass().getResource("control-double-180.png"));
        ImageIcon playImage = new ImageIcon(getClass().getResource("control.png"));
        ImageIcon nextImage = new ImageIcon(getClass().getResource("control-double.png"));

        JButton prev = new JButton(prevImage);
        prev.addActionListener (new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(iTable.getSelectedRow() == 0)
                   iTable.getSelectionModel().setSelectionInterval(iTable.getRowCount(), iTable.getRowCount());
                else
                   iTable.getSelectionModel().setSelectionInterval(iTable.getSelectedRow()-1, iTable.getSelectedRow()-1);
            }
        });
        controlPanel.add(prev);
        
        JButton play = new JButton(playImage);
        play.addActionListener (new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(slideshow.isRunning()){
                    ImageIcon playImage = new ImageIcon(getClass().getResource("control.png"));
                    play.setIcon(playImage);
                    slideshow.stopSlideshow();
                }else{
                    ImageIcon pauseImage = new ImageIcon(getClass().getResource("control-pause.png"));
                    play.setIcon(pauseImage);
                    slideshow.startSlideshow();
                }
            }
        });
        controlPanel.add(play);
        
        JButton next = new JButton(nextImage);
        next.addActionListener (new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(iTable.getSelectedRow() == iTable.getRowCount()-1)
                    iTable.getSelectionModel().setSelectionInterval(0,0);
                else
                    iTable.getSelectionModel().setSelectionInterval(iTable.getSelectedRow()+1, iTable.getSelectedRow()+1);
            }
        });
        controlPanel.add(next);
        
        return controlPanel;
    }
    
    
    //Sets up the ImageTable and all it's action listeners
    private JScrollPane buildTable(){
        sortRows("Artist/Photographer");
        
	// Pressing the Delete key deletes the selected song from the selected ImageList
        iTable.addKeyListener(new KeyListener(){
		public void keyTyped(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_DELETE){
                        if(slideshow.isRunning())
                            slideshow.stopSlideshow();
                        
                        int[] sel = iTable.getSelectedRows();

                        //Remove images from the bottom up, so that the indexes don't all change each time you delete an image
                        for(int i = sel.length-1; i >= 0; i--)
                            currentList.removeImage(iTable.convertRowIndexToModel(sel[i]));

                        canAdjustDisplay = false;
                        tModel.fireTableDataChanged();
                        canAdjustDisplay = true;
                        dPane.setDisplayedImage(null);
                        ImageLibrary.getInstance().save();
                        PlaylistList.getInstance().save();
                    }
		}
		public void keyPressed(KeyEvent e) {}
	});
        
        //Clicking a column header sorts the table by that header in ascending order
        //Clicking that column again sorts it in descending order
        iTable.getTableHeader().addMouseListener(new MouseListener(){
            public void mouseEntered(MouseEvent e){}
            public void mouseExited(MouseEvent e){}
            public void mousePressed(MouseEvent e){}
            public void mouseReleased(MouseEvent e){
                canAdjustDisplay = false;
                iTable.getSelectionModel().clearSelection();
                int col = iTable.getColumnModel().getColumnIndexAtX(e.getX());
                String colName = tModel.getColumnName(col);
                if(currentSort.equals(colName))
                    orderToggle = !orderToggle;
                else
                    orderToggle = false;

                currentSort = colName;

                sortRows(colName);
                canAdjustDisplay = true;
            }
            public void mouseClicked(MouseEvent e){}
        });
        
        //Changes the image displayed in the display pane whenever a new image is selected in the table
        iTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e){
                if(canAdjustDisplay)
                    try{
                        dPane.setDisplayedImage((ImageObject)currentList.getList().get(iTable.convertRowIndexToModel(iTable.getSelectedRow())));
                    }catch(ArrayIndexOutOfBoundsException ex){
                        //This happens on occasion, and I haven't quite all of the things that have been causing it yet.
                        dPane.setDisplayedImage(null);
                    }
            }
        });
        
        iTable.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        JScrollPane scrollPane = new JScrollPane(iTable);
        iTable.setFillsViewportHeight(true);
        
        return scrollPane;
    }
    
    
    //Changes which ImageList is currently displayed in the table
    public void setCurrentList(ImageList i){
        currentList = i;
        tModel.setCurrentList(currentList);
        sorter.setModel(tModel);
        tModel.fireTableDataChanged();
    }
    
    
    //Checks if your search parameters meet the ImageObject
    public boolean match(ImageObject img){
        
        String cString = "";
        for(String s : currentSearch)
            cString += s;
        
        if(cString.equals(""))
            return true;
        
        /*
        *  matchCounter keeps track of how many of the Strings in currentSearch match one or more fields
        *  in ImageObject entry. If, in the end, matchCounter >= currentSearch.length, you know that all
        *  of the Strings in currentSearch matched fields in entry.
        */
        int matchCounter = 0;
        ArrayList<String> tags = img.getTags();
        for(String str : currentSearch){
            str = str.toLowerCase();
            if(img.getTitle().toLowerCase().contains(str))
                matchCounter++;
            else if(img.getArtist().toLowerCase().contains(str))
                matchCounter++;
            else if(img.getLocation().toLowerCase().contains(str))
                matchCounter++;
            else if(img.getSubject().toLowerCase().contains(str))
                matchCounter++;
            else if(img.printPageNum().contains(str))
                matchCounter++;
            else{
                for(String t : tags){
                    str = str.toLowerCase();
                    if(t.toLowerCase().contains(str)){
                        matchCounter++;
                        break;
                    }
                }
            }
        }
        
        if(matchCounter >= currentSearch.length)
            return true;
        
        return false;
    }
    
    
    //Sorts the rows in the table, first by the selected field, then by artist/photographer,
    //then by title, and finally by page number.
    public void sortRows(String colName){
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        SortOrder order;
        if((orderToggle && !colName.equals("Rating")) || (colName.equals("Rating") && !orderToggle))
            order = SortOrder.ASCENDING;
        else
            order = SortOrder.DESCENDING;
        
        switch(colName){
            case "Rating":
                sortKeys.add(new RowSorter.SortKey(3, order));
                sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
                sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
                sortKeys.add(new RowSorter.SortKey(7, SortOrder.ASCENDING));
                break;
            case "Location":
                sortKeys.add(new RowSorter.SortKey(4, order));
                sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
                sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
                sortKeys.add(new RowSorter.SortKey(7, SortOrder.ASCENDING));
                break;
            case "Subject":
                sortKeys.add(new RowSorter.SortKey(3, order));
                sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
                sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
                sortKeys.add(new RowSorter.SortKey(7, SortOrder.ASCENDING));
                break;
            case "Date Added":
                sortKeys.add(new RowSorter.SortKey(6, order));
                sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
                sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
                sortKeys.add(new RowSorter.SortKey(7, SortOrder.ASCENDING));
                break;
            case "Artist/Photographer":
                sortKeys.add(new RowSorter.SortKey(1, order));
                sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
                sortKeys.add(new RowSorter.SortKey(7, SortOrder.ASCENDING));
                break;
            case "Title":
                sortKeys.add(new RowSorter.SortKey(0, order));
                sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
                sortKeys.add(new RowSorter.SortKey(7, SortOrder.ASCENDING));
                break;
            case "Page":
                sortKeys.add(new RowSorter.SortKey(7, order));
                sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
                sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
                break;
            case "Tags":
            default:
                return;
        }
        
        sorter.setSortKeys(sortKeys);
        iTable.setRowSorter(sorter);
    }
}

