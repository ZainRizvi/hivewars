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
	
	public GameStateData ReconcileGS(GameStateData localState, GameStateData remoteState){
		GameStateData newGS = new GameStateData();
		return newGS;  
	}

}
