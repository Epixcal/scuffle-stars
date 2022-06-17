import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Color;

// Piper is a long-ranged sniper with moderate movement speed and a low amount of health.
// Her base attack is a sniper bullet that deals more damage the further the bullet travels.
// With her super she leaps through the air, leaving behind four bombs that explode after a short period of time.

public class Piper extends Scuffler {
   
   Timer bombTimer;

   
   public Piper(GamePanel gp, KeyInput ki, MouseInput mi, double x, double y) {
      super(gp, x, y, 10 * gp.tileSize, 8.5 * gp.tileSize, 350, 3 * gp.tileSize, 2400, 15, 11.5 * gp.tileSize, 1, 150, 1, 8, 2.3, 12, true);
      scufflerID = "PIPER";
      throwSuper = true;
      lineShot = true;
      this.ki = ki;
      this.mi = mi;
      screenX = gp.screenWidth / 2 - sizeX / 2;
      screenY = gp.screenHeight / 2 - sizeY / 2;
      centerScreenX = screenX + (sizeX / 2);
      centerScreenY = screenY + (sizeY / 2); 
   } 

   public Piper(GamePanel gp, double x, double y) {
      super(gp, x, y, 10 * gp.tileSize, 8.5 * gp.tileSize, 350, 3 * gp.tileSize, 2400, 15, 11.5 * gp.tileSize, 1, 150, 1, 8, 2.3, 12, false);
      scufflerID = "PIPER";
      throwSuper = true;  
      lineShot = true;              
   }

   // Sets the values of a super bullet shot by Piper.
   public Bullet setSuperBulletValues(Bullet b) {
      b.destroysWalls = true;
      b.damage = 900;
      b.explosionRadius = 1;
      b.sizeX = 25;
      b.sizeY = 25;
      switch (b.groupNum) {
         case 1:
            b.posX = initialX + gp.tileSize;
            b.posY = initialY;
            break;
         case 2:
            b.posX = initialX;
            b.posY = initialY + gp.tileSize;
            break;
         case 3:
            b.posX = initialX - gp.tileSize;
            b.posY = initialY;
            break;
         case 4:
            b.posX = initialX;
            b.posY = initialY - gp.tileSize;      
      }
            
      return b;
   }

   // Updates super bullets that have been thrown by Piper.
   public Bullet updateSuperThrowBullet(Bullet b) {
      bombTimer = new Timer();
      bombTimer.schedule(
            new TimerTask() {
               @Override
               public void run() {
                  b.impact();
                  checkExplodeCollision(b);
                  b.timerActivated = true;
               
               }
            }, 1000);
            
      if (b.timerActivated) {
         bombTimer.cancel();
      }                  
      return b;
   }
   
   public Bullet updateSuperTimer(Bullet b) {
      b = null;
   
      return b;
   }

   // Activates Piper's super ability.
   // Piper leaps into the air for 2 seconds, leaving behind four bombs that explode after 1 second. 
   public void activateSuper() {
      inputEnabled = false;
      bulletShot = true;
      shootBullet(4, true, 100);
      double airAngle = Math.atan2(throwY - (posY + sizeY / 2), throwX - (posX + sizeX / 2));
      initialX = posX;
      initialY = posY;
      initialSizeX = sizeX;
      initialSizeY = sizeY;
      isAimingSuper = false;
      inAir = true;
      timeInAir = 2;
      heightScale = 100;
      airRange = throwRange;
      speedX = ((Math.cos(airAngle) * airRange) / timeInAir) / gp.FPS;
      speedY = ((Math.sin(airAngle) * airRange) / timeInAir) / gp.FPS;         
         
   }
   
   // Updates values from Piper's super ability.
   public void updateSuper() {
      posX += speedX;
      posY += speedY;
   
      double travelRange = Math.sqrt(Math.pow(posX - initialX, 2) + Math.pow(posY - initialY, 2));
      
      if (inAir) { 
         if (travelRange > airRange) {
            inAir = false;
            if (sizeX > initialSizeX || sizeY > initialSizeY) {
               sizeX = initialSizeX;
               sizeY = initialSizeY;
            } 
            superActive = false;
            inputEnabled = true;  
         } 
      }
            
      if (inAir) { 
         sizeX += heightScale / gp.FPS;
         sizeY += heightScale / gp.FPS;
         heightScale = accelerate(100, heightScale, -100, timeInAir);                
      }   
   
   }

   public void calculateDamage(Bullet b) {
      double distance = Math.sqrt(Math.pow(b.posX - b.initialX, 2) + Math.pow(b.posY - b.initialY, 2));
      damageDealt = (int)Math.round(b.damage + (distance * 3));         
   }

}