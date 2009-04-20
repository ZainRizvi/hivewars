package hivewars;

import hivewars.GameSettings.Control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;



public class GameStateController {
	
	private GameStateData gameState;
	private final Semaphore GSFreeForReading = new Semaphore(1, true);
	private final Semaphore GSFreeForWriting = new Semaphore(1, true);

	public GameStateController() {
		gameState = new GameStateData();
	}
	
	public GameStateData readGameState(){
		GameStateData gsd = new GameStateData(gameState);
		return gsd;
	}
	
	public void updateGameState(GameStateData newGameState){
		gameState = newGameState;
	}
	
	public void addAttack(Attack attack){	
		Hive hive = gameState.hives.get(attack.sourceHiveNum);
		gameState.subtractThisAttack = null; 	//be careful not to tell the enemy they have fired illegally unless 
												//they really have
		// BIG CHANGE HERE
		if (attack.hitTime <= this.gameState.gameStateNum){
			//Do nothing
		}else{ //if the attack has not hit yet	
			//add the attack if it is my attack and I have at least two minions or
			//it is the enemy's attack
			if(attack.player != GameController.Me || hive.numMinions>1){
				//check to see if the attack already exists in the arraylist of attacks
				boolean attackExists = false;
				for(int i = 0; i < gameState.attacks.size(); i ++){
					Attack existingattack = gameState.attacks.get(i);				
					if (attack.destHiveNum == existingattack.destHiveNum
							&& attack.sourceHiveNum == existingattack.sourceHiveNum
							&& attack.firingTime == existingattack.firingTime){
						attackExists = true;
						break;
					}				
				}
				
				//if it does not already exist then
				//subtract one minion from the source hive and
				//add the attack to arraylist of attacks
				//*no check to keep the enemy's numMinions from going negative
				if (attackExists == false){
					hive.numMinions--;	
					if(attack.player == GameController.Me){
						if(hive.numMinions == 0){
							hive.numMinions = 1;
						} else {
							gameState.attacks.add(attack);
						}
			    		//if (!this.equals(GameController.MasterGS)) System.out.println("gs: " +
			    			//	gameState.gameStateNum + "source,numMin: " + attack.sourceHiveNum +
			    			//	"," + hive.numMinions + " dest: " + attack.destHiveNum + " fireTime: " +
			    			//	attack.firingTime + " hitTime " + attack.hitTime + " attacks.size(): " +
			    			//	gameState.attacks.size());
					} else { //if the attack belongs to the enemy
						gameState.attacks.add(attack);
						//if (!this.equals(GameController.MasterGS)) System.out.println("gs: " +
							//	gameState.gameStateNum + "source,numMin: " + attack.sourceHiveNum + "," +
							//	hive.numMinions + " dest: " + attack.destHiveNum + " fireTime: " +
							//	attack.firingTime + " hitTime " + attack.hitTime + " attacks.size(): " +
							//	gameState.attacks.size());
					}
				}
			}
		}
	}
	
	public void fastForward(int finalStateNum){		
		// Assumes that the gameState's gameStateNum has not been advanced
		
		if (gameState.gameStateNum >= finalStateNum){
			// Do nothing
		}
		else{
			while (gameState.gameStateNum < finalStateNum){			
				gameState.gameStateNum++;
	
				// Spawn Minions
				HashMap<Integer, Hive> hives = gameState.hives;
				for (int i = 0; i < hives.size(); i ++){
					Hive hive = hives.get(i);
					
					// Don't spawn for neutrals
					if (hive.controllingPlayer == Control.Neutral){
						continue;
					}
					hive.nextSpawnTime--;
					if (hive.nextSpawnTime <= 0){
						hive.nextSpawnTime = hive.spawnRate;
						if(hive.numMinions < hive.hiveCapacity){
							hive.numMinions++;
						}
					}
				}
				
				// Check for collisions
				ArrayList<Attack> attacks = gameState.attacks;
				for (int i = 0; i < attacks.size(); i ++){
					Attack attack = attacks.get(i);
					
					// TODO below may need to be changed to ==
					if (attack.hitTime <= gameState.gameStateNum){
						Hive hive = hives.get(attack.destHiveNum);
						if (attack.player == hive.controllingPlayer){
							if(hive.numMinions < hive.hiveCapacity){
								hive.numMinions++;
							}
						}else {
							hive.numMinions--;
							if(hive.numMinions == 0){
								hive.controllingPlayer = Control.Neutral;
							} else if (hive.numMinions < 0){
								hive.controllingPlayer = attack.player;
								hive.numMinions = 1;
							}
						}
						attacks.remove(i);
					}				
				}			
			}
		}
	}

	public void getSemaphoreForReading(){
	  try {
	          GSFreeForReading.acquire();
	  } catch (InterruptedException e) {e.printStackTrace();}
	}
	
	public void releaseSemaphoreForReading(){
		GSFreeForReading.release();
	}	
	
	public void getSemaphoreForWriting(){
		try {
			GSFreeForWriting.acquire();
		  } catch (InterruptedException e) {e.printStackTrace();}
		}
		
	public void releaseSemaphoreForWritng(){
		GSFreeForWriting.release();
	}	
}
