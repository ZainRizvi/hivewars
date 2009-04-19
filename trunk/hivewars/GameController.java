package hivewars;

import hivewars.GameSettings.Control;

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

public class GameController implements GameSettings{
	
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
	public static int remotePort = 0;
	public static InetAddress remoteInetAddr = null;
	public static UDPSocket socket;
	
	public static UDPSocket TestSocket; 
	
	static boolean GameStarted = false;
	static boolean GameFinished = false;
	
	//who am I: PlayerA, PlayerB, Neutral
	static GameSettings.Control Me = GameSettings.Control.Neutral;
	
	public static void main(String[] args) {
        //initialize reconcile
		//initialize everything
		ViewableGS = new GameStateController();
		new Map();
		GameStateData initialGameState = new GameStateData();
		initialGameState.hives = Map.hives;
		initialGameState.attacks = new ArrayList<Attack>();
		ViewableGS.updateGameState(initialGameState);
		CurrentAttack = null;
		MasterGS = new GameStateController();
		MasterGS.updateGameState(ViewableGS.readGameState());
		
		// Initialize game state receiver thread.  Game starts for player A when 
		//   he receives the first communication.
		new Receive();
		
		//remotePort = 50000;
		try {
			remoteInetAddr = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//start Gui
		new Gui();
		
		while(Me == Control.Neutral){}; //player hasn't chosen to host or join yet
		if(Me == Control.PlayerB){
			// Start playing. when player A will start when he receives player B's first state  
			GameStarted = true; 
		}
		System.out.println("getting somewhere: " + Me);
		// Player A wait for Player B to send him a game state.  
		//    Allows him to learn B's inet and port address
		while (!GameStarted);
		
		while(!GameFinished){
			//call clock every 100ms
			new Clock();
			Thread.currentThread();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		
		// Clean up?
		
	}
	
	//appends new attack to PlayerAttackList
	public static void writeCurrentAttack(Attack newAttack) throws InterruptedException{
		if(newAttack != null){
			double hyp;
			int xv, xdist, ydist;
			int sourceX = Map.hives.get(newAttack.sourceHiveNum).x;
			int destX = Map.hives.get(newAttack.destHiveNum).x;
			int sourceY = Map.hives.get(newAttack.sourceHiveNum).y;
			int destY = Map.hives.get(newAttack.destHiveNum).y;
			//calculate hitTime
			xdist = destX - sourceX;
			ydist = destY - sourceY;
			hyp =  Math.sqrt(xdist*xdist + ydist*ydist);
			xv = (int) ((xdist / hyp) * GameSettings.ATTACK_SPEED);
			newAttack.hitTime = (short) (newAttack.firingTime + xdist / xv);
			System.out.println("hitTime: " + newAttack.hitTime);
		}
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