/*
 * Chris Kepics
 * CS 111 - Final Project
 */
package sunpig;

import java.util.Comparator;

public class ImageComparer implements Comparator<Image>, ImageConstants{
    private int currentSort;
    
    public ImageComparer(int cSort){
        currentSort = cSort;
    }
    
    public int compare(Image a, Image b){
        int comp = getData(a, currentSort).compareTo(getData(b, currentSort));
        
        if(comp < 0)
            return -1;
        else if(comp == 0){
            /*
            *  The following switch block takes care of secondary sorting. Images are sorted first by whatever currentSort
            *  indicates, followed by Artist, Title, and finally Page. If currentSort==SORT_TITLE, the secondary sort would
            *  be by Artist and then by Page. 
            */
            switch (currentSort) {
                case SORT_RATING:
                case SORT_LOCATION:
                case SORT_SUBJECT:
                case SORT_DATE:
                    comp = compare(a, b, SORT_ARTIST);
                    if(comp != 0)
                        return returnComp(comp);
                    comp = compare(a, b, SORT_TITLE);
                    if(comp != 0)
                        return returnComp(comp);
                    comp = compare(a, b, SORT_PAGE);
                    if(comp != 0)
                        return returnComp(comp);
                    break;
                case SORT_ARTIST:
                    comp = compare(a, b, SORT_TITLE);
                    if(comp != 0)
                        return returnComp(comp);
                    comp = compare(a, b, SORT_PAGE);
                    if(comp != 0)
                        return returnComp(comp);
                    break;
                case SORT_TITLE:
                    comp = compare(a, b, SORT_ARTIST);
                    if(comp != 0)
                        return returnComp(comp);
                    comp = compare(a, b, SORT_PAGE);
                    if(comp != 0)
                        return returnComp(comp);
                    break;
                case SORT_PAGE:
                    comp = compare(a, b, SORT_ARTIST);
                    if(comp != 0)
                        return returnComp(comp);
                    comp = compare(a, b, SORT_TITLE);
                    if(comp != 0)
                        return returnComp(comp);
                    break;
            } 
            return 0;
        }else
            return 1;
        
    }
    
    
    private int compare(Image a, Image b, int s){
        return (getData(a, s).compareTo(getData(b, s)));
    }
    
    
    //Returns String data for sorting
    private String getData(Image i, int d){
        switch (d){
            case SORT_TITLE:
                return i.getTitle();
            case SORT_ARTIST:
                return i.getArtist();
            case SORT_RATING:
                return Integer.toString(i.getRating());
            case SORT_LOCATION:
                return i.getLocation();
            case SORT_SUBJECT:
                return i.getSubject();
            case SORT_DATE:
                return i.getDate();
            case SORT_PAGE:
                return i.printPageNum();
            default:
                return "";
        }
    }
    
    private int returnComp(int comp){
        if (comp > 0)
            return 1;
        else if (comp < 0)
            return -1;
        else
            return 0;
    }
}
