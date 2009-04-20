package hivewars;

import hivewars.GameSettings.Control;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Receive implements Runnable{
	int localPort;
	InetAddress localInetAddr;
	
	public Receive() {
		if (GameController.Option != null){
			if(GameController.Option.equals("-w") && GameController.arg0 != null){
				GameController.socket = new UDPSocket(Integer.parseInt(GameController.arg0));
			} else {
				GameController.socket = new UDPSocket();
			}
		}else{
			GameController.socket = new UDPSocket();			
		}
		localPort = GameController.socket.getLocalPort();
		try {
			localInetAddr = InetAddress.getByName(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		GameController.localInetAddr = localInetAddr;
		GameController.localPort = localPort;
		
		Thread t = new Thread(this);
		t.start();
	}	
	
	@Override
	public void run() {
		receiveGameStates();
	}
	
	//
	// Keep receiving new game states from remote player and merge them
	//   with the master game state
	//
	public void receiveGameStates(){
		//get incoming game state
		GameStateData incomingGS = (GameStateData) GameController.socket.getMessage();
		GameController.GameStarted = true;		
		
		//store remote socket's and IP address information
		GameController.remotePort = GameController.socket.remoteSocketData.port;			
		GameController.remoteInetAddr = GameController.socket.remoteSocketData.IPAddr;

		//reconcile incoming game state with master game state
		GameController.MasterGS.ReconcileGS(incomingGS);
		
		while(!GameController.GameFinished){
			//get incoming game state
			incomingGS = (GameStateData) GameController.socket.getMessage();
			
			
			/*	
			String player = new String();
	    	if(GameController.Me == Control.PlayerA){
	    		player += "A";
	    	} else {
	    		player += "B";
	    	}
	    	System.out.print(player + "<" + incomingGS.gameStateNum +
	    			"," + incomingGS.attacks.size() + "atks" + '\t');
	    			
	    	*/
			
			//reconcile incoming game state with master game state
			//GameController.MasterGS.ReconcileGS(incomingGS);
			GameStateData mst = GameController.MasterGS.readGameState();
			System.out.print("RX: in: " + incomingGS.gameStateNum + ",a" + incomingGS.attacks.size() + " mstbefore: " + mst.gameStateNum + ",a" + mst.attacks.size() );
			ReconcileMasterGS(incomingGS);			
			mst = GameController.MasterGS.readGameState();
			//System.out.println(mst);
			System.out.println("  mstafter: " + mst.gameStateNum + ",a" + mst.attacks.size());
			//System.out.print(">MS:" + GameController.MasterGS.readGameState() + " VS: " + GameController.ViewableGS.readGameState() + '\t'); 
		}		
	}
	

	//
	// Reconciles the Master and Viewable game states with incoming game state
	//
	public synchronized void ReconcileMasterGS(GameStateData newGS){
		System.out.println("ReconcileMasterGS");
		GameController.MasterGS.getSemaphore();
		
		int oldStateNum = GameController.MasterGS.readGameState().gameStateNum;
		int viewStateNum = GameController.ViewableGS.readGameState().gameStateNum;
		
		if(viewStateNum >= newGS.gameStateNum){
			//Forward old GameState to be at the same state number as new GS.
			//Viewable is already at or ahead of MasterGS
			GameController.MasterGS.fastForward(newGS.gameStateNum);
		}else{
			GameController.MasterGS.fastForward(GameController.ViewableGS.readGameState().gameStateNum);
		}
		
		if(GameController.lastRemoteClock < newGS.gameStateNum){
			GameController.lastRemoteClock = newGS.gameStateNum;
		}
		
		// Add all attacks from the viewable up to this state
		ArrayList<Attack> vattacks = GameController.ViewableGS.readGameState().attacks;
		for (int i = 0; i < vattacks.size(); i++){
			Attack a = vattacks.get(i);
			GameController.MasterGS.addAttack(a);
		}
		
		//add all new attacks from the newGS into master and viewable game states
		int numAttacks = newGS.attacks.size();
		GameController.ViewableGS.getSemaphore();
		for(int i = 0; i < numAttacks; i++){
			Attack attack = newGS.attacks.get(i);
			if(attack.firingTime > GameController.prevOpponentAttackTime){	
				GameController.prevOpponentAttackTime = attack.firingTime;
				System.out.print("(" + attack.firingTime + "," + oldStateNum + ") ");
				GameController.MasterGS.addAttack(attack);
				GameController.ViewableGS.addAttack(attack);
			}
		}
		GameController.ViewableGS.releaseSemaphore();
		GameController.MasterGS.releaseSemaphore();
	}	
}
