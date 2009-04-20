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
	
	public synchronized GameStateData readGameState(){
		GameStateData gsd = new GameStateData(gameState);
		return gsd;
	}
	
	public synchronized void updateGameState(GameStateData newGameState){
		gameState = newGameState;
	}
	
	public synchronized void updateGameStateNum(int newGameStateNum){
		gameState.gameStateNum = newGameStateNum;
	}
		
	public synchronized void addAttack(Attack attack){
		Hive hive = gameState.hives.get(attack.sourceHiveNum);
		// Make sure hive has minions to shoot
		
		if (attack.hitTime <= GameController.MasterGS.readGameState().gameStateNum){
			System.out.println("Nice try!");
			return;
		}
		
		if(hive.numMinions>0){
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
				System.out.println("Attack added");
				gameState.attacks.add(attack);
				hive.numMinions--;
			}
		}
	}
	
	//removes attack from game state list
	public synchronized void attackCompleted(Attack attack){
		gameState.attacks.remove(attack);
	}

	//for when hive status changes
	//used to change the controlling player or number of minions
	public synchronized void updateHive(int hiveNum, 
			GameSettings.Control controllingPlayer, 
			int numMinions){
		//make sure no more than hiveCapacity minions are in hive
		Hive hive = (Hive) gameState.hives.get(hiveNum);
		hive.controllingPlayer = controllingPlayer;
		hive.numMinions = numMinions;
	}
	
	public synchronized void ReconcileGS(GameStateData newGS){
		int oldStateNum = gameState.gameStateNum;
		
		//forward old GameState to be at the same state number as new GS
		fastForward(newGS.gameStateNum);
			
		//add all new attacks from the newGS into the current Game State
		int numAttacks = newGS.attacks.size();
		for(int i = 0; i < numAttacks; i++){
			Attack attack = newGS.attacks.get(i);
			if(attack.firingTime > oldStateNum){
				gameState.attacks.add(attack);
			}
		}		
	}
	
	public synchronized void fastForward(int finalStateNum){
		// Assumes that the gameState's gameStateNum has not been advanced
		
		if (gameState.gameStateNum >= finalStateNum){
			System.out.println("Trying to rewind?");
			return;
		}
		
		while (gameState.gameStateNum < finalStateNum){			
			gameState.gameStateNum++;

			// Spawn Minions
			HashMap<Integer, Hive> hives = gameState.hives;
			for (int i = 0; i < hives.size(); i ++){
				Hive hive = hives.get(i);
				hive.nextSpawnTime--;
				if (hive.nextSpawnTime <= 0){
					hive.nextSpawnTime = hive.spawnRate;
					if(hive.numMinions < hive.hiveCapacity){
						hive.numMinions++;
					}
					//System.out.println("new minion count: " + hive.numMinions);
				}
				//System.out.println("Hive " + hive.id + " will spawn dude in " + hive.nextSpawnTime + " seconds");
			}
			
			// Check for collisions
			ArrayList<Attack> attacks = gameState.attacks;
			for (int i = 0; i < attacks.size(); i ++){
				Attack attack = attacks.get(i);
				//System.out.println("state num: " + gameState.gameStateNum);
				//System.out.println("processing attack: hitTime = " + attack.hitTime + ", gameSN = " + gameState.gameStateNum);
				//System.out.println("num attacks: " + attacks.size());
				if (attack.hitTime <= gameState.gameStateNum){
					//System.out.println(attack); 
					Hive hive = hives.get(attack.destHiveNum);
					//System.out.println("Hit registered on hive " + attack.destHiveNum);
					//System.out.println("Old hive count = " + hive.numMinions);
					if (attack.player == hive.controllingPlayer){
						hive.numMinions++;
		//				//hack prevention code below
		//				if(hive.numMinions > hive.hiveCapacity){
		//					hive.numMinions = hive.hiveCapacity;
		//			}
					}else {
						hive.numMinions--;
						if(hive.numMinions == 0){
							hive.controllingPlayer = Control.Neutral;
						} else if (hive.numMinions < 0){
							hive.controllingPlayer = attack.player;
							hive.numMinions = 1;
						}
					}
					//System.out.println("New hive count = " + hive.numMinions);
					System.out.println("Attack Removed");
					attacks.remove(i);
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
