import java.awt.geom.Ellipse2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

// Bullet is a type of Entity that is shot by a Scuffler. 
// It contains all information about the bullet including damage, speed, range,
// and contains various attributes such as being able to bounce, pierce, and destroy walls. 

public class Bullet extends Entity {
   
   // Base Settings
   double damage;
   double speed;
   double speedX;
   double speedY;
   Color color;
  
   // Position Settings
   double initialX;
   double initialY;
   double bulletAngle;
   int tileSide;
   
   boolean collisionX = false;
   boolean collisionY = false;   
   
   // Throw Settings
   double throwRange;
   double throwScale;
   double timeInAir;
   boolean bulletInAir;
   boolean bulletBounced = false;
   boolean bulletBouncing = false;
   double explosionRadius = -1;
   
   // Group Settings
   int groupNum; 
   boolean initialized;

   // Attributes
   boolean bounce = false;
   boolean pierce = false;
   ArrayList<Integer> pierceIndexes = new ArrayList<Integer>();
   int numBounces;
   boolean superBullet = false;
   boolean destroysWalls = false;

   boolean timerActivated = false;

   public Bullet(double x, double y) {
      posX = x;
      posY = y;
      sizeX = 0;
      sizeY = 0;
   }

   // Initial constructor for bullet, including its group number in a bullet set, 
   // and whether or not the bullet is a bullet from a Scuffler's special ability.
   public Bullet(int groupNum, boolean superBullet) {
      super();   
      this.groupNum = groupNum;
      this.superBullet = superBullet; 
      initialized = false;
   }
      
   // Default constructor for bullet, listing starting position, size, and speed.   
   public Bullet(GamePanel gp, double x, double y, double sizeX, double sizeY, double speed, Color color) {
      super(gp, x, y, sizeX, sizeY);
      this.speed = speed;
      this.color = color;
      initialized = true;
   }
   
   // Draws the bullet onto the screen at a given x and y position.
   public void draw(Graphics2D g2, double x, double y) {
      g2.setColor(color);
      g2.fill(new Ellipse2D.Double(x, y, this.sizeX, this.sizeY));   
   }

   // UNFINISHED
   // Generates explosion particles around the bullet's final position.
   public void impact() {
   
   }
   
   // Expands the size of the bullet based on a given range parameter. 
   public void expandRange(double range) {
      sizeX += range;
      sizeY += range;
   }
   
   // Sets the position of the bullet based on given x and y parameters.
   public void setPosition(double x, double y) {
      posX = x;
      posY = y;
   }   
}