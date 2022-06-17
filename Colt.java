// Colt is a moderate-ranged shooter that has moderate movement speed and a moderate amount of health.
// His base attack is a barrage of six bullets.
// With his super he shoots twelve powerful bullets that can destroy walls.

public class Colt extends Scuffler {

   public Colt(GamePanel gp, KeyInput ki, MouseInput mi, double x, double y) {
      super(gp, x, y, 9 * gp.tileSize, 11 * gp.tileSize, 360, 3 * gp.tileSize, 2800, 15, 11 * gp.tileSize, 6, 100, 1, 6, 1.7, 12, true);
      scufflerID = "COLT";
      lineSuper = true;
      lineShot = true;
      this.ki = ki;
      this.mi = mi;
      screenX = gp.screenWidth / 2 - sizeX / 2;
      screenY = gp.screenHeight / 2 - sizeY / 2;
      centerScreenX = screenX + (sizeX / 2);
      centerScreenY = screenY + (sizeY / 2); 
   } 

   public Colt(GamePanel gp, double x, double y) {
      super(gp, x, y, 9 * gp.tileSize, 11 * gp.tileSize, 360, 3 * gp.tileSize, 2800, 15, 11 * gp.tileSize, 6, 100, 1, 6, 1.7, 12, true);
      scufflerID = "COLT";
      lineSuper = true;  
      lineShot = true;
             
   }
   
   // Sets the values of a super bullet shot by Colt.
   public Bullet setSuperBulletValues(Bullet b) {
      b.destroysWalls = true;
      b.damage = 320;
      return b;
   }

   // Activates Colt's super ability.
   // Colt shoots twelve powerful bullets that can destroy walls.
   public void activateSuper() {
      bulletShot = true;
      shootBullet(12, true, 100);
   }
   
   // Updates a super bullet shot by Colt.
   public Bullet updateSuperStraightBullet(Bullet b) {
      b = updateStraightBullet(b);
      return b;
   }

}