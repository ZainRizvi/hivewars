package hivewars;

import java.util.HashMap;

public class GameController {
	
	// Game State that both players agree on
	static GameStateController MasterGS;
	
	// A merge of the latest GS from the other player
	// and the most recent GS in GSHistory.
	static GameStateController ViewableGS;
	
	static Attack Construction;
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
