package hivewars;

import java.util.ArrayList;
import java.util.HashMap;

public class GameController {
	
	static GameStateData MasterGS;
	static GameStateData ViewableGS;
	static GameStateData IncomingGS;
	static Attack Construction;
	static GameStateQueue GSHistory;
	//attacks that have not yet been put into the master game state
	//format: {AttackTime, Attack}
	static HashMap<Short, Attack> PlayerAttackList; 
	
	static boolean GameStarted = false;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        //initialize reconcile
		//initialize everything
		
		//start Gui
		new Gui();
		//wait for GameStarted
		while(true){
			//call clock every 10hz
			new Clock();
			Thread.currentThread();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//appends new attack to PlayerAttackList
	public static void addAttack(short time, Attack newAttack){
		
	}

}
