# scuffle-stars
GAME SUMMARY

Scuffle Stars is a single-player, quick top-down shooter game where ten players, the user and nine other programmed bots, face off in a battlefield to be the last one standing.

The game features various unique characters, called Scufflers, which can be selected by the player in a pre-game menu before starting a match. When a player starts a match, opponent Scufflers are randomized.

Each Scuffler has a unique base attack, health, and speed, and has a special ability called a Super. Each shot that they land on an opponent will charge their Super bar by a certain amount. When the Super bar is fully charged, it can be activated at any time separate from the base attack, resetting the charge back to zero.

The battlefield has walls to provide cover from enemy fire, as well as patches of bushes, that when inside will completely conceal the position of a player from opponents unless they are in close enough proximity to where the player is. A danger area that damage players when inside will gradually start closing in, acting as a zone to force players to get closer to each other as a certain amount of time progresses. 


GAME DETAILS

The player's health bar is located above each character on the screen.
When below max health, a player will gradually heal back after a short period of time. Healing will be cancelled if a player shoots or is hit. 
Each player has three ammunition slots, represented as three orange slots below the health bar, that will deplete and gradually reload over time when used.
The Super bar is located at the bottom-right of the screen, and will be charged as the player deals damage.


NOTICE

This code is a demo version of Scuffle Stars. Currently it only serves as a rudimentary simulation for each Scuffler, showcasing the basic features of the full game. It does not have any actual interactive capability at the moment. Many of the features described above have not yet been implemented, including map bounds, enemy bots, lobby and matchmaking, sprites and animations, and music and sound. 


PROGRAM INFO

This program is written in Java, and utilizes various features outside of basic functionality including swing graphics, key and mouse input, array lists, and timers.


GAME SETUP

1. Install OpenJDK: https://adoptium.net/.
2. Install jGRASP: https://www.jgrasp.org/.
3. Open the Main.java file with jGRASP.
4. To run code, press CTRL + R within the Main class in jGRSAP.
5. To change Scuffler, change the text within the scufflerID instance field located at the top of the Main class (line 9). 
The five IDs are COLT, PRIMO, PIPER, SPROUT, and LEON.


HOW TO PLAY

To move, use the WASD keys.
To automatically shoot toward the nearest target, press SPACE.
To aim a shot, hold down SHIFT. To shoot, press SPACE while aiming down.
When the super bar is fully active, hold down TAB to aim the super attack. To shoot, press SPACE while aiming down.
