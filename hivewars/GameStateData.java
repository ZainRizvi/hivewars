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

	
	public String toString(){
		String s = new String();
		s += "{H\n";
		for (int i = 0; i < hives.size(); i ++){
			s += hives.get(i) + "\n";
		}
		s+= "A\n";
		for (int i = 0; i < attacks.size(); i ++){
			s+= attacks.get(i) + "\n";
		}
		
		return s;
	}
}
