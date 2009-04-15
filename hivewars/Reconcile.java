package hivewars;

public class Reconcile{

	//
	//********* To be written by Zain ************//
	//
	public Reconcile() {
		// TODO Auto-generated constructor stub
	}
	
//	@Override
//	public void run() {
//		while(true){
//			//put thread to sleep using semaphores
//			//when Receive wakes up thread...
//			//reconcile master state with remote state
//		}
//	}
	
	// Adds new attacks in newGS to attack list in oldGS
	public static GameStateData ReconcileGS(GameStateController oldGS, GameStateData newGS){
		//forward oldGS to be at the same state number as new GS
		
		//store result in MasterGS
		return newGS;  
	}
	
	public static GameStateData ReconcileNewMoves(GameStateData GS){
		GameStateData newGS = new GameStateData(); 
		//merge a game state with PlayerAttackList commands 
		
		return newGS;
	}
	
	public static void updateAttacksOnHives(GameStateData gs, int tostate){

	}	
}
