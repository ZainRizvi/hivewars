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
	Attack subtractThisAttack; //alert sent to the enemy to let him know that he sent this attack illegally
	
	public GameStateData() {
		gameStateNum = GameSettings.initialState;
	}
	
	//used to clone gameState
	public GameStateData(GameStateData gameState){
		gameStateNum = gameState.gameStateNum;
		hives = new HashMap<Integer,Hive>();
		
		HashMap<Integer,Hive> otherH = gameState.hives;
		for (int i = 0; i < otherH.size(); i++){
			Hive h = new Hive(otherH.get(i)); //creates a deep copy of the hive
			hives.put(h.id, h);
		}		
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
		
		s+="}\n";
		
		return s;
	}
}
