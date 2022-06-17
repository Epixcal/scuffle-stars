// Primo is a close-ranged tank with moderate movement speed and a large amount of health.
// His base attack is a close-range fury of punches.
// With his super he leaps through the air and crashes, dealing damage and destroying walls on impact.

public class Primo extends Scuffler {

   public Primo(GamePanel gp, KeyInput ki, MouseInput mi, double x, double y) {
      super(gp, x, y, 3 * gp.tileSize, 9 * gp.tileSize, 360, 3.25 * gp.tileSize, 6000, 30, 10 * gp.tileSize, 4, 150, 1, 5, 0.8, 12, true);
      scufflerID = "PRIMO";
      radiusSuper = true;
      radiusArea = 200;
      lineShot = true;
      pierceShot = true;
      this.ki = ki;
      this.mi = mi;
      screenX = gp.screenWidth / 2 - sizeX / 2;
      screenY = gp.screenHeight / 2 - sizeY / 2;
      centerScreenX = screenX + (sizeX / 2);
      centerScreenY = screenY + (sizeY / 2); 
   } 

   public Primo(GamePanel gp, double x, double y) {
      super(gp, x, y, 3 * gp.tileSize, 9 * gp.tileSize, 360, 3.25 * gp.tileSize, 6000, 30, 10 * gp.tileSize, 4, 150, 1, 5, 0.8, 12, true);
      scufflerID = "PRIMO";
      radiusSuper = true;  
      radiusArea = 200;
      lineShot = true;
      pierceShot = true;
             
   }

   // Sets the values of a default bullet shot by Primo.
   public Bullet setDefaultBulletValues(Bullet b) {
      b.pierce = true;
      return b;
   }

   // Activates Primo's super ability.
   // Primo leaps into the air and crashes onto the ground in 2 seconds, dealing damage and destroying walls on impact.
   public void activateSuper() {
      inputEnabled = false;
      double airAngle = Math.atan2(throwY - (posY + sizeY / 2), throwX - (posX + sizeX / 2));   
      initialX = posX;
      initialY = posY;
      initialSizeX = sizeX;
      initialSizeY = sizeY;
      isAimingSuper = false;
      inAir = true;
      timeInAir = ((double)throwRange / superRange) * 2;
      heightScale = 100;
      airRange = throwRange;
      speedX = ((Math.cos(airAngle) * airRange) / timeInAir) / gp.FPS;
      speedY = ((Math.sin(airAngle) * airRange) / timeInAir) / gp.FPS;         
         
   }
   
   // Updates values from Primo's super ability.
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
            Bullet smash = new Bullet(posX + sizeX / 2, posY + sizeY / 2);
            smash.explosionRadius = 200;
            smash.damage = 800;
            smash.destroysWalls = true;
            
            checkExplodeCollision(smash); 
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


}