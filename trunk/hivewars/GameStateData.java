package hivewars;

import java.io.Serializable;
import java.util.ArrayList;

public class GameStateData implements Serializable{
    
	short gameStateNum;        //clock time when this state occurred
	ArrayList<Hive> hives; 	   //all hives and their statuses
	ArrayList<Attack> attacks; //all attacks under progress
	
	
	public GameStateData() {
		// TODO Auto-generated constructor stub
	}
	
	//used to clone gameState
	public GameStateData(GameStateData gameState){
		gameStateNum = gameState.gameStateNum;
		hives = (ArrayList<Hive>) gameState.hives.clone();
		attacks = (ArrayList<Attack>) gameState.attacks.clone();
	}

}
