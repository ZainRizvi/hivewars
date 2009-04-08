package hivewars;

import hivewars.GameSettings.Control;

import java.io.Serializable;
import java.util.ArrayList;

//
//********* Being worked on by Zain ************//
//

public class GameStateData implements Serializable{
    
	short gameStateNum;        //clock time when this state occurred
	ArrayList<Hive> hives; 	   //all hives and their statuses
	ArrayList<Attack> attacks; //all attacks under progress
	
	
	public GameStateData() {
		gameStateNum = GameSettings.initialState;
		for(char hiveNum = 0; hiveNum < GameSettings.numHives; hiveNum++){
			hives.add(new Hive(hiveNum, GameSettings.DefaultControler, 
					GameSettings.defaultNumMinions, GameSettings.defaultSpawnRate, 
					GameSettings.defaultHiveCapacity));
		}
	}
	
	//used to clone gameState
	public GameStateData(GameStateData gameState){
		gameStateNum = gameState.gameStateNum;
		hives = (ArrayList<Hive>) gameState.hives.clone();
		attacks = (ArrayList<Attack>) gameState.attacks.clone();
	}

}
