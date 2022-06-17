import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput implements KeyListener {

   public boolean upPressed, leftPressed, downPressed, rightPressed, spacePressed, shiftPressed, shiftReleased, tabPressed, tabReleased;
   boolean movementPressed = false;
   
   public void keyTyped(KeyEvent e) {
   
   }

   public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_W) {
         upPressed = true;
      }
      if (e.getKeyCode() == KeyEvent.VK_A) {
         leftPressed = true;
      }
      if (e.getKeyCode() == KeyEvent.VK_S) {
         downPressed = true;
      }
      if (e.getKeyCode() == KeyEvent.VK_D) {
         rightPressed = true;
      }                  
      
      if (upPressed || leftPressed || downPressed || rightPressed) {
         movementPressed = true;
      }
      
      if (e.getKeyCode() == KeyEvent.VK_SPACE) {
         spacePressed = true;
      }      
      
      if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
         shiftPressed = true;
      }        
      
      if (e.getKeyCode() == KeyEvent.VK_TAB) {
         tabPressed = true;
      }
   }


   public void keyReleased(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_W) {
         upPressed = false;
      }
      if (e.getKeyCode() == KeyEvent.VK_A) {
         leftPressed = false;
      }
      if (e.getKeyCode() == KeyEvent.VK_S) {
         downPressed = false;
      }
      if (e.getKeyCode() == KeyEvent.VK_D) {
         rightPressed = false;
      }          
      
      if (!upPressed && !leftPressed && !downPressed && !rightPressed) {
         movementPressed = false;
      }      
      
      if (e.getKeyCode() == KeyEvent.VK_SPACE) {
         spacePressed = false;
      }            
      
      if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
         shiftPressed = false;
         shiftReleased = true;
      }  
      
      if (e.getKeyCode() == KeyEvent.VK_TAB) {
         tabPressed = false;
         tabReleased = true;
      }      
   }
}