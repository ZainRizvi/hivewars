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
