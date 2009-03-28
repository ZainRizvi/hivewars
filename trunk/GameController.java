package hivewars;

public class GameController {
	
	static GameStateData Master;
	static GameStateData Viewable;
	static GameStateData Incoming;
	static Attack Construction;
	static GameStateQueue History;
	
	static boolean GameStarted = false;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        //initialize reconcile
		//initialize everything
		//start Gui
		new Gui();
		//wait for GameStarted
		while(true){
			//call clock every 10hz
			new Clock();
			Thread.currentThread();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
