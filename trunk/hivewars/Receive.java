package hivewars;

import java.net.InetAddress;

public class Receive implements Runnable{
	UDPSocket socket;
	int localPort;
	InetAddress localInetAddr;
	
	public Receive() {
		socket = new UDPSocket();
		localPort = socket.getLocalPort();
		localInetAddr = socket.getLocalAddress();
		
		Thread t = new Thread(this);
		t.start();
	}
	
	
	
	@Override
	public void run() {
		//get incoming game state
		GameStateData incomingGS = (GameStateData) socket.getMessage();
		//store game state
		Reconcile.ReconcileGS(GameController.MasterGS, incomingGS);
		//wake up Reconcile
	}
	

	

}
