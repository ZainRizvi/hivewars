package hivewars;

public class Reconcile implements Runnable{

	public Reconcile() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void run() {
		while(true){
			//put thread to sleep using semaphores
			//when Receive wakes up thread...
			//reconcile master state with remote state
		}
	}
	
	public GameStateData ReconcileGS(){
		GameStateData newGS = new GameStateData();
		//merge IncomingGS and MasterGS
		//store result in MasterGS
		return newGS;  
	}
	
	public GameStateData ReconcileNewMoves(GameStateData GS){
		GameStateData newGS = new GameStateData(); 
		//merge a game state with PlayerAttackList commands 
		
		return newGS;
	}
}
