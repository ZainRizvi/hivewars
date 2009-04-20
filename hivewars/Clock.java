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
    			transmitCurrentViewable();
    			return;
    		}    		
    		
			createNewViewableGameState();			
	    	transmitCurrentViewable();
	    	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public static void createNewViewableGameState() throws InterruptedException{        	
    	GameController.ViewableGS.getSemaphore();
    	Attack currentAttack = GameController.readCurrentAttack(); 
    	if(currentAttack != null){
    		GameController.ViewableGS.addAttack(currentAttack);
    		GameController.writeCurrentAttack(null);  //reset current attack variable
    	}
    	
    	//determine which attacking minions have reached their target, 
    	//		modify hive status as appropriate, and remove those attacks 
    	//		from Viewable GameState   	
    	int currentState = GameController.ViewableGS.readGameState().gameStateNum;
    	GameController.ViewableGS.fastForward(currentState + 1);
    	
    	GameController.ViewableGS.releaseSemaphore();  	
    	determineIfGameOver();   	
    }
    
    public static void determineIfGameOver(){
    	GameController.ViewableGS.getSemaphore();
    	GameController.MasterGS.getSemaphore();
    	
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
    	
    	Control leader = Control.Neutral;
    	if((playerATerr == 0) || (playerBTerr == 0)){
    		boolean gameOver = true;
	    	if(playerATerr == 0){
	    		leader = Control.PlayerB;
	    		
	    	}
	    	if(playerBTerr == 0){
	    		leader = Control.PlayerA;
	    		//GameController.StopAttacks = true;
	    	}
	    	
	    	// one player has lost all his hives
	    	// see if he has any minions left on the field
    		for(int i = 0; i < GameController.MasterGS.readGameState().attacks.size(); i++){
    			Attack attack = GameController.MasterGS.readGameState().attacks.get(i);
        		if(attack.player != leader){
        			gameOver = false; // the underdog has a minion left.  He just may capture another hive!
        		}
    		}
    		if(gameOver){
    			//nope, he had no minions.  game over
	    		GameController.Winner = leader;
	    		GameController.GameFinished = gameOver;
    		}
    	}
    	GameController.ViewableGS.releaseSemaphore();
    	GameController.MasterGS.releaseSemaphore();    	
    }
    
    
    public static void transmitCurrentViewable(){
    	GameController.ViewableGS.getSemaphore();
    	GameController.socket.sendMessage(
    			GameController.ViewableGS.readGameState(), 
    			GameController.remoteInetAddr, 
    			GameController.remotePort);
    	GameController.ViewableGS.releaseSemaphore();
    }
    
    
}
