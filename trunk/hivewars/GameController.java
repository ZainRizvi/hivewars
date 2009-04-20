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
	public static int localPort;
	public static InetAddress localInetAddr;
	
	public static UDPSocket TestSocket; 
	
	static boolean GameStarted = false;
	static boolean GameFinished = false;
	static boolean StopAttacks = false; //stop attacks before finishing game
	static int LastHit = 0;
	static int prevOpponentAttackTime = 0; //the opponent's state number when he last attacked
	static int prevOwnAttackTime = 0;
	//who am I: PlayerA, PlayerB, Neutral
	static GameSettings.Control Me = GameSettings.Control.Neutral;
	public static Control Winner;  //game winner
	
	static Semaphore init = new Semaphore(1, true);
	
	/* <-m> : go to menu 
	 * <-w [port]> : wait on "port" (optional) ... 
	 * <-c ip port> : connect to "ip" on "port"
	 */
	
	static String Option = null;
	static String arg0 = null;
	static String arg1 = null;
	
	public static void main(String[] args) throws UnknownHostException {
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
		
		if(args.length > 0)	Option = new String(args[0]);
		if(args.length > 1) arg0 = new String(args[1]);
		if(args.length > 2) arg1 = new String(args[2]);
		
		// Initialize game state receiver thread.  Game starts for player A when 
		//   he receives the first communication.
		new Receive();
		
		////System.out.println(Option + " " + arg0 + " " + arg1);
		//start Gui
		new Gui();
		try {
			init.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//System.out.println("Gui initialized");
		
		while(Me == Control.Neutral){
			//player hasn't chosen to host or join yet
			Thread.currentThread();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {e.printStackTrace();}
		}; 
		
		while(!GameFinished){
			//call clock every 100ms
			new Clock();
			Thread.currentThread();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {e.printStackTrace();}
		}		
	}
	
	public static void writeCurrentAttack(Attack newAttack) throws InterruptedException{
		if(newAttack != null){
			double hyp;
			double xv, yv;
			double xdist, ydist;
			int sourceX = Map.hives.get(newAttack.sourceHiveNum).x - 12;
			int destX = Map.hives.get(newAttack.destHiveNum).x - 12;
			int sourceY = Map.hives.get(newAttack.sourceHiveNum).y - 12;
			int destY = Map.hives.get(newAttack.destHiveNum).y - 12;
			//calculate hitTime
			xdist = destX - sourceX;
			ydist = destY - sourceY;
			hyp = Math.sqrt(xdist*xdist + ydist*ydist);
			xv = ((xdist / hyp) * GameSettings.ATTACK_SPEED);
			yv = ((ydist / hyp) * GameSettings.ATTACK_SPEED);
			if(xv == 0){
				newAttack.hitTime = (short) (newAttack.firingTime + ydist / yv);
			} else {
				newAttack.hitTime = (short) (newAttack.firingTime + xdist / xv);
			}
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
