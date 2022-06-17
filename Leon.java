import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.BasicStroke;
import java.awt.geom.AffineTransform; 
import java.awt.Font;
import java.awt.Shape;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;

// Leon is a close-ranged assassin that has fast movement speed and a moderate amount of health.
// His base attack is four blades that travel outward in a cone shape. The farther they travel, the less damage they do.
// With his super he is able to turn invisible for a short period of time. If he shoots he will become visible. 

public class Leon extends Scuffler {

   // Invisibility Settings
   boolean invisibility = false; 
   Timer invisTimer;   
   double maxInvisTime = 6;
   double invisTime = maxInvisTime;


   public Leon(GamePanel gp, KeyInput ki, MouseInput mi, double x, double y) {
      super(gp, x, y, 10 * gp.tileSize, 0, 480, 3.5 * gp.tileSize, 3200, 25, 10 * gp.tileSize, 4, 150, 3, 6, 1.9, 8, true);
      scufflerID = "LEON";
      abilitySuper = true;
      coneShot = true;
      this.ki = ki;
      this.mi = mi;
      screenX = gp.screenWidth / 2 - sizeX / 2;
      screenY = gp.screenHeight / 2 - sizeY / 2;
      centerScreenX = screenX + (sizeX / 2);
      centerScreenY = screenY + (sizeY / 2); 
   } 

   public Leon(GamePanel gp, double x, double y) {
      super(gp, x, y, 10 * gp.tileSize, 0, 480, 3.5 * gp.tileSize, 3200, 25, 10 * gp.tileSize, 4, 150, 3, 6, 1.9, 8, false);
      scufflerID = "LEON";
      abilitySuper = true;
      coneShot = true;      
   }
   
   // Activates Leon's super, invisibility. Duration lasts for six seconds, and is cancelled if he attacks. 
   // If he takes damage, he will momentarily be visible.
   public void activateSuper() {
      invisibility = true;   
      invisTimer = new Timer(); 
      invisTimer.scheduleAtFixedRate(            
            new TimerTask() {
               @Override
               public void run() {
                  invisTime -= maxInvisTime / (maxInvisTime * gp.FPS); 
               }
            }, 0, (long)(1.0 / gp.FPS * 1000));
   } 
   
   // Updates visibility of Leon. If invisibile, he cannot be seen, unless he shoots or the timer runs out.
   public void updateVisibility() {
      super.updateVisibility();
      if (invisibility) {
         visible = false;
      } 
   
      if (invisibility) {
         if (bulletShot || invisTime <= 0) {                                 
            invisibility = false;
            superActive = false;
            invisTime = maxInvisTime;                        
            invisTimer.cancel();     
         }    
      }
   }   
   
   // Draws player bars, and draws Leon's super timer if player is invisible.
   public void drawBars(Graphics2D g2) {
      super.drawBars(g2);
      if (invisibility && isPlayer) {
         g2.setColor(new Color(30, 40, 62));
         g2.fill(new RoundRectangle2D.Double(barX - 1 - barStroke / 2, barY + 7 - barStroke / 2, barLength + 2 + barStroke, 4 + barStroke, 5, 5)); 
         
         g2.setColor(new Color(251, 229, 88));
         g2.fill(new RoundRectangle2D.Double(barX - 1, barY + 7, (invisTime / maxInvisTime) * (barLength + 2), 4, 5, 5));          
      }
   }
   
   // Calculates damage from bullet collision.
   public void calculateDamage(Bullet b) {
      double distance = Math.sqrt(Math.pow(b.posX - b.initialX, 2) + Math.pow(b.posY - b.initialY, 2));
      damageDealt = (int)Math.round(b.damage - (distance * 0.7));               
   }
   
}