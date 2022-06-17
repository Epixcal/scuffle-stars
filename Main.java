import javax.swing.JFrame;

// Main creates and opens the game window, and starts the game session.

public class Main {
   
   // Edit to change Scuffler
   // (COLT, PRIMO, PIPER, SPROUT, LEON)
   static String scufflerID = "LEON"; 
   
   public static void main(String[] args) {
      JFrame window = new JFrame();
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      window.setResizable(false);
      window.setTitle("Scuffle Stars");
      
      GamePanel gp = new GamePanel(scufflerID);
      window.add(gp);
      window.pack();
      window.setLocationRelativeTo(null);
      window.setVisible(true); 
     
      gp.startGameThread();
   }

}