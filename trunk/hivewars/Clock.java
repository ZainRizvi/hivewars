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
	
	static GameStateController NewViewable;
	
	public Clock() {
		this.run();
	}
    public void run() {
		int masterGameStateNum;
		int viewableGameStateNum;
		
		//ensure there is only one writer for the duration of this instance of the Clock thread
		GameController.ViewableGS.getSemaphoreForWriting();
		NewViewable = new GameStateController();
		
		GameController.MasterGS.getSemaphoreForReading();
			masterGameStateNum = GameController.MasterGS.readGameState().gameStateNum;
		GameController.MasterGS.releaseSemaphoreForReading();
		GameController.ViewableGS.getSemaphoreForReading();
			viewableGameStateNum = GameController.ViewableGS.readGameState().gameStateNum;
			NewViewable.updateGameState(GameController.ViewableGS.readGameState());
		GameController.ViewableGS.releaseSemaphoreForReading();
		
    	try {    		
    		// Need to lag?
    		//create new game state unless viewable is more than 10 states ahead of the master
    		if (masterGameStateNum + 10 > viewableGameStateNum) {
    			GameController.Lagging = false;
    			createNewViewableGameState();	
    		}
    		else {
    			GameController.Lagging = true;
    		}
    		GameController.ViewableGS.releaseSemaphoreForWritng();
    		transmitCurrentViewable();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public static void createNewViewableGameState() throws InterruptedException{        	
    	Attack currentAttack = GameController.readCurrentAttack(); 
    	if(currentAttack != null){
    		NewViewable.addAttack(currentAttack);
    		GameController.writeCurrentAttack(null);  //reset current attack variable
    	}
    	
    	//determine which attacking minions have reached their target, 
    	//		modify hive status as appropriate, and remove those attacks 
    	//		from Viewable GameState   	
    	int currentState = NewViewable.readGameState().gameStateNum;
    	NewViewable.fastForward(currentState + 1);

    	determineIfGameOver();  
    	GameController.ViewableGS.getSemaphoreForReading();
    		GameController.ViewableGS.updateGameState(NewViewable.readGameState());
    	GameController.ViewableGS.releaseSemaphoreForReading();
    }
    
    public static void determineIfGameOver(){
    	
    	HashMap<Integer,Hive> hives = NewViewable.readGameState().hives;
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
	    	
	    	GameController.MasterGS.getSemaphoreForReading();
	    	// one player has lost all his hives
	    	// see if he has any minions left on the field
    		for(int i = 0; i < GameController.MasterGS.readGameState().attacks.size(); i++){
    			Attack attack = GameController.MasterGS.readGameState().attacks.get(i);
        		if(attack.player != leader){
        			gameOver = false; // the underdog has a minion left.  He just may capture another hive!
        		}
    		}
        	GameController.MasterGS.releaseSemaphoreForReading();    

    		if(gameOver){
    			//nope, he had no minions.  game over
	    		GameController.Winner = leader;
	    		GameController.GameFinished = gameOver;
    		}
    	}
    }
    
    
    public static void transmitCurrentViewable(){
    	GameController.ViewableGS.getSemaphoreForReading();
    	GameController.socket.sendMessage(
    			GameController.ViewableGS.readGameState(), 
    			GameController.remoteInetAddr, 
    			GameController.remotePort);
    	GameController.ViewableGS.releaseSemaphoreForReading();
    }
    
    
}
