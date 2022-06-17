// Entity contains basic elements of an entity, including position, size, and screen position.
// It contains methods that can be used universally by all entities, including setting position, and acceleration.

public class Entity {

   double posX;
   double posY;
   double sizeX;
   double sizeY;
   double screenX;
   double screenY;
   double centerScreenX;
   double centerScreenY;
   
   double entityAngle;
   
   boolean visible = true;
     
   GamePanel gp; 
   CollisionChecker cc;
    
   public Entity() {
   
   } 
    
   public Entity (GamePanel gp, double x, double y, double sizeX, double sizeY) {
      this.gp = gp;
      this.cc = gp.cc;
      this.posX = x;
      this.posY = y;
      this.sizeX = sizeX;
      this.sizeY = sizeY;   
   }
   
   public void setPosition(double x, double y) {
      posX = x;
      posY = y;
   }
   
   public double accelerate(double initialValue, double value, double endValue, double timeAccel) {
      if ((endValue - initialValue) > 0) {
         if (value < endValue) {
            value += (endValue - initialValue) / (timeAccel * gp.FPS);
         }
         if (value > endValue) {
            value = endValue;
         }         
      } else {
         if (value > endValue) {
            value -= (initialValue - endValue) / (timeAccel * gp.FPS);
         }
         if (value < endValue) {
            value = endValue;
         }         
      }
      return value;
   }  
   
}