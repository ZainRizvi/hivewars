package hivewars;

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
			createNewViewableGameState();
			
			if (GameController.lastRemoteClock > GameController.MasterGS.readGameState().gameStateNum){
				GameController.MasterGS.updateGameStateNum(GameController.ViewableGS.readGameState().gameStateNum);
			}
			
	    	transmitCurrentViewable();
	    	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public static void createNewViewableGameState() throws InterruptedException{
    	Attack currentAttack = GameController.readCurrentAttack();
    	GameController.ViewableGS.addAttack(currentAttack);    	
    	
    	//determine which attacking minions have reached their target, 
    	//		modify hive status as appropriate, and remove those attacks 
    	//		from Viewable GameState
    	int currentState = GameController.ViewableGS.readGameState().gameStateNum;
    	GameController.ViewableGS.fastForward(currentState + 1);
    	

    }
    
    public static void transmitCurrentViewable(){
    	GameController.socket.sendMessage(
    			GameController.ViewableGS, 
    			GameController.remoteInetAddr, 
    			GameController.remotePort);
    }
    
    
}