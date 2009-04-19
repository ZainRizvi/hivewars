package hivewars;

import hivewars.GameSettings.Control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

//
//********* Being worked on by Zain ************//
//

@SuppressWarnings("serial")
public class GameStateData implements Serializable{
    
	int gameStateNum;        //clock time when this state occurred
	HashMap<Integer,Hive> hives; 	   //all hives and their statuses
	ArrayList<Attack> attacks; //all attacks under progress
	
	
	public GameStateData() {
		gameStateNum = GameSettings.initialState;
	}
	
	//used to clone gameState
	public GameStateData(GameStateData gameState){
		gameStateNum = gameState.gameStateNum;
		hives = (HashMap<Integer,Hive>) gameState.hives.clone();
		attacks = (ArrayList<Attack>) gameState.attacks.clone();
	}

	
	public String GameStateData(){
		String s = new String();
		s += "Att#=" + attacks.size();
		return s;
	}
}
