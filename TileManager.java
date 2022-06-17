import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.awt.Point;
import java.util.ArrayList;

// TileManager loads a map text file, sets properties of all tiles, and draws all tiles on the map.

public class TileManager {

   GamePanel gp;
   Tile[] tile;
   int mapTileNum[][];
   ArrayList<Point> spawnLocations = new ArrayList<Point>();
   
   public TileManager(GamePanel gp) {
      this.gp = gp;
      tile = new Tile[10];
      mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
      
      getTileImage();
      loadMap("/maps/map01.txt");
      getSpawnLocations();
   }

   // Sets properties of all tiles, corresponding array index numbers to set sprites and properties.
   // Properties include collision, collision with bullet, and transparency.
   public void getTileImage() {
      tile[0] = new Tile();
      tile[0].color = new Color(247, 166, 133);
      tile[0].collision = false;
      tile[0].collisionBullet = false;
      tile[0].channel = 0;
      
      tile[1] = new Tile();
      tile[1].color = new Color(235, 118, 84);
      tile[1].collision = true;
      tile[1].collisionBullet = true;      
      tile[1].channel = 0;
      
      tile[2] = new Tile();
      tile[2].color = new Color(46, 173, 48);
      tile[2].collision = false;
      tile[2].collisionBullet = false;      
      tile[2].channel = 0;
     
      tile[3] = new Tile();
      tile[3].color = new Color(6, 133, 8);
      tile[3].collision = false;
      tile[3].collisionBullet = false;      
      tile[3].channel = 0;
           
      tile[4] = new Tile();
      tile[4].color = new Color(29, 152, 224);
      tile[4].collision = true;
      tile[4].collisionBullet = false;            
      tile[4].channel = 0;
      
      tile[5] = new Tile();
      tile[5].color = new Color(247, 166, 133);
      tile[5].collision = false;
      tile[5].collisionBullet = false;      
      tile[5].channel = 0;
      
      tile[6] = new Tile();
      tile[6].color = new Color(205, 96, 124);
      tile[6].collision = true;
      tile[6].collisionBullet = true;      
      tile[6].channel = 0;
            
   //      tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/.png"));
   
   }
   
   // Loads tile map information into an array based on a text file located in the game directory.
   public void loadMap(String filePath) {
      try {
         InputStream is = getClass().getResourceAsStream(filePath);
         BufferedReader br = new BufferedReader(new InputStreamReader(is));
         
         int row = 0;
         int col = 0;
         
         while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            String line = br.readLine();
            
            while (col < gp.maxWorldCol) {
               String numbers[] = line.split(" ");
               int num = Integer.parseInt(numbers[col]);
               mapTileNum[col][row] = num;
               col++;
            }
            if (col == gp.maxWorldCol) {
               col = 0;
               row++;
            }
         }
         br.close();
      } catch (Exception e) {
      
      }
   }
   
   // Searches all map tiles and adds all spawn location tiles to a spawn location ArrayList.
   public void getSpawnLocations() {
      
      int row = 0;
      int col = 0;
      
      while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
         int tileNum = mapTileNum[col][row];
         if (tileNum == 5) {
            spawnLocations.add(new Point(col * gp.tileSize, row * gp.tileSize));
         }
         col++;
         
         if (col == gp.maxWorldCol) {
            col = 0;
            row++;
         } 
      }      
   }
   
   // Draws all tiles within the map tile array, relative to the position of the player.
   public void draw(Graphics2D g2, int channel) { 
      
      int row = 0;
      int col = 0;
      
      while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
         int tileNum = mapTileNum[col][row];
         int posX = col * gp.tileSize;
         int posY = row * gp.tileSize;
         double screenX = posX - gp.player.posX + gp.player.screenX;
         double screenY = posY - gp.player.posY + gp.player.screenY;
         
         if (true) {
            
            g2.setColor(tile[tileNum].color); 
            if (tile[tileNum].channel == channel) {  
               g2.fill(new Rectangle2D.Double(screenX, screenY, gp.tileSize, gp.tileSize));
            }
         }
         
         col++;
         
         if (col == gp.maxWorldCol) {
            col = 0;
            row++;
         } 

      }      
   }
}