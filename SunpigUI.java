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
    private TableRowSorter<ImageTableModel> sorter = new TableRowSorter<>(tModel);
    private final RowFilter searchFilter = new RowFilter(){
        public boolean include(Entry entry){
            return match((ImageObject)entry.getValue(0));
        }
    };
    private boolean canAdjustDisplay = true;
    
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
                PlaylistList.getInstance().save();
                ImageLibrary.getInstance().save();
            }
            @Override
            public void windowClosed(WindowEvent e){}
            @Override
            public void windowActivated(WindowEvent e){}
        };
        
        addWindowListener(windowListener);
        
        sorter.setRowFilter(searchFilter);
        
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
                    PlaylistList.getInstance().save();
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
        //list.setDragEnabled(true);
        list.setTransferHandler(new PlaylistTransferHandler());
        
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
                        setCurrentList(ImageLibrary.getInstance());
                    }
		}
	});
        
        
        list.addListSelectionListener(new ListSelectionListener(){
		public void valueChanged(ListSelectionEvent e) {
                    if(list.getSelectedIndex() >= 0 && e.getValueIsAdjusting()){
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
			PlaylistList.getInstance().removePlaylist(list.getSelectedIndex());
			list.setListData(PlaylistList.getInstance().getPlaylistList().toArray());
			PlaylistList.getInstance().save();
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
        split.setResizeWeight(1);
        split.setSize(1000, 500);

        main.add(buildTopBar(), BorderLayout.NORTH);
        main.add(split, BorderLayout.CENTER);

        return main;
    }


    private JPanel buildTopBar(){
        JPanel topBar = new JPanel();
        
        topBar.setLayout(new BorderLayout());
        topBar.add(buildAddButton(), BorderLayout.WEST);
        topBar.add(buildControlPanel(), BorderLayout.CENTER);
        topBar.add(buildSearchbar(), BorderLayout.EAST);
        
        return topBar;
    }


    private JPanel buildAddButton(){
        JPanel pButtonPanel = new JPanel();
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        
        ImageIcon plusImage = new ImageIcon(getClass().getResource("plus.png"));
        JButton plus = new JButton(plusImage);
        pButtonPanel.add(plus);
        
        plus.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
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
                    if(!iTable.isEditing()){
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
    
    
    private JPanel buildControlPanel(){
        JPanel controlPanel = new JPanel();
        
        ImageIcon prevImage = new ImageIcon(getClass().getResource("control-double-180.png"));
        ImageIcon playImage = new ImageIcon(getClass().getResource("control.png"));
        ImageIcon nextImage = new ImageIcon(getClass().getResource("control-double.png"));

        JButton prev = new JButton(prevImage);
        controlPanel.add(prev);
        JButton play = new JButton(playImage);
        controlPanel.add(play);
        JButton next = new JButton(nextImage);
        controlPanel.add(next);
        
        return controlPanel;
    }
    
    
    private JScrollPane buildTable(){
        
	// Register the action listeners.
        iTable.addKeyListener(new KeyListener(){
		public void keyTyped(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_DELETE){
                        int[] sel = iTable.getSelectedRows();

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
        
        iTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e){
                if(canAdjustDisplay)
                    dPane.setDisplayedImage((ImageObject)currentList.getList().get(iTable.convertRowIndexToModel(iTable.getSelectedRow())));
            }
        });
        
        iTable.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        JScrollPane scrollPane = new JScrollPane(iTable);
        iTable.setFillsViewportHeight(true);
        
        return scrollPane;
    }
    
    
    public void setCurrentList(ImageList i){
        currentList = i;
        tModel.setCurrentList(currentList);
        sorter.setModel(tModel);
        tModel.fireTableDataChanged();
    }
    
    
    public boolean match(ImageObject img){
        /*
        *  matchCounter keeps track of how many of the Strings in currentSearch match one or more fields
        *  in ImageObject entry. If, in the end, matchCounter >= currentSearch.length, you know that all
        *  of the Strings in currentSearch matched fields in entry.
        */
        
        String cString = "";
        for(String s : currentSearch)
            cString += s;
        
        if(cString.equals(""))
            return true;
        
        
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
}

