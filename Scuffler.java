import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Line2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.AffineTransform; 
import java.awt.Font;
import java.awt.Shape;
import java.awt.RenderingHints;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

// Scuffler is an Entity containing all information and capabilities of a Scuffler.
// This encompasses player input, movement, healing, reloading, shooting, and other functionality.

public class Scuffler extends Entity {

   // Base Settings
   int playerIndex;
   String name;
   String scufflerID;   
   boolean isPlayer;
   
   // Movement Settings
   double speed;
   double speedUp = 0;
   double speedDown = 0;
   double speedLeft = 0;
   double speedRight = 0;
   
   double speedAngleUp = 0;
   double speedAngleDown = 0;
   double speedAngleLeft = 0;
   double speedAngleRight = 0;

   double timeAccel = 0.8;
   double timeRotate = 0.25;
   
   boolean up = false;
   boolean down = false;
   boolean left = false; 
   boolean right = false;


   // Health Settings
   int maxHealth;
   int health;
   int healRate;
   Timer healTimer;   
   long healCooldown = 30;
   long healDelay = 10;
   boolean isHealing = false;   
   boolean isShot = false;
   
   
   // Bullet Settings
   boolean coneShot = false;
   boolean lineShot = false;
   boolean throwShot = false;
   boolean pierceShot = false;
   double throwX;
   double throwY;
   double throwRange;
   boolean radiusShot = false;
   double radiusArea;
   
   ArrayList<Bullet> ammo = new ArrayList<Bullet>(); 
   int maxAmmo = 3;
   double numBullets = maxAmmo;       
   double range;
   double damage;  
   int damageDealt;
   double bulletSize;    
   double bulletSpeed;
   double attackWidth;  
   double attackArcAngle;
   Timer cooldownTimer = new Timer(); 
   long bulletCooldownTime;     
   boolean bulletCooldown = false;
   boolean bulletActive = false;
   boolean bulletShot = false;      

   double groupMax;
   double bulletGroupMax;
   int bulletGroupSize = 0;
   double bulletGroupAngle = -1;
   Timer ammoGroupTimer;
   boolean isShootingGroup = false;
   double bulletGroupDelay;
   
   // Reload Settings
   Timer reloadTimer;   
   long reloadCooldownTime = 2;         
   double reloadTime;
   boolean isReloading = false;


   // Invisibility Settings
   boolean visibleWhileHidden = false;   
   boolean isRevealed = false;
   Timer revealTimer;
   long revealTime = 1;

   // Air Settings
   boolean inAir = false;
   double timeInAir;
   double airRange;
   double heightScale;
   
   double speedX;
   double speedY;
   double initialX;
   double initialY;  
   double initialSizeX;
   double initialSizeY;
   
   
   // Super Settings
   boolean lineSuper = false;
   boolean throwSuper = false;
   boolean radiusSuper = false;
   boolean abilitySuper = false;
   boolean superBreaksWalls = false;
   double superRange;
   
   boolean isAimingSuper;
   boolean isShootingSuper = false;
   
   double superMeter = 0;
   double superHitsNeeded;
   boolean superActivated = false;
   boolean superActive = false;
       
   
   // Input Settings
   boolean inputEnabled = true;
   boolean isAiming = false;  
   boolean isShooting = false; 
 
   
   // UI Settings
   double barX;
   double barY;
   double barLength = 48;
   double barStroke = 2.6;      
   double barLengthSlot = (barLength - barStroke) / maxAmmo;
   double barArcSize = 2;   

   // Player Settings
   KeyInput ki;
   MouseInput mi;
   double mouseAngle;
   

   public Scuffler(GamePanel gp, double x, double y, double range, double superRange, double damage, double speed, int maxHealth, double bulletSize,
   double bulletSpeed, double bulletGroupMax, double bulletGroupDelay, double attackWidth, long bulletCooldownTime, double reloadTime, int superHitsNeeded, boolean isPlayer) {
      super(gp, x, y, gp.tileSize, gp.tileSize);
      this.range = range;
      this.superRange = superRange;
      this.damage = damage;
      this.speed = speed;
      this.maxHealth = maxHealth;
      health = maxHealth;
      healRate = (int)Math.round(0.13 * maxHealth);
      this.bulletSize = bulletSize;  
      this.bulletSpeed = bulletSpeed;
      this.bulletGroupMax = bulletGroupMax;
      this.bulletGroupDelay = bulletGroupDelay;
      this.attackWidth = attackWidth;
      attackArcAngle = Math.atan2((attackWidth / 2) * gp.tileSize, range);         
      this.bulletCooldownTime = bulletCooldownTime; 
      this.reloadTime = reloadTime;    
      this.superHitsNeeded = superHitsNeeded;
      this.isPlayer = isPlayer;
      if (isPlayer) {
         name = "Player";
      } else {
         name = "";
      }
   }
   
   // Sets player index in the game to given integer parameter.
   public void setIndex(int pi) {
      playerIndex = pi; 
   }   
   
   // Sets visibility while hidden to a given boolean parameter.
   public void setHiddenVisibility(boolean b) {
      visibleWhileHidden = b;
   }
   
   // Subtracts health from Scuffler based on damage dealt, and sets isShot to true.
   public void isShot(int damageDealt) {
      health -= damageDealt;
      isShot = true;
   }
   
   // Calculates the damage from a bullet. Varies between Scufflers.   
   public void calculateDamage(Bullet b) {
      damageDealt = (int)b.damage;
   }

   // Activates super ability of Scuffler. Varies between Scufflers.
   public void activateSuper() {
   
   }   
   
   // Updates super ability of Scuffler. Varies between Scufflers.
   public void updateSuper() {
   
   }

   // Updates all Scuffler values, including input, position, visibility, movement, health, reloading, and active super abilities.
   public void update() {
      if (superActive) {
         updateSuper();
      } 
      if (isPlayer) {
         if (inputEnabled) {
            updatePlayer();
         }
         updatePlayerValues();
      }
      if (inputEnabled) {
         updateInput();  
      }
   
      updateVisibility();
      updateHealth();
      updateReloading();
      if (inputEnabled) {
         updateMovement();
      }
      updateScreenPosition();
   
   } 
   
   // Updates player values based on key input, including direction, aiming, and shooting.
   public void updatePlayer() {
      cc.checkBushRange(this);
      up = ki.upPressed;
      down = ki.downPressed;
      left = ki.leftPressed;
      right = ki.rightPressed;   
      
   
      if (ki.shiftPressed) {
         isAiming = true;
      } else {
         isAiming = false;
      }
         
      if (abilitySuper && ki.tabPressed && superMeter == superHitsNeeded) {
         superActivated = true;
         ki.tabPressed = false;         
      }      
      
      
      if (lineSuper || throwSuper || radiusSuper) {
         if (ki.tabPressed && superMeter == superHitsNeeded) {
            isAimingSuper = true;
            isAiming = false;
         } else {
            isAimingSuper = false;
         }
         
         if (ki.tabPressed && ki.spacePressed && superMeter == superHitsNeeded) {
            superActivated = true;
            ki.tabPressed = false;
            ki.shiftPressed = false;
            ki.spacePressed = false;  
         }  
      }                
      
      if (numBullets >= 1 && ((ki.shiftPressed && ki.spacePressed && !bulletCooldown) || (ki.spacePressed && !isAiming && !bulletCooldown))) {
         isShooting = true;
         ki.shiftPressed = false;             
      }
   }
   
   // Updates center position and angle between mouse and center of player.
   public void updatePlayerValues() { 
      screenX = gp.screenWidth / 2 - sizeX / 2;
      screenY = gp.screenHeight / 2 - sizeY / 2;
      centerScreenX = screenX + (sizeX / 2);
      centerScreenY = screenY + (sizeY / 2);    
      double difX = mi.mouseX - centerScreenX;
      double difY = mi.mouseY - centerScreenY;
      mouseAngle = Math.PI / 2 - (Math.atan2(difX, difY) - Math.PI / 2);
   }
      
   // Updates action inputs from a player or bot.
   public void updateInput() {
      if (isShooting) {
         bulletShot = true;
         bulletActive = true;
         isShooting = false;   
      }   
            
      if (superActivated) {
         superMeter = 0;
         superActive = true;
         activateSuper();         
         superActivated = false;
         
      }
   
   }

   // Updates visibility of Scuffler. 
   public void updateVisibility() {
      if (inAir) {
         visible = true;
      } else {
         cc.checkBush(this);
      }
      
      if (!visible) {
         if (isShot == true && isRevealed == false) {
            visibleWhileHidden = true;
            revealTimer = new Timer();
            revealTimer.schedule(            
               new TimerTask() {
                  @Override
                  public void run() {
                     visibleWhileHidden = false;
                     isRevealed = false;
                  }
               }, revealTime * 1000);
            isRevealed = true;
         }
      }   
   }
  
   
   // Updates health values of Scuffler, gradually healing Scuffler's health, and registering whether the Scuffler is below or at zero health.
   public void updateHealth() {
      if (health <= 0) {
         gp.isEliminated(playerIndex);
      }
      
      if ((health < maxHealth || isShot) && isHealing == false) {
         isShot = false;
         healTimer = new Timer(); 
         healTimer.scheduleAtFixedRate(            
            new TimerTask() {
               @Override
               public void run() {
                  health += healRate;  
               }
            }, healCooldown * 100, healDelay * 100);
         isHealing = true;   
      }
      
      if (isHealing) {
         if (health >= maxHealth || isShot || bulletShot) {
            if (health >= maxHealth) {
               health = maxHealth;
            }
            if (isShot) { // Move down to red animation (?)
               isShot = false;
            }          
            isHealing = false;   
            healTimer.cancel();
         }            
      }
        
   }

   // Updates the Scuffler's reload cycle, gradually reloading ammunition.
   public void updateReloading() {
      if (numBullets < 3 && isReloading == false) {
         reloadTimer = new Timer(); 
         reloadTimer.scheduleAtFixedRate(            
            new TimerTask() {
               @Override
               public void run() {
                  numBullets += 1 / (reloadTime * gp.FPS); 
               }
            }, reloadCooldownTime * 100, (long)(1.0 / gp.FPS * 1000));
      
         isReloading = true;  
      }
      
      if (isReloading) {
         if (numBullets >= maxAmmo || bulletShot) {
            if (numBullets >= maxAmmo) {
               numBullets = maxAmmo;
            }                                        
            isReloading = false;                        
            reloadTimer.cancel();     
         }    
      }
         
   }

   // Updates movement of Scuffler. Accelerates speed and turns entity angle based on up, down, left and right inputs.
   public void updateMovement() {
      boolean collisionUp = cc.checkTileCollision(this, 0, -1 * speedUp);
      boolean collisionDown = cc.checkTileCollision(this, 0, speedDown);
      boolean collisionLeft = cc.checkTileCollision(this, -1 * speedLeft, 0);
      boolean collisionRight = cc.checkTileCollision(this, speedRight, 0);
      
      // Up movement
      if (up) {
         if (!collisionUp) {
            posY -= speedUp / gp.FPS;
            speedUp = accelerate(0, speedUp, Math.abs(Math.cos(entityAngle)) * speed, timeAccel);
         } else {
            speedUp = 0;
            collisionUp = false;
         }
         
         if (entityAngle <= Math.PI) {
            entityAngle = accelerate(entityAngle, entityAngle, 0, timeRotate);
         } else {
            entityAngle = accelerate(entityAngle - 2 * Math.PI, entityAngle - 2 * Math.PI, 0,  timeRotate) + 2 * Math.PI;
            
         }
         
      } else {  
         if (!collisionUp) {
            posY -= speedUp / gp.FPS;
            speedUp = accelerate(speed, speedUp, 0, timeAccel);
         } else {
            speedUp = 0;
            collisionUp = false;
         }     
      } 
      
      
      // Down movement
      if (down) {
         if (!collisionDown) {
            posY += speedDown / gp.FPS;
            speedDown = accelerate(0, speedDown, Math.abs(Math.cos(entityAngle)) * speed, timeAccel);
         } else {
            speedDown = 0;
            collisionDown = false;
         }
         
         entityAngle = accelerate(entityAngle, entityAngle, Math.PI, timeRotate);       
         
      } else { 
         if (!collisionDown) {
            posY += speedDown / gp.FPS;
            speedDown = accelerate(speed, speedDown, 0, timeAccel);  
         } else {
            speedDown = 0;
            collisionDown = false;
         }                
      }     
      
              
      // Left movement  
      if (left) {
         if (!collisionLeft) {
            posX -= speedLeft / gp.FPS;
            speedLeft = accelerate(0, speedLeft, Math.abs(Math.sin(entityAngle)) * speed, timeAccel);
         } else {
            speedLeft = 0;
            collisionLeft = false;
         }
         
         if (entityAngle >= Math.PI / 2) {
            entityAngle = accelerate(entityAngle, entityAngle, 3 * Math.PI / 2, timeRotate);    
         } else {
            entityAngle = accelerate(entityAngle + 2 * Math.PI, entityAngle + 2 * Math.PI, 3 * Math.PI / 2, timeRotate);                    
         }         
      } else {
         if (!collisionLeft) {    
            posX -= speedLeft / gp.FPS;
            speedLeft = accelerate(speed, speedLeft, 0, timeAccel);  
         } else {
            speedLeft = 0;
            collisionLeft = false;
         }    
      } 
      
      
         // Right movement
      if (right) {
         if (!collisionRight) {
            posX += speedRight / gp.FPS;
            speedRight = accelerate(0, speedRight, Math.abs(Math.sin(entityAngle)) * speed, timeAccel);
         } else {
            speedRight = 0;
            collisionRight = false;
         }
         
         if (entityAngle <= 3 * Math.PI / 2) {
            entityAngle = accelerate(entityAngle, entityAngle, Math.PI / 2, timeRotate);        
         } else {
            entityAngle = accelerate(entityAngle - 2 * Math.PI, entityAngle - 2 * Math.PI, Math.PI / 2, timeRotate);                    
         }          
      } else { 
         if (!collisionRight) {   
            posX += speedRight / gp.FPS;
            speedRight = accelerate(speed, speedRight, 0, timeAccel);  
         } else {
            speedRight = 0;
            collisionRight = false;
         }    
      }       
   }
   
   // Updates screen position of Scuffler relative to the player.
   public void updateScreenPosition() {
      screenX = posX - gp.player.posX + gp.player.screenX;
      screenY = posY - gp.player.posY + gp.player.screenY; 
      centerScreenX = screenX + (sizeX / 2);
      centerScreenY = screenY + (sizeY / 2);
   }   



   // Draws Scuffler and all character elements, including the character, bars and bullets.
   public void draw(Graphics2D g2) {
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      if (!throwShot && !pierceShot) {
         drawBullets(g2);
      }    
      drawScuffler(g2);
      if (visible || isPlayer || !isPlayer && visibleWhileHidden == true) {
         drawBars(g2);
      }      
   }
   
   // Draws the Scuffler onto the screen. 
   public void drawScuffler(Graphics2D g2) {
      AffineTransform transform = g2.getTransform();
      int alpha;  
      if (visible) {
         alpha = 255;
      } else if (isPlayer || !isPlayer && visibleWhileHidden == true) {
         alpha = 90;
      } else {
         alpha = 0;
      }
      
      if (isPlayer) {
         if (this instanceof Leon) {
            g2.setColor(new Color(18, 154, 65, alpha));
         } else if (this instanceof Sprout) {
            g2.setColor(new Color(195, 240, 254, alpha));
         } else if (this instanceof Piper) {
            g2.setColor(new Color(43, 214, 254, alpha));
         } else if (this instanceof Colt) {
            g2.setColor(new Color(131, 163, 246, alpha));
         } else if (this instanceof Primo) {
            g2.setColor(new Color(28, 102, 207, alpha));
         }
      } else {
         g2.setColor(new Color(197, 57, 68, alpha)); 
      }
      g2.rotate(entityAngle, centerScreenX, centerScreenY);       
      g2.fill(new Ellipse2D.Double(screenX, screenY, sizeX, sizeY));
      g2.setTransform(transform);    
   }
   
   // Draws health bar, and draws ammo bars and name if player.
   public void drawBars(Graphics2D g2) {
      barX = screenX - (barLength - sizeX) / 2;
      barY = screenY - 13.5;
      
      // Draw health bar
      g2.setColor(new Color(30, 40, 62));
      g2.fill(new RoundRectangle2D.Double(barX - 1 - barStroke / 2, barY - 10 - barStroke / 2, barLength + 2 + barStroke, 8 + barStroke, 10, 10)); 
   
      if (isPlayer) {
         g2.setColor(new Color(75, 225, 56));
      } else {
         g2.setColor(new Color(218, 28, 38));
      }
      g2.fill(new RoundRectangle2D.Double(barX - 1, barY - 10, ((double)health / maxHealth) * (barLength + 2), 8, 10, 10));    
            
      // Draw ammo bars
      if (isPlayer) {  
         g2.setColor(new Color(30, 40, 62));          
         g2.fill(new RoundRectangle2D.Double(barX - barStroke / 2, barY - barStroke / 2, barLength + barStroke, 4 + barStroke, barArcSize, barArcSize));  
      
         for (int i = 0; i < maxAmmo; i++) {
            if (numBullets >= i + 1) {
               g2.setColor(new Color(226, 103, 31));
               g2.fill(new RoundRectangle2D.Double(barX + i * (barLengthSlot + barStroke / 2), barY, barLengthSlot, 4, barArcSize, barArcSize));            
            } else {
               g2.setColor(new Color(110, 142, 184));
               g2.fill(new RoundRectangle2D.Double(barX + i * (barLengthSlot + barStroke / 2), barY, barLengthSlot * (numBullets - i), 4, barArcSize, barArcSize));
            }
         }            
      }
          
      // Draw red bar
      if (isShot) {
      
      }
      
      // Draw text         
      Rectangle2D r1 = g2.getFontMetrics(gp.f.deriveFont(10f)).getStringBounds(health + "", g2);
      double widthHealth = r1.getWidth();
      gp.drawText(g2, barX + (barLength / 2) - (widthHealth / 2.0), screenY - 21, health + "", Color.white, 10f, 2);
      
      Rectangle2D r2 = g2.getFontMetrics(gp.f.deriveFont(13f)).getStringBounds(name, g2);
   
      double widthName = r2.getWidth();      
      
      Color nameColor;
      if (isPlayer) {
         nameColor = new Color(75, 225, 56);
      } else {
         nameColor = new Color(238, 182, 194);
      }
      gp.drawText(g2, barX + (barLength / 2) - (widthName / 2.0), screenY - 32, name, nameColor, 13f, 2);
   
   }
   
   // Draws the aim bar for player when aiming, varying based on the unique shooting properties of the Scuffler.
   public void drawAim(Graphics2D g2) {   
      if (isAiming || isAimingSuper) { 
         AffineTransform transform = g2.getTransform(); // Get default transformation settings
         g2.rotate(mouseAngle - Math.PI / 2, centerScreenX, centerScreenY); 
         
         if (coneShot) {
            if (numBullets >= 1) {
               g2.setColor(new Color(255, 255, 255, 175));   
            } else {
               g2.setColor(new Color(255, 0, 0, 175));        
            }                             
            g2.draw(new Arc2D.Double(centerScreenX - (range), centerScreenY - (range), range * 2, range * 2, Math.toDegrees(attackArcAngle), -2 * Math.toDegrees(attackArcAngle), Arc2D.PIE)); 
         }  
         if ((lineShot && isAiming) || (lineSuper && isAimingSuper)) {
            double lineLength = 0;
            if (isAimingSuper && lineSuper && ki.tabPressed && superMeter == superHitsNeeded) {
               g2.setColor(new Color(248, 255, 133, 175));   
               lineLength = cc.findCollisionDistance(posX + (sizeX / 2), posY + (sizeY / 2), mouseAngle - Math.PI / 2, superRange);  
               if (superBreaksWalls) {
                  lineLength = range;
               }        
            } else if (isAiming && lineShot && numBullets < 1) {
               g2.setColor(new Color(255, 0, 0, 175));   
               lineLength = cc.findCollisionDistance(posX + (sizeX / 2), posY + (sizeY / 2), mouseAngle - Math.PI / 2, range);                  
            } else if (isAiming && lineShot && ki.shiftPressed) {
               g2.setColor(new Color(255, 255, 255, 175)); 
               lineLength = cc.findCollisionDistance(posX + (sizeX / 2), posY + (sizeY / 2), mouseAngle - Math.PI / 2, range);             
            }
            
            g2.fill(new Rectangle2D.Double(centerScreenX, centerScreenY - bulletSize / 2, lineLength, bulletSize));
         }  
                
         if ((throwShot && isAiming) || (throwSuper && isAimingSuper) || (radiusShot && isAiming) || (radiusSuper || isAimingSuper)) {
            double mouseDistanceX = mi.mouseX - centerScreenX;
            double mouseDistanceY = mi.mouseY - centerScreenY;
            throwRange = Math.sqrt(Math.pow(mouseDistanceX, 2) + Math.pow(mouseDistanceY, 2));
            
            double bulletRange;
            if (isAimingSuper) {
               bulletRange = superRange;
            } else {
               bulletRange = range;
            }
            
            if (throwRange > bulletRange) {
               mouseDistanceX = Math.cos(mouseAngle - Math.PI / 2) * bulletRange;
               mouseDistanceY = Math.sin(mouseAngle - Math.PI / 2) * bulletRange;
               throwRange = bulletRange;
            } 
            if (throwRange < ((sizeX / 2) + 15)) {
               mouseDistanceX = Math.cos(mouseAngle - Math.PI / 2) * ((sizeX / 2) + 15);
               mouseDistanceY = Math.sin(mouseAngle - Math.PI / 2) * ((sizeX / 2) + 15);
               throwRange = ((sizeX / 2) + 15);
            }           
            throwX = posX + sizeX / 2 + mouseDistanceX;
            throwY = posY + sizeY / 2 + mouseDistanceY;
            double throwScreenX = centerScreenX + mouseDistanceX;
            double throwScreenY = centerScreenY + mouseDistanceY;               
         
            if (isAimingSuper && (throwSuper || radiusSuper) && ki.tabPressed && superMeter == superHitsNeeded) {
               g2.setColor(new Color(248, 255, 133, 175));           
            } else if (isAiming && throwShot && numBullets < 1) {
               g2.setColor(new Color(255, 0, 0, 175));     
            } else if (isAiming && throwShot && ki.shiftPressed) {
               g2.setColor(new Color(255, 255, 255, 175));           
            }
         
            if ((throwShot && isAiming) || (throwSuper && isAimingSuper)) {
               g2.fill(new Rectangle2D.Double(centerScreenX, centerScreenY - 2.5, throwRange, 5));                        
               g2.setTransform(transform); 
               g2.setStroke(new BasicStroke(5));             
               g2.draw(new Line2D.Double(throwScreenX - 9, throwScreenY - 9, throwScreenX + 9, throwScreenY + 9));
               g2.draw(new Line2D.Double(throwScreenX + 9, throwScreenY - 9, throwScreenX - 9, throwScreenY + 9));
            } else if ((radiusShot && isAiming) || (radiusSuper && isAimingSuper)) {
               g2.fill(new Rectangle2D.Double(centerScreenX, centerScreenY - 2.5, throwRange, 5));                        
               g2.setTransform(transform); 
               g2.draw(new Ellipse2D.Double(throwScreenX - radiusArea / 2, throwScreenY - radiusArea / 2, radiusArea, radiusArea));     
            }
         }
      
      
         g2.setTransform(transform);            
      }     
   }
    
         
   // Draws the super charge meter for the player, at the bottom-right section of the screen.
   public void drawSuperBar(Graphics2D g2) {
      double x = 1050;
      double y = 500;
      double size = 90;
      if (superMeter < superHitsNeeded) {
         g2.setColor(new Color(30, 40, 62));
         g2.fill(new Ellipse2D.Double(x, y, size, size)); 
         g2.setColor(new Color(50, 102, 150)); 
         g2.fill(new Ellipse2D.Double(x + size / 8, y + size / 8, size - size / 4, size - size / 4));          
         g2.setColor(new Color(248, 194, 0));
         g2.setStroke(new BasicStroke(6));
         g2.draw(new Arc2D.Double(x + size / 16, y + size / 16, size - size / 8, size - size / 8, -90, (-360 / superHitsNeeded) * superMeter, Arc2D.OPEN)); 
      } else {
         g2.setColor(new Color(141, 30, 25));
         g2.fill(new Ellipse2D.Double(x, y, size, size));
         g2.setColor(new Color(252, 192, 24));         
         g2.fill(new Ellipse2D.Double(x + size / 32, y + size / 32, size - size / 16, size - size / 16));
            
      }              
   }    
   
   // Shoots a single group of bullets from the Scuffler. Creates bullet, either with default properties or super properties if super ability is active.
   public void shootBullet(double groupMax, boolean superBullet, double groupDelay) {
      this.groupMax = groupMax;
      if (bulletActive || superActive) {
         if (bulletShot == true && (bulletCooldown == false || superActive == true) && isShootingGroup == false && ammo.size() <= 4 && (numBullets >= 1 || superActive)) {
            if (bulletActive) {
               numBullets--;             
            }
            isShootingGroup = true;
            ammoGroupTimer = new Timer();
            ammoGroupTimer.scheduleAtFixedRate(            
               new TimerTask() {
                  @Override
                  public void run() {
                     bulletGroupSize++;
                     if (bulletGroupSize == 1) {
                        getBulletAngle();
                     }
                     if (!superBullet) {                    
                        ammo.add(new Bullet(bulletGroupSize, false));
                     } else {
                        ammo.add(new Bullet(bulletGroupSize, true));
                     }
                  }
               }, 0, (long)groupDelay);
         }
      }       
   }
    
   // Creates new bullets if shot, updates positions and draws onto screen.
   public void drawBullets(Graphics2D g2) {
      if (bulletActive) {
         shootBullet(bulletGroupMax, false, bulletGroupDelay);        
      }
      for (int i = 1; i <= ammo.size(); i++) { // Checks each bullet for collision, draws bullets if false
         Bullet b = ammo.get(i - 1);
         b = updateBulletPosition(b);
         if (b == null) {
            ammo.remove(i - 1);
            i--;
         } else {           
            double screenXB = b.posX - gp.player.posX + gp.player.screenX;
            double screenYB = b.posY - gp.player.posY + gp.player.screenY;
            b.draw(g2, screenXB, screenYB);
            ammo.set(i - 1, b);
         }   
      }
               
      if (isShootingGroup) {
         if (bulletGroupSize >= groupMax) {
            ammoGroupTimer.cancel();
            bulletGroupAngle = -1;
            bulletGroupSize = 0;               
            isShootingGroup = false;              
         } 
      }         
      
      if (ammo.size() == 0) {
         bulletActive = false;
      }                
   }   

   // Gets bullet angle for aiming, for auto-aim, and for bots.
   public void getBulletAngle() {
      if ((isAiming || isAimingSuper) && isPlayer) {
         bulletGroupAngle = gp.player.mouseAngle - Math.PI / 2;
         if (throwShot || radiusShot || throwSuper || radiusSuper) {
            if (cc.getTileCollision(throwX, throwY)) {
               Point2D p2 = cc.getUnmergePosition(throwX, throwY);
            
               throwX = p2.getX() + (gp.tileSize / 2) - (bulletSize / 2);
               throwY = p2.getY() + (gp.tileSize / 2) - (bulletSize / 2);              
               bulletGroupAngle = Math.atan2(throwY - (posY + sizeY / 2), throwX - (posX + sizeX / 2));
            }
         }
         isAiming = false;
         isAimingSuper = false; 
      } else if (!isPlayer && bulletGroupAngle == -1) {
         // Bot aiming
      } else if (gp.playerList.size() > 1 && bulletGroupAngle == -1) {      
         int closestIndex = gp.getClosestScufflerIndex(this);
         if (closestIndex == -1) {
            bulletGroupAngle = 0;         
         } else { 
            Scuffler s = gp.playerList.get(closestIndex);
            double difX = s.posX - this.posX;
            double difY = s.posY - this.posY;
            bulletGroupAngle = Math.PI / 2 - (Math.atan2(difX, difY));
            if (throwShot || throwSuper || radiusShot || radiusSuper) {
               throwRange = Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2));
               double bulletRange;
               if (isAimingSuper) {
                  bulletRange = superRange;
               } else {
                  bulletRange = range;
               }
               
                  
               if (throwRange > bulletRange) {
                  difX = Math.cos(bulletGroupAngle) * bulletRange;
                  difY = Math.sin(bulletGroupAngle) * bulletRange;
                  throwRange = bulletRange;
               }            
               if (throwRange < ((sizeX / 2) + 15)) {
                  difX = Math.cos(bulletGroupAngle) * ((sizeX / 2) + 15);
                  difY = Math.sin(bulletGroupAngle) * ((sizeX / 2) + 15);
                  throwRange = ((sizeX / 2) + 15);
               }                 
               throwX = posX + sizeX / 2 + difX;
               throwY = posX + sizeX / 2 + difY;
            } 
         }
      } else if (bulletGroupAngle == -1) {
         bulletGroupAngle = 0;
      }     
   }
   

   // Creates new bullets if shot, updates all other current bullet positions. Returns null if impacts with solid object.
   public Bullet updateBulletPosition(Bullet b) {
      if (b.initialized == false) {
         int groupNum = b.groupNum;
         boolean superBullet = b.superBullet; 
         boolean destroysWalls = b.destroysWalls;
         bulletShot = false;
         bulletCooldown = true;
       
         b = new Bullet(gp, this.posX + sizeX / 2 - bulletSize / 2, this.posY + sizeY / 2 - bulletSize / 2, bulletSize, bulletSize, bulletSpeed, new Color(61, 130, 255));
         b.groupNum = groupNum;
         b.superBullet = superBullet;
         cooldownTimer.schedule(
            new TimerTask() {
               @Override
               public void run() {
                  bulletCooldown = false;
               }
            }, bulletCooldownTime * 100);           
         
         while (groupNum > bulletGroupMax) {
            groupNum -= bulletGroupMax;
         }
         
         if (coneShot) { 
            if (groupNum < (bulletGroupMax + 1) / 2) {
               b.bulletAngle = bulletGroupAngle - attackArcAngle / groupNum;
            } else if (groupNum > (bulletGroupMax + 1) / 2) {      
               b.bulletAngle = bulletGroupAngle + attackArcAngle / (bulletGroupMax + 1 - groupNum);            
            } else {
               b.bulletAngle = bulletGroupAngle;           
            }
         } else {
            b.bulletAngle = bulletGroupAngle;
         }
         
         if (!b.superBullet) {
            b = setDefaultBulletValues(b);           
            b.damage = damage;
         } else {
            b = setSuperBulletValues(b);
         }
         if (throwShot || radiusShot) {
            b.throwScale = 100; 
            b.throwRange = throwRange; 
            b.speedX = ((Math.cos(b.bulletAngle) * b.throwRange) / b.timeInAir) / gp.FPS;
            b.speedY = ((Math.sin(b.bulletAngle) * b.throwRange) / b.timeInAir) / gp.FPS;                 
         } else { 
            b.speedX = Math.cos(b.bulletAngle) * b.speed / gp.FPS;
            b.speedY = Math.sin(b.bulletAngle) * b.speed / gp.FPS;
         }         
         b.initialX = this.posX + sizeX / 2 - bulletSize / 2;
         b.initialY = this.posY + sizeY / 2 - bulletSize / 2;  
      }  
      
      if (lineSuper && b.superBullet) {
         b = updateSuperStraightBullet(b);
      } else if (throwSuper && b.superBullet || radiusSuper && b.superBullet) {
         b = updateSuperThrowBullet(b);
      } else if (lineShot || coneShot) {
         b = updateStraightBullet(b);         
      } else if (throwShot || radiusShot) {
         b = updateThrowBullet(b);
      }
      
      if (b == null) {
         return b;
      }
      
      if (b.timerActivated) {
         if (b.superBullet) {
            b = updateSuperTimer(b);
         } else {
            b = updateDefaultTimer(b);
         }
      }
        
      return b;
   }   
   
   // Checks for bullet entity collision, if found will apply damage and nullify bullet.
   public Bullet getBulletEntityCollision(Bullet b) {
      for (int i = 0; i < gp.playerList.size(); i++) {
         boolean entityCollision;
         if (i != playerIndex && !gp.playerList.get(i).inAir) {
            Scuffler s = gp.playerList.get(i);
            entityCollision = cc.checkEntityCollision(s, b, s.speedRight - s.speedLeft, s.speedUp - s.speedDown, b.speedX, b.speedY);
         } else {
            entityCollision = false;
         }
         
         if (entityCollision) {
            b.impact();
            if (!b.pierceIndexes.contains(i)) {
               getDamageMethod(b, i);
            }
            if (!b.pierce) {
               b = null;
            } else {
               b.pierceIndexes.add(i);
            }
            return b;   
         }
      }
      return b;
   }
   
   // Chooses a method to calculate bullet damage whether or not there is an explosion radius property.
   public void getDamageMethod(Bullet b, int index) {
      if (b.explosionRadius != -1) {
         checkExplodeCollision(b);
      } else {
         calculateDamage(b);
                     
         if (superMeter < superHitsNeeded) {
            superMeter += damageDealt / damage;
         }
         if (superMeter > superHitsNeeded) {
            superMeter = superHitsNeeded;
         }
         gp.playerList.get(index).isShot(damageDealt);         
      }
         
   
   }

   // Checks for players in range of bullet explosion, if found will apply damage.
   public void checkExplodeCollision(Bullet b) {
      b.expandRange(b.explosionRadius);
      b.setPosition(b.posX - b.explosionRadius / 2, b.posY - b.explosionRadius / 2);
      for (int i = 0; i < gp.playerList.size(); i++) {
         boolean entityCollision;
         if (i != playerIndex && !gp.playerList.get(i).inAir) {
            Scuffler s = gp.playerList.get(i);
            entityCollision = cc.checkEntityCollision(s, b, s.speedRight - s.speedLeft, s.speedUp - s.speedDown, b.speedX, b.speedY);
         } else {
            entityCollision = false;
         }
         
         if (entityCollision) {
            calculateDamage(b);
                     
            if (superMeter < superHitsNeeded) {
               superMeter += damageDealt / damage;
            }
            if (superMeter > superHitsNeeded) {
               superMeter = superHitsNeeded;
            }
            gp.playerList.get(i).isShot(damageDealt);   
         }
      }
      
      if (b.destroysWalls) {
         cc.destroyTiles(b);  
      }
   
      
   }

   
   // Sets default bullet values for specific Scuffler. Varies between Scufflers.
   public Bullet setDefaultBulletValues(Bullet b) {
      return b;
   }
   
   // Sets super bullet values for specific Scuffler. Varies between Scufflers.
   public Bullet setSuperBulletValues(Bullet b) {
      return b;   
   }

   // Activates time-based update for default bullets. Varies between Scufflers.
   public Bullet updateDefaultTimer(Bullet b) {
      return b;
   }
   
   // Activates time-based update for Super bullets. Varies between Scufflers.
   public Bullet updateSuperTimer(Bullet b) {
      return b;
   }

   // Updates behavior of default straight bullets.
   public Bullet updateStraightBullet(Bullet b) {
      b = getBulletEntityCollision(b);
      if (b == null) {      
         return b;
      }
      
      boolean rangeX; 
      if (b.speedX > 0) {
         rangeX = (b.posX - b.initialX) < (Math.cos(b.bulletAngle) * this.range);
      } else if (b.speedX < 0) {
         rangeX = (b.posX - b.initialX) > (Math.cos(b.bulletAngle) * this.range);      
      } else {
         rangeX = true;
      }
      
      boolean rangeY;
      if (b.speedY > 0) {
         rangeY = (b.posY - b.initialY) < (Math.sin(b.bulletAngle) * this.range);
      } else if (b.speedY < 0) {
         rangeY = (b.posY - b.initialY) > (Math.sin(b.bulletAngle) * this.range);
      } else {
         rangeY = true;
      }
            
      b.collisionX = cc.checkTileCollision(b, b.speedX, 0);
      b.collisionY = cc.checkTileCollision(b, 0, b.speedY);
      
      if (b.bounce) {
         b = updateBounceStraightBullet(b);
         if (b == null) {      
            return b;
         }
      }
      if (b.destroysWalls) {
         cc.destroyTiles(b);   
      }         
      
      if (!b.collisionX && rangeX) {
         b.posX += b.speedX;
      } else if (b.bounce) {
         b.posY -= b.speedX;
         b.collisionX = true;
         b.bulletBounced = true;
         b.numBounces++;   
      } else {
         b.impact();
         if (b.explosionRadius != -1) {
            checkExplodeCollision(b);
         }                     
         b = null;     
         return b; 
      }
      
      if (!b.collisionY && rangeY) {   
         b.posY += b.speedY;
      } else if (b.bounce) {
         b.posY -= b.speedY;
         b.speedY *= -1;
      } else {
         b.impact();
         if (b.explosionRadius != -1) {
            checkExplodeCollision(b);
         }                  
         b = null;
      
         return b;
      }
      
      return b;  
   }

   // Updates bouncing behavior of straight bullets.
   public Bullet updateBounceStraightBullet (Bullet b) {
      if (b.numBounces > 3) {
         b.impact();
         if (b.explosionRadius != -1) {
            checkExplodeCollision(b);
         }
         b = null;
         return b;
      }   
   
      if (b.bulletBounced) {
         b.bulletBounced = false;
         if (b.collisionX) {
            b.speedX = -1 * ((Math.cos(b.bulletAngle) * b.throwRange) / b.timeInAir) / gp.FPS;
            b.speedY = ((Math.sin(b.bulletAngle) * b.throwRange) / b.timeInAir) / gp.FPS;
            b.bulletAngle = Math.atan2(b.speedX, b.speedY);
            b.collisionX = false;
         } else if (b.collisionY) {
            b.speedX = ((Math.cos(b.bulletAngle) * b.throwRange) / b.timeInAir) / gp.FPS;
            b.speedY = -1 * ((Math.sin(b.bulletAngle) * b.throwRange) / b.timeInAir) / gp.FPS;
            b.bulletAngle = Math.atan2(b.speedX, b.speedY);                             
            b.collisionY = false;
         } else {
            b.speedX = ((Math.cos(b.bulletAngle) * b.throwRange) / b.timeInAir) / gp.FPS;
            b.speedY = ((Math.sin(b.bulletAngle) * b.throwRange) / b.timeInAir) / gp.FPS;   
         }
         
         b.bulletBouncing = true;
         
      }  
      
      return b; 
   }
   
   // Updates super behavior of straight bullets. Varies between Scufflers.
   public Bullet updateSuperStraightBullet(Bullet b) {
      return b;
   }

   // Updates behavior of default thrown bullets.
   public Bullet updateThrowBullet(Bullet b) {
      b.collisionX = false;
      b.collisionY = false;
      
      double bulletRange = Math.sqrt(Math.pow(b.posX - b.initialX, 2) + Math.pow(b.posY - b.initialY, 2));
      
      if (b.bulletInAir) {  
         if (bulletRange > b.throwRange) {            
            b.bulletInAir = false;
            b.bulletBounced = true;
            b.numBounces++;
         }
      }
      
      if (b.bulletInAir) { 
         b.sizeX += b.throwScale / gp.FPS;
         b.sizeY += b.throwScale / gp.FPS;
         b.throwScale = accelerate(100, b.throwScale, -100, b.timeInAir);                
      }      
      
      if (!b.bulletInAir) {
         b = getBulletEntityCollision(b);
         if (b == null) {      
            return b;
         }  
         b.collisionX = cc.checkTileCollision(b, b.speedX, 0);
         b.collisionY = cc.checkTileCollision(b, 0, b.speedY);    
      }
      
      if (b.bounce) {
         b = updateBounceThrowBullet(b);
         if (b == null) {      
            return b;
         }   
      } 
      
      if (!b.bounce && !b.bulletInAir) {
         b.impact();
         if (b.explosionRadius != -1) {
            checkExplodeCollision(b);
         }      
         b = null;
         return b;
      }
            
      if (!b.collisionX) {
         b.posX += b.speedX;
      } else if (b.bounce && !cc.checkSelectTileCollision(b, b.posX + b.sizeX / 2, b.posY + b.sizeY / 2)) {
         b.posX -= b.speedX;
         b.bulletBounced = true;
         b.numBounces++;
      } else if (b.bounce) {
         b.posX += b.speedX;      
      } else {
         b.impact();
         if (b.explosionRadius != -1) {
            checkExplodeCollision(b);
         }
         b = null;
         return b;       
      }
      
      if (!b.collisionY) {   
         b.posY += b.speedY;
      } else if (b.bounce && !cc.checkSelectTileCollision(b, b.posX + b.sizeX / 2, b.posY + b.sizeY / 2)) {
         b.posY -= b.speedY;
         b.bulletBounced = true;
         b.numBounces++;         
      } else if (b.bounce) {
         b.posY += b.speedY;      
      } else {
         b.impact();
         if (b.explosionRadius != -1) {
            checkExplodeCollision(b);
         }
         b = null;
         return b;       
      }
      
      
      return b;            
             
                 
   }

   // Updates bouncing behavior of thrown bullets.
   public Bullet updateBounceThrowBullet(Bullet b) {
      if (b.numBounces > 3) {
         b.impact();
         if (b.explosionRadius != -1) {
            checkExplodeCollision(b);
         }
         b = null;
         return b;
      }   
   
      if (b.bulletBounced) {
         b.bulletBounced = false;
         b.throwScale = 30;
         b.throwRange = 3 * gp.tileSize;
         b.timeInAir = 0.5;
         if (b.collisionX) {
            b.speedX = -1 * ((Math.cos(b.bulletAngle) * b.throwRange) / b.timeInAir) / gp.FPS;
            b.speedY = ((Math.sin(b.bulletAngle) * b.throwRange) / b.timeInAir) / gp.FPS;
            b.bulletAngle = Math.atan2(b.speedX, b.speedY);
            b.collisionX = false;
         } else if (b.collisionY) {
            b.speedX = ((Math.cos(b.bulletAngle) * b.throwRange) / b.timeInAir) / gp.FPS;
            b.speedY = -1 * ((Math.sin(b.bulletAngle) * b.throwRange) / b.timeInAir) / gp.FPS;
            b.bulletAngle = Math.atan2(b.speedX, b.speedY);                             
            b.collisionY = false;
         } else {
            b.speedX = ((Math.cos(b.bulletAngle) * b.throwRange) / b.timeInAir) / gp.FPS;
            b.speedY = ((Math.sin(b.bulletAngle) * b.throwRange) / b.timeInAir) / gp.FPS;   
         }
         
         b.bulletBouncing = true;
         
      }
   
      if (b.bulletBouncing) {
         b.sizeX += b.throwScale / gp.FPS;
         b.sizeY += b.throwScale / gp.FPS;
         b.throwScale = accelerate(30, b.throwScale, -30, b.timeInAir);
         if (b.sizeX <= bulletSize) {
            b.bulletBouncing = false;
         }           
      }      
      
      if (!b.bulletBouncing && !b.bulletInAir) {
         b.impact();
         if (b.explosionRadius != -1) {
            checkExplodeCollision(b);
         }
         b = null;
         return b; 
      } else {
         return b;
      }     
   }
   
   
   
   // Updates super behavior of thrown bullets. Varies between Scufflers.
   public Bullet updateSuperThrowBullet (Bullet b) {
      return b;
   }

}