package hivewars;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Receive implements Runnable{
	int localPort;
	InetAddress localInetAddr;
	
	public Receive() throws UnknownHostException {
		if (GameController.Option != null){
			if(GameController.Option.equals("-w") && GameController.arg0 != null){
				GameController.socket = new UDPSocket(Integer.parseInt(GameController.arg0));
			} else {
				GameController.socket = new UDPSocket();
			}
		} else {
			GameController.socket = new UDPSocket();
		}		
		localPort = GameController.socket.getLocalPort();
		localInetAddr = InetAddress.getByName(InetAddress.getLocalHost().getHostName());
		GameController.localInetAddr = localInetAddr;
		GameController.localPort = localPort;
		new Thread(this).start();
	}	
	
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

		
		// TODO WTF? Why two reconciles?
		//GameController.MasterGS.ReconcileGS(incomingGS);
		
		while(!GameController.GameFinished){
			incomingGS = (GameStateData) GameController.socket.getMessage();			
			ReconcileMasterGS(incomingGS);
		}		
	}	

	//
	// Reconciles the Master and Viewable game states with incoming game state
	//
	public synchronized void ReconcileMasterGS(GameStateData newGS){
		GameController.ViewableGS.getSemaphore();
		GameController.MasterGS.getSemaphore();
		
		int viewStateNum = GameController.ViewableGS.readGameState().gameStateNum;
		int incomingStateNum = newGS.gameStateNum;
		
		//catch masterGS up to whichever GS is behind (meaning you spawn minions and check for collisions)
		if(viewStateNum >= incomingStateNum){
			//Forward old GameState to be at the same state number as new GS.
			//Viewable is already at or ahead of MasterGS
			GameController.MasterGS.fastForward(incomingStateNum);
		}else{
			GameController.MasterGS.fastForward(viewStateNum);
		}
		
		// Add all attacks from the viewable up to this state
		ArrayList<Attack> vattacks = GameController.ViewableGS.readGameState().attacks;
		for (int i = 0; i < vattacks.size(); i++){
			Attack a = vattacks.get(i);
			GameController.MasterGS.addAttack(a);
		}
		
		//------------subtract attack-------------------
		//if the enemy says that I fired illegally believe him and subtract the illegal attack 
		if(newGS.subtractThisAttack != null){
			GameController.ViewableGS.readGameState().hives.get(newGS.subtractThisAttack.sourceHiveNum).numMinions++;
			GameController.MasterGS.readGameState().hives.get(newGS.subtractThisAttack.sourceHiveNum).numMinions++;
			GameController.ViewableGS.readGameState().attacks.remove(newGS.subtractThisAttack);
			GameController.ViewableGS.readGameState().attacks.remove(newGS.subtractThisAttack);
		}
		
		// Add all new attacks from the newGS into master and viewable game states
		int numAttacks = newGS.attacks.size();
		int maxOpponentFiring = 0;
		int maxOwnFiring = 0;
		
		for(int i = 0; i < numAttacks; i++){
			Attack attack = newGS.attacks.get(i);
			
			//make sure you don't add any attacks twice?
			//THIS COULD POSSIBLY BE REMOVED BECAUSE YOU CHECK FOR REDUNDANT ATTACK IN ADDATTACK()
			//WHY WOULD I LOAD MY OWN ATTACK AT ALL HERE, I DID IT ALREADY
			if(attack.player == GameController.Me){
				if(attack.firingTime > GameController.prevOwnAttackTime){
					if(attack.firingTime > maxOwnFiring){
						maxOwnFiring = attack.firingTime;
					}
					GameController.MasterGS.addAttack(attack);
					GameController.ViewableGS.addAttack(attack);
				}				
			}else{	
				if(attack.firingTime > GameController.prevOpponentAttackTime){
					if(attack.firingTime > maxOpponentFiring){
						maxOpponentFiring = attack.firingTime;
					}
					GameController.MasterGS.addAttack(attack);
					GameController.ViewableGS.addAttack(attack);
				}
			}
		}
		if (maxOwnFiring > GameController.prevOwnAttackTime){			
			GameController.prevOwnAttackTime = maxOwnFiring;
		}
		if (maxOpponentFiring > GameController.prevOpponentAttackTime){			
			GameController.prevOpponentAttackTime = maxOpponentFiring;
		}
		
		GameController.MasterGS.releaseSemaphore();
		GameController.ViewableGS.releaseSemaphore();
	}	
}
