/**Jessica Barwell
CIS111B Online - Fall2014
Group Members: Chris Kepics, Michele Gravel**/

package sunpig;

import java.util.*;
import java.text.*;
import java.io.*;

public class ImageObject implements Serializable
{
   private String path;
   private String title;
   private String artist = "";
   private String location = "";
   private GregorianCalendar date;
   private SimpleDateFormat fmt = new SimpleDateFormat("MMM.dd.yyyy, h:mm a");
   private String subject = "";
   private Tags tags = new Tags();
   private int rating = 5;
   private int pageNum = -1;
   
   /**Constructor*/
   public ImageObject(String path_)
   {
      path = path_;
      title = path;
      
      date = new GregorianCalendar();
      Date currentDate = new Date();
      date.setTime(currentDate);
      
   }

   /**The getPath method returns the ImageObject path
   @return path The image path*/
   public String getPath()
   {
      return path;
   }

   /**The setTitle method sets the ImageObject title
   @param t The image title*/
   public void setTitle(String t)
   {
      title = t;
   }
   
   /**The getTitle method returns the ImageObject title
   @return The ImageObject title*/
   public String getTitle()
   {
      return title;
   }

   /**The setArtist method sets the artist of the ImageObject
   @param a The artist of the ImageObject*/
   public void setArtist(String a)
   {
      artist = a;
   }
   
   /**The getArtist method returns the artist of the image
   @return The artist of the image*/
   public String getArtist()
   {
      return artist;
   }

   /**The setLocation method sets the image location
   @param n The image location*/
   public void setLocation(String n)
   {
      location = n;
   }
   
   /**The getLocation method returns the location of the image
   @return The image location*/
   public String getLocation()
   {
      return location;
   }

   
   /**The getDate method returns the date the image was taken
   @return The date the image was taken*/
   public String getDate()
   {
      String dateFormatted = fmt.format(date.getTime());
      return dateFormatted;
   }
       
   /**The setSubject method sets the subject of the image
   @param s The subject of the image*/
   public void setSubject(String s)
   {
      subject = s;
   }

   /**The getSubject method returns the subject of the image
   @return The subject of the image*/
   public String getSubject()
   {
      return subject;
   }

   /**The setTags method sets image tags
   @param t the tag to set for the image*/
   public void setTags(String t)
   {
      tags.setTags(t);
   }
   
   /**The getTags method returns the tags set for the image
   @return The image tags*/
   public ArrayList getTags()
   {
      return tags.getTagList();
   }
   
   /**The printTags method returns the tags set for the image
   @return The image tags*/
   public String printTags()
   {
      return tags.printTagList();
   }

   /**The setRating method sets the image rating
   @param r The image rating*/
   //Internally, rating values are opposite what they display. This is so that
   //when sorting by rating, they sort from most stars to least stars. --Chris
   public void setRating(String r)
   {
      if(r.equals("5") || r.equals("*****"))
         rating = 0;
      else if(r.equals("4") || r.equals("****"))
         rating = 1;
      else if(r.equals("3") || r.equals("***"))
         rating = 2;
      else if(r.equals("2") || r.equals("**"))
         rating = 3;
      else if(r.equals("1") || r.equals("*"))
         rating = 4;
      else if(r.equals("0") || r.equals(""))
         rating = 5;
   }

   /**The getRating method returns the image rating
   @return The image rating*/
   public int getRating()
   {
      return rating;
   }
   
   /**The printRating method returns the image rating in stars
   @return The image rating in stars*/
   public String printRating()
   {
      if(rating == 0)
         return "*****";
      else if(rating == 1)
         return "****";
      else if(rating == 2)
         return "***";
      else if(rating == 3)
         return "**";
      else if(rating == 4)
         return "*";
      else 
         return "";
   }
   
   

   /**The setPageNum method sets the image page number
   @param p The image page number*/
   public void setPageNum(String p)
   {
      if(p == "")
         pageNum = -1;
      else
      {
         try
         {
         int pageNumber = Integer.parseInt(p);
         if(pageNumber >=0)
            pageNum = pageNumber;
         }
         catch(Exception e)
         {
         }
      }
   }

   /**The getPageNum method returns the image page number
   @return The image page number*/
   public int getPageNum()
   {
      return pageNum;
   }

   /**The printPageNum method returns the page number as a String
   @return The page number as a String*/
   public String printPageNum()
   {
      if(pageNum == -1)
         return "";
      else
         return String.valueOf(pageNum);
   }

   /**The toString method returns the title as a String
   @return The title as a String*/
   @Override
   public String toString()
   {
      return title;
   }
}//end class
   