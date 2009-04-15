package hivewars;

import java.net.InetAddress;

public class Receive implements Runnable{
	int localPort;
	InetAddress localInetAddr;
	
	public Receive() {
		//socket = new UDPSocket();
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
}
