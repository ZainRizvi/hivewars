package hivewars;

import hivewars.GameSettings.Control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;



public class GameStateController {
	
	private GameStateData gameState;
	private final Semaphore GSFree = new Semaphore(1, true);

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

		// BIG CHANGE HERE
		if (attack.hitTime <= this.gameState.gameStateNum){
			//Do nothing
		}else{		
			if(attack.player != GameController.Me || hive.numMinions>1){
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
				
				if (!attackExists){
					gameState.attacks.add(attack);
					hive.numMinions--;
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
							hive.numMinions++;
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

	public void getSemaphore(){
	  try {
	          GSFree.acquire();
	  } catch (InterruptedException e) {e.printStackTrace();}
	}
	
	public void releaseSemaphore(){
	  GSFree.release();
	}	
}
