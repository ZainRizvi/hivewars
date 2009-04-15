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
	
	public void addAttack(Attack attack){
		//add mutex
		try {
			GSFree.acquire();} catch (InterruptedException e) {e.printStackTrace();
		}
		//add attack
		gameState.attacks.add(attack);
		GSFree.release();
	}

	//update game state number
	public void updateTime(short newStateNum){
		//add mutex
		try {
			GSFree.acquire();} catch (InterruptedException e) {e.printStackTrace();
		}
		//update time
		gameState.gameStateNum = newStateNum;
		GSFree.release();
	}
	
	//removes attack from game state list
	public void attackCompleted(Attack attack){
		//add mutex
		try {
			GSFree.acquire();} catch (InterruptedException e) {e.printStackTrace();
		}
		//get all attacks
		int numAttacks = gameState.attacks.size();
		for(int i = 0; i < numAttacks; i++){
			
		}
		gameState.attacks.remove(attack);
		GSFree.release();
	}

	//for when hive status changes
	public void updateHive(char hiveNum, char controllingPlayer, char numMinions){
		//add mutex
		
		//make sure no more than hiveCapacity minions are in hive
	}
	
	public void updateGameState(GameStateData newGameState){
		//add mutex 
		gameState = newGameState;
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
