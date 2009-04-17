package hivewars;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

/***
 * Name: Game Controller
 * Description:
 * 
 * @author josh
 */

public class GameController {
	
	public static int lastRemoteClock = 0;
	
	// Game State that both players agree on
	public static GameStateController MasterGS;	
	// A merge of the latest GS from the other player
	// and the most recent GS in GSHistory.
	public static GameStateController ViewableGS;
	// Written to by the GUI
	// Read by the Clock
	public static Attack CurrentAttack = null;
	
	public static Semaphore attackMutex = new Semaphore(1, true);
	

	//port data for talking with remote player
	public static int remotePort;
	public static InetAddress remoteInetAddr;
	public static UDPSocket socket;
	
	public static UDPSocket TestSocket; 
	
	static boolean GameStarted = false;
	static boolean GameFinished = false;
	
	public static void main(String[] args) {
        //initialize reconcile
		//initialize everything
		ViewableGS = new GameStateController();
		new Map();
		GameStateData initialGameState = new GameStateData();
		initialGameState.hives = Map.hives;
		initialGameState.attacks = new ArrayList<Attack>();
		ViewableGS.updateGameState(initialGameState);
		CurrentAttack = new Attack(GameSettings.Control.Neutral, (char) 0, (char) 0, (short) 0);
		MasterGS = new GameStateController();
		MasterGS.updateGameState(ViewableGS.readGameState());
		new Receive();
		remotePort = 50000;
		try {
			remoteInetAddr = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//TestSocket= new UDPSocket(50000);
		
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
