import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
public class BoingDriver {
   static FileWriter f;
   public static void main(String[] args) {
      Screenshot.capture();
      Image image = new ImageIcon("image.png").getImage();
      
      Color[][] array = getArray(toBufferedImage(image));
     
      point[] points = searchStartPoint(array);
      
      MouseAction.instantiate();   
      MouseAction.click(points[2].y - 135, points[3].x - 120, 100);
      for (int i=0; i<500; i++) {
         System.out.println("Jump# " + String.valueOf(i));
         image.flush();
         Screenshot.capture();
         image = new ImageIcon("image.png").getImage();
         array = getArray(toBufferedImage(image));
         //sort out image
         
         point point = searchPlayer(array, points[0].y, points[2].y, points[0].x, points[2].x);
         point.y += 55; //account for dispositioning
         point.x += 5;
         //find player
         point point2;
         if (point.x <= (points[0].y + (points[2].y - points[0].y)/2)) {
            point2 = identifyPad(array, point.x + 12, points[2].y - 10, points[0].x + 200, points[2].x - 50, array[points[3].x - 500][points[2].y - 175]);
         }
         else {
            point2 = identifyPad(array, points[0].y + 10, point.x - 12, points[0].x + 200, points[2].x - 50, array[points[3].x - 500][points[2].y - 175]);
         }
         System.out.println(String.valueOf(point2.x) + " " + String.valueOf(point2.y));
         System.out.println("Distance " + String.valueOf(distance(point, point2)));
         MouseAction.move(point2.x, point2.y);
         MouseAction.click(point2.x, point2.y, processFunction(distance(point, point2)));
         try {
            Thread.sleep(1000);
         }
         catch (Exception e) {
            System.out.println("Uh oh");
            System.exit(0);
         }
         
      }
         
         System.exit(0);
   }
   private static int processFunction(int distance) {
      if (((distance * 4.8) > 250) && ((distance * 4.8) < 2000)) {
         return (int)(distance * 4.82);
      }
      else {
         return 250;
      }
   }
   private static int distance(point p1, point p2) {
      return (int) Math.sqrt(Math.pow(Math.abs(p2.x - p1.x), 2) + Math.abs(p2.y - p1.y));
   }
   private static point identifyPad(Color[][] image, int minX, int maxX, int minY, int maxY, Color c) {
      //Color c = new Color(208, 211, 222);
      System.out.println("Background Value ");
      System.out.println(String.valueOf(c));
      Color c3 = new Color(245, 245, 245);
      Color c2 = new Color(141, 143, 150);
      point[] point = new point[2];
      for (int i=minY; i<maxY; i++) {
         if (equal(image[i][minX+1], c, 15)) {
            c = image[i][minX+1];
         }
         for (int j=minX; j<maxX; j++) {
            if (!equal(image[i][j], c, 15)) {
               System.out.println("First Different Value ");
               System.out.println(String.valueOf(image[i][j]));
               int x = j;
               int y = i;
               int dis = y;
               int store = x;    
               int right, left; 
               while (!(equal(image[y][x+1], c2, 1) || equal(image[y][x+1], c, 5))) {
                  x++;
                  if (x > maxX) {
                     break;
                  }
               } 
               right = x;
               while (!(equal(image[y][x-1], c2, 1) || equal(image[y][x-1], c, 5))) {
                  x--;
                  if (x < minX) {
                     break;
                  }
               } 
               left = x;
               if (Math.abs(Math.abs(store - left) - Math.abs(store - right)) < 100) {
                  x = left + (right - left)/2;
               }
               else {
                  System.out.println("error");
                  MouseAction.move(x, y);
                  System.out.println(String.valueOf(image[y][x]));
                  System.out.println(x);
                  System.out.println(y);
                  System.out.println(j);
                  System.out.println(i);
                  x = store;
               }
               if (equal(image[y+1][x], c3, 10)) {
                  while (!(equal(image[y+1][x], c2, 1) || equal(image[y+1][x], c, 15))) {
                     y++;
                  } 
                  y = y - dis - 42;
                  
               return new point(x, dis + y/2);   
               }
               while (!(equal(image[y+1][x], c2, 1) || equal(image[y+1][x], c, 15) || equal(image[y+1][x], c3, 2))) {
                  y++;
                  //
               }   
               if (equal(image[y+1][x], c3, 2)) {
                  return new point(x, y+4);  
               }
               else {
                  y = y - dis - 42; 
                  return new point(x, dis + y/2);   
               }
            }
         }
      }   
      return new point(0, 0);
   }
   private static point hackDot(Color[][] image, int minX, int maxX, int minY, int maxY, Color c, int x, int y) {
      Color c2 = new Color(141, 143, 150);
      Color c3 = new Color(245, 245, 245);
      while (!(equal(image[y+1][x], c2, 1) || equal(image[y+1][x], c, 10) || equal(image[y+1][x], c3, 2))) {
         y++;
      }
      if (equal(image[y+1][x], c3, 5)) {
         System.out.println(x);
         System.out.println(y);
         return new point(x, y+4);
      }
      else {
         return new point(0, 0);
      }
   }
   private static point midpoint(point[] points) {
      point point = new point(points[0].x, points[1].y);
      return point;
   }
   private static point searchPlayer(Color[][] image, int minX, int maxX, int minY, int maxY) {
      point point = new point(0, 0);
      Color c = new Color(68, 65, 105);    
      for (int i=minX; i<maxX; i++) {
         for (int j=minY; j<maxY; j++) {
            if (equal(image[j][i], c, 1)) {
               point.x = i;
               point.y = j;
               return point;
            }
         }
      }
      return point;
   }
   private static point[] searchStartPoint(Color[][] image) {   //find bounds of game window
      //x is y, y is x;
      Color c = new Color(116, 119, 125);
      Color c2 = new Color(103, 104, 109);
      point[] points = new point[4];
      boolean a = false;
      breakpoint: {
         for (int i=0; i<image.length - 50; i++) {
            for (int j=0; j<image[0].length - 50; j++) {
               if (equal(image[i][j], c, 1))  {
                  a = true;
                  points[0] = new point(i, j);
                  break breakpoint;
               }              
            }
         }
      }
      if (a) {
         a = false;
         int temp = points[0].y; //vertical
         while (equal(image[points[0].x][temp], c, 1)) {
            temp++;
         }
         temp--;
         points[1] = new point(points[0].x, temp);
         breakpoint2: {
            for (int i=image.length-50; i>= 0; i--) {
               for (int j = image[0].length-50; j>= 0; j--) {
                  if (equal(image[i][j], c2, 1))  {
                     a = true;
                     points[2] = new point(i, j);
                     points[3] = new point(points[2].x, points[0].y);
                     break breakpoint2;
                  }              
               }
            }
         }
         
         return points;
      }
      else {
         return points; 
      }
   }
   private static boolean equal(Color a, Color b, int d) {
      if (within(a.getRed(), b.getRed(), d) && within(a.getBlue(), b.getBlue(), d) && within(a.getGreen(), b.getGreen(), d)) {
         return true;
      }
      else {
         return false;
      }
   }
   private static boolean within(int a, int b, int d) {
      if (Math.abs(a - b) <= d) {
         return true;
      }
      else {
         return false;
      }
   } 
   private static Color[][] getArray(BufferedImage img) {
      Color[][] arr;
      int numcols = img.getWidth();
      int numrows = img.getHeight();
      arr = new Color[numrows][numcols];
      for(int j = 0; j < arr.length; j++) {
         for(int k = 0; k < arr[0].length; k++) {
            int rgb = img.getRGB(k,j);
            arr[j][k] = new Color(rgb);
         }
      }
      return arr;
   }
   private static BufferedImage toBufferedImage(Image img) {
      if (img instanceof BufferedImage) {
         return (BufferedImage) img;
      }
      // Create a buffered image with transparency
      BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
      // Draw the image on to the buffered image
      Graphics2D bGr = bimage.createGraphics();
      bGr.drawImage(img, 0, 0, null);
      bGr.dispose();
      // Return the buffered image
      return bimage;
   }
   private static class point {
      int x;
      int y;
      public point(int xc, int yc) {
         x = xc;
         y = yc;
      }
   }
}
