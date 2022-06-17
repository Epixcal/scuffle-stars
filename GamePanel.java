import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL; 
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.MouseInfo;
import java.awt.RenderingHints;
import java.awt.Font;
import java.io.InputStream;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.BasicStroke;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;
import java.awt.geom.Rectangle2D;
import java.util.Timer;
import java.util.TimerTask;

// GamePanel contains all game elements. 
// Continuously updates and draws game elements at fixed frame-rate. 

public class GamePanel extends JPanel implements Runnable  {
   
   String scufflerID;
   
   KeyInput ki = new KeyInput();
   MouseInput mi = new MouseInput();
   Thread gameThread;
   
   // Screen settings
   public final int tileSize = 40;
   
   public final int maxScreenCol = 31;
   public final int maxScreenRow = 17;
   public final int screenWidth = tileSize * maxScreenCol;
   public final int screenHeight = tileSize * maxScreenRow;
   
   public final int maxWorldCol = 50;
   public final int maxWorldRow = 50;
   public final int worldWidth = tileSize * maxWorldCol;
   public final int worldHeight = tileSize * maxWorldRow;
   
   Font f;
  
   // Set FPS
   int FPS = 60;

   TileManager tm = new TileManager(this);
   CollisionChecker cc = new CollisionChecker(this, tm);
       
   ArrayList<Scuffler> playerList = new ArrayList<Scuffler>(); 
   ArrayList<Scuffler> layerList = new ArrayList<Scuffler>(); 
   Scuffler player;

   boolean gameStarted = false;
   boolean gameActive = false;

   int currentScuffler = 1;
   int numScufflers = 5;
   boolean inputCooldown = false;
   Timer inputTimer = new Timer();
   
   // Loads window and other essential elements.
   public GamePanel(String scufflerID) {
      this.scufflerID = scufflerID;
      this.setPreferredSize(new Dimension(screenWidth, screenHeight));
      this.setBackground(new Color(247, 166, 133));
      this.setDoubleBuffered(true);
      this.addKeyListener(ki);
      this.setFocusTraversalKeysEnabled(false);
      this.addMouseListener(mi);
      this.addMouseMotionListener(mi);
      this.setFocusable(true);
      loadPlayers();
      loadFont("/fonts/LilitaOne-Regular.ttf");
   }
   
   // Initiates game thread.
   public void startGameThread() {
      gameThread = new Thread(this);
      gameThread.start();
   }
   
   // Loads a font based on given file path.
   public void loadFont(String filePath) {
      try {
         InputStream is = getClass().getResourceAsStream(filePath);
         f = Font.createFont(Font.TRUETYPE_FONT, is);
      } catch (Exception e) {
      
      }
   }
   
  // Draws text onto screen based on given x and y coordinates, string, color, size, and stroke size.
   public void drawText(Graphics2D g2, double x, double y, String text, Color color, float size, int stroke) {
      Font font = f.deriveFont(size);
      GlyphVector v = font.createGlyphVector(getFontMetrics(font).getFontRenderContext(), text);
      Shape s = v.getOutline();
      g2.translate(x, y);
      g2.setColor(Color.black);
      g2.setStroke(new BasicStroke(stroke));
      g2.draw(s);
      g2.setColor(color);
      g2.fill(s);
      g2.translate(-1 * x, -1 * y);
   }
   
   // Randomly assigns spawn locations and loads all players into the PlayerList.
   public void loadPlayers() {
      Random rand = new Random();
      int size = tm.spawnLocations.size();
      int index = rand.nextInt(tm.spawnLocations.size());
      double startX = tm.spawnLocations.get(index).x;
      double startY = tm.spawnLocations.get(index).y;
      tm.spawnLocations.remove(index);

      switch(scufflerID) {
         case "COLT": 
            player = new Colt(this, ki, mi, startX, startY);   
            break;
         case "PRIMO": 
            player = new Primo(this, ki, mi, startX, startY);      
            break;
         case "PIPER":
            player = new Piper(this, ki, mi, startX, startY);      
            break;
         case "SPROUT":
            player = new Sprout(this, ki, mi, startX, startY);         
            break;
         case "LEON":
            player = new Leon(this, ki, mi, startX, startY);            
      }      

      playerList.add(player);
      layerList.add(player);
      for (int i = 1; i < size; i++) {
         index = rand.nextInt(tm.spawnLocations.size());
         startX = tm.spawnLocations.get(index).x;
         startY = tm.spawnLocations.get(index).y;
         tm.spawnLocations.remove(index);
         Scuffler bot = new Leon(this, startX, startY);         
         playerList.add(bot);
         layerList.add(bot); 
      }
   }
   
   // Sorts player list in order of y-position on screen.
   public void sortLayerList() {
      Collections.sort(layerList, 
         new Comparator<Scuffler>() {
            public int compare(Scuffler o1, Scuffler o2) {
               int compare;
               if (o1.posY > o2.posY) {
                  compare = 1;
               } else if (o1.posY < o2.posY) {
                  compare = -1;
               } else {
                  compare = 0;
               }
               if (o1.inAir && !o2.inAir) {
                  compare = 1;
               } else if (o2.inAir && !o1.inAir) {
                  compare = -1;
               }            
               return compare; 
            }
         });      
   }

   // Updates visibility of all Scufflers to the player based on proximity.      
   public void updateVisibilityToPlayer() {
      for (int i = 0; i < playerList.size(); i++) {
         if (playerList.get(i).isRevealed == false) {
            playerList.get(i).setHiddenVisibility(false);
         }
      }
      ArrayList<Integer> playerProximityIndexes = getScufflersInProximity(player);
      for (int i = 0; i < playerProximityIndexes.size(); i++) {
         playerList.get(playerProximityIndexes.get(i)).setHiddenVisibility(true);
      }   
   }   
      
   // Gets the index of the closest scuffler.
   public int getClosestScufflerIndex(Scuffler s1) {
      int index = 0;
      int closestIndex = -1;
      double initialDistance;
      double distance;
      Scuffler s2;
      
      if (playerList.size() == 1) {
         return closestIndex;
      }
      
      if (s1.playerIndex == 0) {
         index = 1;
      }
      
      s2 = playerList.get(index);
      distance = Math.sqrt(Math.pow(Math.abs(s1.posX - s2.posX), 2) + Math.pow(Math.abs(s1.posY - s2.posY), 2));      
      if (s2.visible || s2.visibleWhileHidden) {
         closestIndex = index;      
      }
   
      
      for (index++; index < playerList.size(); index++) {
         if (s1.playerIndex != index) {
            initialDistance = distance;
            s2 = playerList.get(index);
            if (s2.visible || s2.visibleWhileHidden) {
               distance = Math.sqrt(Math.pow(Math.abs(s1.posX - s2.posX), 2) + Math.pow(Math.abs(s1.posY - s2.posY), 2));
            }
            if (initialDistance > distance) {
               closestIndex = index;
            } else {
               distance = initialDistance;
            }
         }   
      }
      return closestIndex;
   }
   
   // Gets a list of indexes within the proximity of an entity.
   public ArrayList<Integer> getScufflersInProximity(Entity e) {
      ArrayList<Integer> proximityIndexes = new ArrayList<Integer>();
      int eCol = (int)Math.floor((e.posX + (e.sizeX / 2)) / tileSize);
      int eRow = (int)Math.floor((e.posY + (e.sizeY / 2)) / tileSize);
      for (int i = 0; i < playerList.size(); i++) {
         Scuffler s = playerList.get(i);
         int sCol = (int)Math.floor(s.posX / tileSize);
         int sRow = (int)Math.floor(s.posY / tileSize);      
         for (int col = 1; col <= 5; col++) {
            for (int row = 1; row <= 5; row++) {     
               if (!(row == 1 && col == 1) && !(row == 5 && col == 5) &&
                   !(row == 1 && col == 5) && !(row == 5 && col == 1)) {
               
                  if (sCol == col + eCol - 3 && sRow == row + eRow - 3) {
                     proximityIndexes.add(s.playerIndex);
                  }         
               }
                      
            }
         }
      }
      return proximityIndexes;   
   }   

   
   // Removes eliminated player from match, and updates all current player indexes.
   public void isEliminated(int pi) {
      if (pi == 0) {
         gameOver();
      }
      Scuffler s = playerList.get(pi);
      playerList.remove(pi);
      layerList.remove(layerList.indexOf(s));
      for (int i = 0; i < playerList.size(); i++) {
         playerList.get(i).setIndex(i);   
      }
      // Kill notification in chat
   }
   
   // Initiates game over sequence for player.
   public void gameOver() {
      System.out.println("Game Over!");
   }
   
 
   // Runs the game, updates and draws based on frames per second.
   public void run() {
      double drawInterval = 1000000000/FPS;
      double delta = 0;
      long lastTime = System.nanoTime();
      long currentTime;
      
      while (gameThread != null) {
         currentTime = System.nanoTime();
         delta += (currentTime - lastTime) / drawInterval;
         lastTime = currentTime;
         if (delta >= 1) {
            update();
            repaint();
            delta--;
         }
      } 
   }

   // Updates game elements. If game has not been started, updates lobby. Otherwise,
   // updates map and players in game.
   public void update() {
      //if (!gameStarted || !gameActive) {
      //   updateLobby();
      //} else {
         updateGame();
      //}
   }

   // Updates lobby elements based on key input for character selection.
   public void updateLobby() {
      if (!inputCooldown) {
         if (ki.rightPressed) {
            currentScuffler++;
         }
         if (ki.leftPressed) {
            currentScuffler --;
         }
         if (currentScuffler > numScufflers) {
            currentScuffler = 1;
         }
         if (currentScuffler <= 0) {
            currentScuffler = numScufflers;
         }
         inputCooldown = true;
      
         inputTimer.schedule(
            new TimerTask() {
               @Override
               public void run() {
                  inputCooldown = false;
               }
            }, 100);       
      }
   
   }

   // Updates all game elements, incorporating map generation and players.
   public void updateGame() {
     //if (gameStarted) {
     //    loadPlayers();
     //    gameStarted = false;
     //    gameActive = true;   
     // }
      for (int i = 0; i < playerList.size(); i++) {
         playerList.get(i).setIndex(i);
         playerList.get(i).update();
      }
   }

   // Draws all game elements. If game has not been started, draws lobby. Otherwise, 
   // draws map and players in game.
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;
      //if (!gameActive) {
      //   drawLobby(g2);
      //} else {
         drawGame(g2);
      //}
      g2.dispose();
      
   }

   // Draws lobby elements, incorporating Scuffler icons and currently selected Scufflers highlighted.
   public void drawLobby(Graphics2D g2) {
      g2.setColor(new Color(3, 90, 219));
      g2.fill(new Rectangle2D.Double(0, 0, screenWidth, screenHeight));
      Rectangle2D r = g2.getFontMetrics(f.deriveFont(50f)).getStringBounds("CHOOSE SCUFFLER", g2);
      double widthText = r.getWidth();   
      drawText(g2, (screenWidth / 2) - (widthText / 2), 100, "CHOOSE SCUFFLER", Color.WHITE, 50f, 5);
   
      double rectSize = 200;
      double distance = (screenWidth - (rectSize * numScufflers)) / (numScufflers + 1);
      for (int i = 1; i <= numScufflers; i++) {
         g2.setColor(Color.WHITE);
         if (i == currentScuffler) {
            g2.setColor(Color.YELLOW);            
         }
         g2.fill(new Rectangle2D.Double((distance * i) + ((i - 1) * rectSize), 200, rectSize, rectSize));
      
      }
   }
   
   // Draws game elements, incorporating map generation and players.
   public void drawGame(Graphics2D g2) {
      tm.draw(g2, 0);
      sortLayerList();
      updateVisibilityToPlayer();
      for (int i = 0; i < layerList.size(); i++) {
         layerList.get(i).draw(g2);
      }
      for (int i = 0; i < layerList.size(); i++) {   
         if (layerList.get(i).throwShot || layerList.get(i).pierceShot) {
            layerList.get(i).drawBullets(g2);
         }         
      } 
      player.drawAim(g2);
      player.drawSuperBar(g2);
   }

}