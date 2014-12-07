/**Jessica Barwell
CIS111B Online - Fall2014
Group Members: Chris Kepics, Michele Gravel**/

package sunpig;

import java.util.*;
import java.util.Collections;
import java.io.*;

public class Tags implements Serializable
{

   private ArrayList<String> tagList = new ArrayList();
    
   public void setTags(String t)
   {
      tagList.clear();
      String[] tags = t.split("\\s*,\\s*");
      Collections.addAll(tagList, tags);    
   }
   
  public ArrayList getTagList()
   {
      return tagList;
   }
   
   public String printTagList()
   {
       
      String tagString = tagList.toString()
         .replace("[", "")  
         .replace("]", "");  
           
      return tagString;
         
   }
   
   
   
}//end class