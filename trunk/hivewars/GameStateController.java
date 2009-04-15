package hivewars;

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
	
	public synchronized void fastForward(int stateNum){
		
	}
	
//	public void getSemaphore(){
//		try {
//			GSFree.acquire();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public void releaseSemaphore(){
//		GSFree.release();
//	}

}
