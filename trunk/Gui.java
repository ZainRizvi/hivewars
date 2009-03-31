package hivewars;

//JFC
import java.awt.Graphics2D;
import java.awt.Dimension;

// GTGE
import com.golden.gamedev.Game;
import com.golden.gamedev.GameLoader;


public class Gui extends Game implements Runnable {


 /****************************************************************************/
 /**************************** GAME SKELETON *********************************/
 /****************************************************************************/

    public void initResources() {
    }

    public void update(long elapsedTime) {
    }

    public void render(Graphics2D g) {
    }


 /****************************************************************************/
 /***************************** START-POINT **********************************/
 /****************************************************************************/

    public void run(){
        GameLoader game = new GameLoader();
        game.setup(new Gui(), new Dimension(1280,800), true);
        game.start();	
    }
    
    public static void main(String[] args) {
        GameLoader game = new GameLoader();
        game.setup(new Gui(), new Dimension(1280,800), true);
        game.start();
    }

}
