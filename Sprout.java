import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Color;
import java.util.ArrayList;
import java.awt.Point;

// Sprout is a medium-ranged thrower that has moderate movement speed and a moderate amount of health.
// Its base attack is a ball lobbed into the air that can travel over obstacles. When it reaches the
// ground it will bounce, and explode on impact.
// With its super it creates a temporary wall that will disappear after a short period of time.

public class Sprout extends Scuffler {

   public Sprout(GamePanel gp, KeyInput ki, MouseInput mi, double x, double y) {
      super(gp, x, y, 5 * gp.tileSize, 7 * gp.tileSize, 980, 3 * gp.tileSize, 3000, 35, 10 * gp.tileSize, 1, 150, 1, 4, 1.7, 5, true);
      scufflerID = "SPROUT";
      radiusSuper = true;
      radiusArea = 110;
      throwShot = true;
      this.ki = ki;
      this.mi = mi;
      screenX = gp.screenWidth / 2 - sizeX / 2;
      screenY = gp.screenHeight / 2 - sizeY / 2;
      centerScreenX = screenX + (sizeX / 2);
      centerScreenY = screenY + (sizeY / 2); 
   } 

   public Sprout(GamePanel gp, double x, double y) {
      super(gp, x, y, 5 * gp.tileSize, 7 * gp.tileSize, 980, 3 * gp.tileSize, 3000, 35, 10 * gp.tileSize, 1, 150, 1, 4, 1.7, 5, false);
      scufflerID = "SPROUT";
      radiusSuper = true;
      radiusArea = 110;      
      throwShot = true;     
   }
   
   // Sets the values of a default bullet shot from Sprout.
   public Bullet setDefaultBulletValues(Bullet b) {
      b.bounce = true;
      b.explosionRadius = 20;
      b.timeInAir = 0.6;
      b.bulletInAir = true;
      b.color = new Color(22, 243, 59);
      return b;   
   }
   
   // Sets the values of a super bullet shot from Sprout.
   public Bullet setSuperBulletValues(Bullet b) {
      b.timeInAir = 1;
      b.bulletInAir = true;
      b.color = new Color(204, 95, 126);
      return b;
   }
   
   // Updates super bullets that have been thrown by Sprout.
   public Bullet updateSuperThrowBullet(Bullet b) {
      double bulletRange = Math.sqrt(Math.pow(b.posX - b.initialX, 2) + Math.pow(b.posY - b.initialY, 2));
     
      if (b.bulletInAir) {  
         if (bulletRange > b.throwRange) {            
            b.impact();
            createWall(b);
            b = null;
            return b;   
         }
      }
      
      if (b.bulletInAir) { 
         b.sizeX += b.throwScale / gp.FPS;
         b.sizeY += b.throwScale / gp.FPS;
         b.throwScale = accelerate(100, b.throwScale, -100, b.timeInAir);                
      }
      
      b.posX += b.speedX;
      b.posY += b.speedY;
      
      return b;      
   }   

   // Activates Sprout's super ability.
   // Sprout creates a hedge barrier. The barrier has a 3-by-3, 5-tile cross pattern from the center of the seed and blocks enemies' and allies' paths. 
   // However, if there are walls at most one tile away adjacent to where the seed was planted, the hedge will grow towards them and connect with the walls. 
   // The hedges can last for a maximum of 10 seconds, and more than one hedge can be active on the arena at a time.
   public void activateSuper() {
      bulletShot = true;
      shootBullet(1, true, bulletGroupDelay);
   }
   
   public void createWall(Bullet b) {
      int midCol = cc.getColumn(b.posX + b.sizeX / 2);
      int midRow = cc.getRow(b.posY + b.sizeY / 2);
      
      
      if (!gp.tm.tile[gp.tm.mapTileNum[midCol][midRow]].collision) {
         gp.tm.mapTileNum[midCol][midRow] = 6;      
      } else {
         cc.checkMergeCollision(b);  
         midCol = cc.getColumn(b.posX + b.sizeX / 2);
         midRow = cc.getRow(b.posY + b.sizeY / 2);       
         gp.tm.mapTileNum[midCol][midRow] = 6;              
      }
      ArrayList<Point> tileList = new ArrayList<Point>();
      tileList.add(new Point(midCol, midRow));    
        
      for (int col = 1; col <= 3; col++) {
         for (int row = 1; row <= 3; row++) {
            if (!gp.tm.tile[gp.tm.mapTileNum[col + midCol - 2][row + midRow - 2]].collision) {        
               if (!(row == 1 && col == 1) && !(row == 3 && col == 3) &&
                   !(row == 1 && col == 3) && !(row == 3 && col == 1)) {
                  int wallCol = col + midCol - 2;
                  int wallRow = row + midRow - 2;
                  gp.tm.mapTileNum[wallCol][wallRow] = 6;
                  tileList.add(new Point(wallCol, wallRow));
                  if (gp.tm.mapTileNum[wallCol][wallRow - 2] == 1 && !gp.tm.tile[gp.tm.mapTileNum[wallCol][wallRow- 1]].collision) {
                     gp.tm.mapTileNum[wallCol][wallRow - 1] = 6;
                     tileList.add(new Point(wallCol, wallRow - 1));
                  }
                  if (gp.tm.mapTileNum[wallCol][wallRow + 2] == 1 && !gp.tm.tile[gp.tm.mapTileNum[wallCol][wallRow + 1]].collision) {
                     gp.tm.mapTileNum[wallCol][wallRow + 1] = 6;
                     tileList.add(new Point(wallCol, wallRow + 1));               
                  }
                  if (gp.tm.mapTileNum[wallCol - 2][wallRow] == 1 && !gp.tm.tile[gp.tm.mapTileNum[wallCol - 1][wallRow]].collision) {
                     gp.tm.mapTileNum[wallCol - 1][wallRow] = 6;
                     tileList.add(new Point(wallCol - 1, wallRow));               
                  }
                  if (gp.tm.mapTileNum[wallCol + 2][wallRow] == 1 && !gp.tm.tile[gp.tm.mapTileNum[wallCol + 1][wallRow]].collision) {
                     gp.tm.mapTileNum[wallCol + 1][wallRow] = 6;
                     tileList.add(new Point(wallCol + 1, wallRow));               
                  }                         
                                 
               }
            }
         }   
      }
      
      for (int i = 0; i < gp.playerList.size(); i++) {
         cc.checkMergeCollision(gp.playerList.get(i));
      }
      
      Timer wallTimer = new Timer();
      wallTimer.schedule(
            new TimerTask() {
               @Override
               public void run() {
                  for (int i = 0; i < tileList.size(); i++) {
                     gp.tm.mapTileNum[tileList.get(i).x][tileList.get(i).y] = 0;
                  }
                  superActive = false;
               }
            }, 10 * 1000);
      
   }
               
}