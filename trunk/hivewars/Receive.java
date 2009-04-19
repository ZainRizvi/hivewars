package hivewars;

import java.net.InetAddress;

public class Receive implements Runnable{
	int localPort;
	InetAddress localInetAddr;
	
	public Receive() {
		GameController.socket = new UDPSocket();
		localPort = GameController.socket.getLocalPort();
		localInetAddr = GameController.socket.getLocalAddress();
		
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
		
			//reconcile incoming game state with master game state
			GameController.MasterGS.ReconcileGS(incomingGS);			
		}		
	}
	

	//
	// Reconciles the Master and Viewable game states with incoming game state
	//
	public synchronized void ReconcileMasterGS(GameStateData newGS){
		GameController.MasterGS.getSemaphore();
		
		int oldStateNum = GameController.MasterGS.readGameState().gameStateNum;
		int viewStateNum = GameController.ViewableGS.readGameState().gameStateNum;
		
		if(viewStateNum >= newGS.gameStateNum){
			//Forward old GameState to be at the same state number as new GS.
			//Viewable is already at or ahead of MasterGS
			GameController.MasterGS.fastForward(newGS.gameStateNum);
		}
		
		if(GameController.lastRemoteClock < newGS.gameStateNum){
			GameController.lastRemoteClock = newGS.gameStateNum;
		}
		
		//add all new attacks from the newGS into master and viewable game states
		int numAttacks = newGS.attacks.size();
		GameController.ViewableGS.getSemaphore();
		for(int i = 0; i < numAttacks; i++){
			Attack attack = newGS.attacks.get(i);
			if(attack.firingTime > oldStateNum){
				GameController.MasterGS.addAttack(attack);
				GameController.ViewableGS.addAttack(attack);
			}
		}
		GameController.ViewableGS.releaseSemaphore();
		GameController.MasterGS.releaseSemaphore();
	}	
}