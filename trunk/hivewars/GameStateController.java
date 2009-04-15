package hivewars;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;



public class GameStateController {
	
	private GameStateData gameState;
	private static final Semaphore GSFree = new Semaphore(1, true);

	public GameStateController() {
		gameState = new GameStateData();
	}
	
	public synchronized GameStateData readGameState(){
		return new GameStateData(gameState);
	}
	
	public synchronized void updateGameState(GameStateData newGameState){
		//add mutex 
		gameState = newGameState;
	}
		
	public synchronized void addAttack(Attack attack){
		//add mutex
		//add attack
		gameState.attacks.add(attack);
	}

	//update game state number
	public synchronized void updateTime(short newStateNum){
		//add mutex
		//update time
		gameState.gameStateNum = newStateNum;
	}
	
	//removes attack from game state list
	public synchronized void attackCompleted(Attack attack){
		//add mutex
		//remove attack
		gameState.attacks.remove(attack);
	}

	//for when hive status changes
	//used to change the controlling player or number of minions
	public synchronized void updateHive(char hiveNum, GameSettings.Control controllingPlayer, char numMinions){
		//add mutex
		//make sure no more than hiveCapacity minions are in hive
		Hive hive = gameState.hives.get((int) hiveNum);
		hive.controllingPlayer = controllingPlayer;
		hive.numMinions = numMinions;
		gameState.hives.set((int) hiveNum, hive);
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
	
	public synchronized void ReconcileNewMoves(GameStateData GS){
		GameStateData newGS = new GameStateData(); 
		//merge a game state with PlayerAttackList commands 
	}	
	
	public synchronized void fastForward(int finalStateNum){
		// Assumes that the gameState's gameStateNum has not been advanced
		
		if (gameState.gameStateNum >= finalStateNum){
			System.out.println("WTF?");
			return;
		}
		
		while (gameState.gameStateNum < finalStateNum){			
			gameState.gameStateNum++;
			
			// Spawn Minions
			ArrayList<Hive> hives = gameState.hives;
			for (int i = 0; i < hives.size(); i ++){
				Hive hive = hives.get(i);				
				hive.nextSpawnTime--;
				if (hive.nextSpawnTime == 0){
					hive.nextSpawnTime = hive.spawnRate;					
					hive.numMinions++;					
				}				
			}			
			
			// Check for collisions
			ArrayList<Attack> attacks = gameState.attacks;
			for (int i = 0; i < attacks.size(); i ++){
				Attack attack = attacks.get(i);
				
				if (attack.hitTime == gameState.gameStateNum){
					hives.get(attack.destHiveNum).numMinions--;
					attacks.remove(i);
				}				
			}			
		}		
	}
	
	public synchronized void incrementCounter(){
		gameState.gameStateNum++;
	}

}
