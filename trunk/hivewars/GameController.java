package hivewars;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

/***
 * Name: Game Controller
 * Description:
 * 
 * @author josh
 */

public class GameController {
	
	// Game State that both players agree on
	public static GameStateController MasterGS;	
	// A merge of the latest GS from the other player
	// and the most recent GS in GSHistory.
	public static GameStateController ViewableGS;
	// Written to by the GUI
	// Read by the Clock
	public static Attack CurrentAttack = null;
	
	public static Semaphore attackMutex = new Semaphore(1, true);
	
	
	static boolean GameStarted = false;
	static boolean GameFinished = false;
	
	public static void main(String[] args) {
        //initialize reconcile
		//initialize everything
		
		//start Gui
		new Gui();
		
		// Wait for game to start
		while (!GameStarted);
		
		while(!GameFinished){
			//call clock every 10hz
			new Clock();
			Thread.currentThread();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// Clean up?
		
	}
	
	//appends new attack to PlayerAttackList
	public static void writeCurrentAttack(Attack newAttack) throws InterruptedException{
		attackMutex.acquire();
		CurrentAttack = newAttack;
		attackMutex.release();
	}
	
	public static Attack readCurrentAttack() throws InterruptedException{
		attackMutex.acquire();
		Attack a = Attack.copy(CurrentAttack);
		attackMutex.release();
		return a;
	}

}
