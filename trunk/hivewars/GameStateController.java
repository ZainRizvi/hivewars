package hivewars;

import java.util.concurrent.Semaphore;



public class GameStateController {
	
	private GameStateData gameState;
	private static final Semaphore GSFree = new Semaphore(1, true);

	public GameStateController() {
		gameState = new GameStateData();
	}
	
	public GameStateData readGameState(){
		return new GameStateData(gameState);
	}
	
	public void updateGameState(GameStateData newGameState){
		//add mutex 
		gameState = newGameState;
	}
		
	public void addAttack(Attack attack){
		//add mutex
		getSemaphore();
		//add attack
		gameState.attacks.add(attack);
		releaseSemaphore();
	}

	//update game state number
	public void updateTime(short newStateNum){
		//add mutex
		getSemaphore();
		//update time
		gameState.gameStateNum = newStateNum;
		releaseSemaphore();
	}
	
	//removes attack from game state list
	public void attackCompleted(Attack attack){
		//add mutex
		getSemaphore();
		//remove attack
		gameState.attacks.remove(attack);
		releaseSemaphore();
	}

	//for when hive status changes
	//used to change the controlling player or number of minions
	public void updateHive(char hiveNum, GameSettings.Control controllingPlayer, char numMinions){
		
		//add mutex
		getSemaphore();
		//make sure no more than hiveCapacity minions are in hive
		Hive hive = gameState.hives.get((int) hiveNum);
		hive.controllingPlayer = controllingPlayer;
		hive.numMinions = numMinions;
		gameState.hives.set((int) hiveNum, hive);
		releaseSemaphore();
	}
	
	public void ReconcileGS(GameStateData newGS){
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
	
	public void fastForward(int stateNum){
		
	}
	
	public void getSemaphore(){
		try {
			GSFree.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void releaseSemaphore(){
		GSFree.release();
	}

}
