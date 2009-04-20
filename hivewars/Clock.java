package hivewars;

import hivewars.GameSettings.Control;

import java.util.ArrayList;
import java.util.HashMap;

/***
 * Name: Clock
 * Description: Updates user variable and sends across UDP
 * 	Spawned at 10Hz.
 * @author josh
 */
public class Clock implements Runnable{
	
	public Clock() {
		this.run();
	}
    public void run() {
    	try {
    		
    		// Need to lag?
    		if (GameController.MasterGS.readGameState().gameStateNum + 10 
    				<= GameController.ViewableGS.readGameState().gameStateNum){
    			System.out.println("lag");
    			transmitCurrentViewable();
    			return;
    		}
    		
    		
			createNewViewableGameState();
			
			//  Update Master Game State to either the remote game state or viewable game state,
			//    whichever is lower
			
			/*
			GameController.MasterGS.getSemaphore();
			if (GameController.lastRemoteClock > GameController.MasterGS.readGameState().gameStateNum){
				GameController.MasterGS.fastForward(GameController.ViewableGS.readGameState().gameStateNum);
			}
			GameController.MasterGS.releaseSemaphore();
			*/
			
	    	transmitCurrentViewable();
	    	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(GameController.LastHit > GameController.ViewableGS.readGameState().gameStateNum){
			//if(GameController.MasterGS.readGameState().attacks.size() <= 0){
				System.out.println("Remaining attacks: " + GameController.ViewableGS.readGameState().attacks.size() );
				GameController.GameFinished = true;
			//}
		}
    }
    
    public static void createNewViewableGameState() throws InterruptedException{
    	System.out.println("createNewViewableGameState");
    	
    	Attack currentAttack = GameController.readCurrentAttack();
    	
    	GameController.ViewableGS.getSemaphore();
    	
    	if(!GameController.StopAttacks){
	    	if(currentAttack != null){
	    		GameController.ViewableGS.addAttack(currentAttack); 
	
				System.out.println("Clock reads attack: " + currentAttack);
	    		GameController.writeCurrentAttack(null);  //reset current attack variable
	    	}
    	} else {
    		System.out.println("Didn't register the attack");
    	}
    	
    	//determine which attacking minions have reached their target, 
    	//		modify hive status as appropriate, and remove those attacks 
    	//		from Viewable GameState
    	int currentState = GameController.ViewableGS.readGameState().gameStateNum;
    	GameController.ViewableGS.fastForward(currentState + 1);
    	
    	GameController.ViewableGS.releaseSemaphore();
    	
    	//TODO: see if one player controls all bases => game is over!
    	HashMap<Integer,Hive> hives = GameController.ViewableGS.readGameState().hives;
    	int playerATerr = 0;
    	int playerBTerr = 0;
    	for(int i = 0; i < hives.size(); i++){
    		if (hives.get(i).controllingPlayer ==  Control.PlayerA){
    			playerATerr++;
    		} else if (hives.get(i).controllingPlayer ==  Control.PlayerB){
    			playerBTerr++;
    		}
    	}
    	if(playerATerr == hives.size()){
    		GameController.Winner = Control.PlayerA;
    		GameController.StopAttacks = true;
    	}
    	if(playerBTerr == hives.size()){
    		GameController.Winner = Control.PlayerB;
    		GameController.StopAttacks = true;
    	}
    	if(GameController.StopAttacks == true){
    		int lastHit = 0;
    		ArrayList<Attack> remainingAttacks = GameController.ViewableGS.readGameState().attacks; 
    		for(int i = 0; i < remainingAttacks.size(); i++){
    			if(remainingAttacks.get(i).hitTime > lastHit){
    				lastHit = remainingAttacks.get(i).hitTime ;
    			}
    		}
    		GameController.LastHit = lastHit;
    	}
    	
    	System.out.println(GameController.MasterGS.readGameState());
    	
    }
    
    public static void transmitCurrentViewable(){
    	String player = new String();
    	//if(GameController.Me == Control.PlayerA){
    	//	player += "A";
    	//} else {
    	//	player += "B";
    	//}
    	//System.out.print(player + ">" + GameController.ViewableGS.readGameState().gameStateNum +
    	//		"," + GameController.ViewableGS.readGameState().attacks.size() + "atks" + '\t');    	
    //	System.out.println(player + " sending game state " + GameController.ViewableGS.readGameState().gameStateNum +
    //			" Num attacks = " + GameController.ViewableGS.readGameState().attacks.size());
    	
    	GameController.ViewableGS.getSemaphore();
    	GameStateData vgs = GameController.ViewableGS.readGameState();
		System.out.println("TX: " + vgs.gameStateNum + ",a" + vgs.attacks.size() );    	
    	//System.out.println("TX: " + GameController.ViewableGS.readGameState().gameStateNum);
    	GameController.socket.sendMessage(
    			GameController.ViewableGS.readGameState(), 
    			GameController.remoteInetAddr, 
    			GameController.remotePort);
    	GameController.ViewableGS.releaseSemaphore();
    }
    
    
}
