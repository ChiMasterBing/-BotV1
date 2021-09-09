import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
public class Screenshot {
   static void capture() {
      try {
         Thread.sleep(120);
         Robot r = new Robot();
           
         // It saves screenshot to desired path
         String path = "image.png";
           
         // Used to get ScreenSize and capture image
         Rectangle capture = 
         new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
         BufferedImage Image = r.createScreenCapture(capture);
         ImageIO.write(Image, "png", new File(path));
         
      }
      catch (AWTException | IOException | InterruptedException ex) {
         System.out.println(ex);
      }
   }
   
}