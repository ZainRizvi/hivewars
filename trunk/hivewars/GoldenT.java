package hivewars;

//JFC
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.lang.Math;

// GTGE
import com.golden.gamedev.*;
import com.golden.gamedev.object.*;
import com.golden.gamedev.object.background.*;
import com.golden.gamedev.object.collision.*;
import com.golden.gamedev.engine.input.*;

public class GoldenT extends Game {

	//gui variables
	PlayField playfield;
	Background bg;
	Color c;
	
	//constructor
	public GoldenT() {
		
	}
	
	//toggles flash screen, gtge logo in top right of screen, and fps in bottom left
	{ distribute = false; }
	
    //initResouces
	public void initResources() {
    	
		showCursor();
    	
    	//make background
    	bg = new ImageBackground(getImage("SuperNova.jpg"),1024,768);
    	//initialize playfield
    	playfield = new PlayField(bg);
    	
    	//set Mask color to light blue
    	c = new Color(166,202,240);
    	setMaskColor(c);
    	
    	//get images
    	
    	//create sprites and spritegroups
    	
    	//add groups to playfield
    	
   		//set up collisions
    	
    }
	
	//update
    public void update(long elapsedTime) {
    	
    	//local variables
    	
    	//check for mouse event
    	
    	//tell sprites which way to face when they are walking
    	
    	//check collisons, boundary and sprite to sprite
    	
    	//update playfield
    	
    }

    //render playfield
    public void render(Graphics2D g) {
    	playfield.render(g);
    }

    //main:
    //for testing  
    public static void main(String[] args) {
        GameLoader game = new GameLoader();
        game.setup(new GoldenT(), new Dimension(1024,768), false);
        game.start();
    }

}
