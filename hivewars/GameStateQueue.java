package hivewars;

import java.util.ArrayList;

//Stores all the old game states since the last time the 
//GameState has been synced with remote player
public class GameStateQueue {

	ArrayList<GameStateData> History;
	
	public GameStateQueue() {
		// TODO Auto-generated constructor stub
	}
	
	//appends new GameState to end of the list
	public void addGameState(GameStateData newState){
		//add mutex
	}
	
	//removes all GameState that occurred before and including GSNumber
	public void removeGameStates(int GSNumber){
		//add mutex
	}

}
