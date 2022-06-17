import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputListener;

public class MouseInput implements MouseInputListener {

   int mouseX, mouseY;

   public void mouseClicked(MouseEvent e) {

   }
   
   public void mouseEntered(MouseEvent e) {
   
   }
   
   public void mouseExited(MouseEvent e) {
   
   }
   
   public void mousePressed(MouseEvent e) {

   }
   
   public void mouseReleased(MouseEvent e) {

   }
   
   public void mouseDragged(MouseEvent e) {
      mouseX = e.getX();
      mouseY = e.getY();   
   }
   
   public void mouseMoved(MouseEvent e) {
      mouseX = e.getX();
      mouseY = e.getY();
   }
   
}