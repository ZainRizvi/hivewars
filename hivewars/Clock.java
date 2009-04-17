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
			
			GameController.MasterGS.getSemaphore();
			if (GameController.lastRemoteClock > GameController.MasterGS.readGameState().gameStateNum){
				GameController.MasterGS.fastForward(GameController.ViewableGS.readGameState().gameStateNum);
			}
			GameController.MasterGS.releaseSemaphore();
			
	    	transmitCurrentViewable();
	    	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public static void createNewViewableGameState() throws InterruptedException{
    	Attack currentAttack = GameController.readCurrentAttack();
    	
    	GameController.ViewableGS.getSemaphore();
    	
    	GameController.ViewableGS.addAttack(currentAttack);    	
    	
    	//determine which attacking minions have reached their target, 
    	//		modify hive status as appropriate, and remove those attacks 
    	//		from Viewable GameState
    	int currentState = GameController.ViewableGS.readGameState().gameStateNum;
    	GameController.ViewableGS.fastForward(currentState + 1);
    	
    	GameController.ViewableGS.releaseSemaphore();
    }
    
    public static void transmitCurrentViewable(){
    	GameController.socket.sendMessage(
    			GameController.ViewableGS.readGameState(), 
    			GameController.remoteInetAddr, 
    			GameController.remotePort);
    }
    
    
}
