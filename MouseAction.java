import java.awt.*;
import java.awt.event.MouseEvent;
public class MouseAction {
   private static Robot robot = null;
   public static void main(String[] args) {
      instantiate();
      move(16, 325);
   }
   public static void instantiate() {
      try {
         robot = new Robot();
      }
      catch (Exception e) {
         System.out.println("Uh oh.");
         e.printStackTrace();
         System.exit(0);
      }
   }
   public static void move(int x, int y) {
      robot.mouseMove(x, y);
   }
   public static void click(int x, int y, int d) {
      robot.mouseMove(x, y);
      robot.delay(5);
      robot.mousePress(MouseEvent.BUTTON1_MASK);
      robot.delay(d);
      robot.mouseRelease(MouseEvent.BUTTON1_MASK);
      robot.delay(1000);
   }
}