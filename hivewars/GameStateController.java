package hivewars;



public class GameStateController {
	
	private GameStateData gameState;

	public GameStateController() {
		// TODO Auto-generated constructor stub
	}
	
	public GameStateData readGameState(){
		return new GameStateData(gameState);
	}
	
	public void addAttack(Attack attack){
		//add mutex
	}

	//update game state number
	public void updateTime(short time){
		//add mutex
	}
	
	//removes attack from game state list
	public void attackCompleted(Attack attack){
		//add mutex
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

}
