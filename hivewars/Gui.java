package hivewars;

import java.awt.Dimension;
import com.golden.gamedev.GameLoader;

//this class simply starts the Gui thread
public class Gui extends Thread {
	
	//constructor
	public Gui() {
		start();
	}

	public void run() {
        GameLoader game = new GameLoader();
        game.setup(new GoldenT(), new Dimension(800,600), true);
        //start:
        //		initResouces()
        //		while(true){
        //			update();
        //			render();
        //		}
        //plus other GTGE background stuff
        game.start();	
	}
	
    //main:
    //for testing  
    public static void main(String[] args) {
    	new Gui();
    }

}
