import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.lang.Integer;
import java.awt.Point;

// CollisionChecker contains various methods that check for collision and interaction between entities, other entities, and tiles.

public class CollisionChecker {
   
   GamePanel gp;
   TileManager tm;
   
   public CollisionChecker(GamePanel gp, TileManager tm) {
      this.gp = gp;
      this.tm = tm;
   }
   
   // Gets the column number on the screen based on a given x parameter.
   public int getColumn(double x) {
      return (int)Math.floor(x / gp.tileSize);   
   }
   
   // Gets the row number on the screen based on a given y parameter.
   public int getRow(double y) {
      return (int)Math.floor(y / gp.tileSize);   
   }
   
   // Gets the Area object of an entity on the screen, which is transformed based on given speed parameters. 
   public Area getEntityArea(Entity e, double speedX, double speedY) {
      Area entityArea;
      AffineTransform af = new AffineTransform();
      af.translate(speedX / gp.FPS, speedY / gp.FPS);
      if (e instanceof Scuffler) {
         entityArea = new Area(new Ellipse2D.Double(e.posX, e.posY, e.sizeX, e.sizeY));
      } else {
         af.rotate(e.entityAngle, e.posX + (e.sizeX / 2), e.posY + (e.sizeY / 2));
         entityArea = new Area(new Rectangle2D.Double(e.posX, e.posY, e.sizeX, e.sizeY));
      }
      return entityArea.createTransformedArea(af);   
         
   }

   // Checks for tile collision of an entity that has been transformed by given speed parameters. Returns true if collision.
   public boolean checkTileCollision(Entity e, double speedX, double speedY) {
      Area entityArea = getEntityArea(e, speedX, speedY);
      
      boolean up = false, down = false, left = false, right = false, rotate = false;
   
      if (speedX > 0) {
         right = true;
      } else if (speedX < 0) {
         left = true;
      } 
      if (speedY > 0) {
         down = true;
      } else if (speedY < 0) {
         up = true;
      }
      
      
      int col = getColumn(e.posX + (e.sizeX / 2));
      int row = getRow(e.posY + (e.sizeY / 2));
      
      Rectangle2D t1 = new Rectangle2D.Double(col * gp.tileSize, row * gp.tileSize, gp.tileSize, gp.tileSize);
      Rectangle2D t2 = new Rectangle2D.Double(col * gp.tileSize, row * gp.tileSize, gp.tileSize, gp.tileSize);
      Rectangle2D t3 = new Rectangle2D.Double(col * gp.tileSize, row * gp.tileSize, gp.tileSize, gp.tileSize);
      
      int tn1 = gp.tm.mapTileNum[col][row]; 
      int tn2 = gp.tm.mapTileNum[col][row]; 
      int tn3 = gp.tm.mapTileNum[col][row]; 
      
      if (up) {
         t1 = new Rectangle2D.Double(col * gp.tileSize, (row - 1) * gp.tileSize, gp.tileSize, gp.tileSize); 
         t2 = new Rectangle2D.Double((col - 1) * gp.tileSize, (row - 1) * gp.tileSize, gp.tileSize, gp.tileSize); 
         t3 = new Rectangle2D.Double((col + 1) * gp.tileSize, (row - 1) * gp.tileSize, gp.tileSize, gp.tileSize);    
         tn1 = gp.tm.mapTileNum[col][row - 1];     
         tn2 = gp.tm.mapTileNum[col - 1][row - 1];     
         tn3 = gp.tm.mapTileNum[col + 1][row - 1];      
      }
      if (down) {
         t1 = new Rectangle2D.Double(col * gp.tileSize, (row + 1) * gp.tileSize, gp.tileSize, gp.tileSize); 
         t2 = new Rectangle2D.Double((col - 1) * gp.tileSize, (row + 1) * gp.tileSize, gp.tileSize, gp.tileSize); 
         t3 = new Rectangle2D.Double((col + 1) * gp.tileSize, (row + 1) * gp.tileSize, gp.tileSize, gp.tileSize);  
         tn1 = gp.tm.mapTileNum[col][row + 1];     
         tn2 = gp.tm.mapTileNum[col - 1][row + 1];     
         tn3 = gp.tm.mapTileNum[col + 1][row + 1];    
      }
      if (left) {
         t1 = new Rectangle2D.Double((col - 1) * gp.tileSize, row * gp.tileSize, gp.tileSize, gp.tileSize); 
         t2 = new Rectangle2D.Double((col - 1) * gp.tileSize, (row - 1) * gp.tileSize, gp.tileSize, gp.tileSize); 
         t3 = new Rectangle2D.Double((col - 1) * gp.tileSize, (row + 1) * gp.tileSize, gp.tileSize, gp.tileSize);  
         tn1 = gp.tm.mapTileNum[col - 1][row];     
         tn2 = gp.tm.mapTileNum[col - 1][row - 1];     
         tn3 = gp.tm.mapTileNum[col - 1][row + 1];    
      }
      if (right) {
         t1 = new Rectangle2D.Double((col + 1) * gp.tileSize, row * gp.tileSize, gp.tileSize, gp.tileSize); 
         t2 = new Rectangle2D.Double((col + 1) * gp.tileSize, (row - 1) * gp.tileSize, gp.tileSize, gp.tileSize); 
         t3 = new Rectangle2D.Double((col + 1) * gp.tileSize, (row + 1) * gp.tileSize, gp.tileSize, gp.tileSize);  
         tn1 = gp.tm.mapTileNum[col + 1][row];     
         tn2 = gp.tm.mapTileNum[col + 1][row - 1];     
         tn3 = gp.tm.mapTileNum[col + 1][row + 1];              
      }
   
      if (e instanceof Bullet) {
         if ((!gp.tm.tile[tn1].collision || !gp.tm.tile[tn1].collisionBullet)) {
            return false;
         }
      }
   
      if (gp.tm.tile[tn1].collision && entityArea.intersects(t1) ||
          gp.tm.tile[tn2].collision && entityArea.intersects(t2) ||
          gp.tm.tile[tn3].collision && entityArea.intersects(t3)) {
         return true;
      } else {
         return false;      
      }   
   }
   
   // Checks for collision of an entity on a select tile based on given x and y parameters. Returns true if collision.
   public boolean checkSelectTileCollision(Entity e, double x, double y) {
      int col = getColumn(x);
      int row = getRow(y);
      int tn = gp.tm.mapTileNum[col][row];
      Area entityArea = getEntityArea(e, 0, 0);
      Rectangle2D tile = new Rectangle2D.Double(col * gp.tileSize, row * gp.tileSize, gp.tileSize, gp.tileSize);      
      if (gp.tm.tile[tn].collision && entityArea.intersects(tile)) {
         return true;
      } else {
         return false;
      }
   }
  
  // Checks if a tile on the map has a collision property at the given x and y parameters. Returns true if collision.
   public boolean getTileCollision(double x, double y) {
      int col = getColumn(x);
      int row = getRow(y);
      if (gp.tm.tile[gp.tm.mapTileNum[col][row]].collision) {
         return true;
      } else {
         return false;
      }
   }
  
  // UNFINISHED
  // Gets the closest x and y coordinates of a tile in a Point object that does not have a collision property.
  // Used for when an entity has been merged into a collision tile, and needs to be pushed out. 
   public Point2D getUnmergePosition(double x, double y) {
         int midCol = getColumn(x);
         int midRow = getRow(y);
         ArrayList<Point> surroundingTiles = new ArrayList<Point>();
         for (int col = 1; col <= 3; col++) {
            for (int row = 1; row <= 3; row++) {
               if (!gp.tm.tile[gp.tm.mapTileNum[col + midRow - 2][row + midRow - 2]].collision) {
                  surroundingTiles.add(new Point(col + midRow - 2, row + midRow - 2)); 
               }         
            }
         }      
         double distance = Math.sqrt(Math.pow(x - surroundingTiles.get(0).x * gp.tileSize, 2) + Math.pow(y - surroundingTiles.get(0).y * gp.tileSize, 2)); 
         double initialDistance;
         int index = 0;
         int closestIndex = 0;
         for (index++; index < surroundingTiles.size(); index++) {
            initialDistance = distance;
            distance = Math.sqrt(Math.pow(x - surroundingTiles.get(index).x * gp.tileSize, 2) + Math.pow(y - surroundingTiles.get(index).y * gp.tileSize, 2)); 
            if (initialDistance > distance) {
               closestIndex = index;
            } else {
               distance = initialDistance;
            }
         }
         
         double finalX = surroundingTiles.get(closestIndex).x  * gp.tileSize;
         double finalY = surroundingTiles.get(closestIndex).y * gp.tileSize;
         return new Point2D.Double(finalX, finalY);
   }
   
   // UNFINISHED
   // Checks if an entity has been merged into a tile with a collision property.
   // If true, will find the nearest tile coordinate that does not have a collision property and will
   // set its position to that location.
   public void checkMergeCollision(Entity e) {
      if (checkSelectTileCollision(e, e.posX + e.sizeX / 2, e.posY + e.sizeY / 2)) {
         Point2D unmerge = getUnmergePosition(e.posX + e.sizeX / 2, e.posY + e.sizeY / 2);
         e.setPosition(unmerge.getX(), unmerge.getY());
      }
   }
   
   // Checks collision between two entities, based on given speed parameters for both entities. Returns true if collision.
   public boolean checkEntityCollision(Entity e1, Entity e2, double speedX1, double speedY1, double speedX2, double speedY2) {
      Area entityArea1 = getEntityArea(e1, speedX1, speedY1);
      Area entityArea2 = getEntityArea(e2, speedX2, speedY2);
      if (entityArea1.intersects(entityArea2.getBounds2D())) {
         return true;
      } else {
         return false;
      }
   }
   
   // UNFINISHED
   // Checks whether the current tile an entity with a wall-destructive property is intersecting is solid. If so, destroys the tile. 
   public void destroyTiles(Entity e) {
      int eCol = getColumn(e.posX + e.sizeX /2);
      int eRow = getRow(e.posY + e.sizeY / 2);
      Area entityArea = getEntityArea(e, 0, 0);
      for (int col = 1; col <= 7; col++) {
         for (int row = 1; row <= 7; row++) {
            if (gp.tm.mapTileNum[col + eCol - 4][row + eRow - 4] == 1 || gp.tm.mapTileNum[col + eCol - 4][row + eRow - 4] == 2 ||
                gp.tm.mapTileNum[col + eCol - 4][row + eRow - 4] == 3) {
               Rectangle2D t = new Rectangle2D.Double((col + eCol - 4) * gp.tileSize, (row + eRow - 4) * gp.tileSize, gp.tileSize, gp.tileSize);
               if (entityArea.intersects(t)) {
                  gp.tm.mapTileNum[col + eCol - 4][row + eRow - 4] = 0;
               }
            }         
         }
      }      
   
   }
   
   // Finds the distance to the nearest point of collision at a given position, angle and range. 
   public double findCollisionDistance(double x, double y, double angle, double range) {
      double initialX = x;
      double initialY = y;
      double distance = 0;
      int tn = 0;
      while (distance < range && gp.tm.tile[tn].collisionBullet == false) {
         distance = Math.sqrt(Math.pow(Math.abs(x - initialX), 2) + Math.pow(Math.abs(y - initialY), 2));        
         x += Math.cos(angle) * 0.01;
         y += Math.sin(angle) * 0.01;
         int col = getColumn(x);
         int row = getRow(y);
         tn = gp.tm.mapTileNum[col][row];
      }
      if (distance > range) {
         distance = range;
      }
      return distance;
   }
   
   // Checks whether or not a player is currently located in a bush tile.
   // If so, visibility is set to false. Otherwise, visibility is set to true.
   public void checkBush(Entity e) {
      int col = getColumn(e.posX + (e.sizeX / 2));
      int row = getRow(e.posY + (e.sizeY / 2));
      if (gp.tm.mapTileNum[col][row] == 2 || gp.tm.mapTileNum[col][row] == 3) {
         e.visible = false;
      } else {
         e.visible = true;
      }
   }
   
   // Reveals bush tiles surrounding a given entity, making them transparent, to a certain extent of range.
   public void checkBushRange(Entity e) {
      int eCol = getColumn(e.posX + (e.sizeX / 2));
      int eRow = getRow(e.posY + (e.sizeY / 2));
   
      for (int col = 1; col <= 7; col++) {
         for (int row = 1; row <= 7; row++) {
            if (gp.tm.mapTileNum[col + eCol - 4][row + eRow - 4] == 3) {
               gp.tm.mapTileNum[col + eCol - 4][row + eRow - 4] = 2;  
            }         
         }
      }
      
      for (int col = 1; col <= 5; col++) {
         for (int row = 1; row <= 5; row++) {
            if (gp.tm.mapTileNum[col + eCol - 3][row + eRow - 3] == 2) {        
               if (!(row == 1 && col == 1) && !(row == 5 && col == 5) &&
                   !(row == 1 && col == 5) && !(row == 5 && col == 1)) {
                  gp.tm.mapTileNum[col + eCol - 3][row + eRow - 3] = 3;                   
               }
            }
         }   
      }
   }
   

}