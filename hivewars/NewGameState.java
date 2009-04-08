package hivewars;

public class NewGameState implements Runnable {

	public NewGameState() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
	
	//
	//********* To be written by Zain ************//
	//
	// TODO: Figure out why there are two Reocncile functions
	//  	 (this function and the class) and get rid of 
	//  	 one!
	public GameStateData Reconcile(GameStateData localState, GameStateData remoteState){
		GameStateData newGS = new GameStateData();
		return newGS;  
	}
	
	public void AddNewEvents(GameStateData masterState ){
		
	}

}
