package hivewars;

//JFC
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.lang.Math;
import java.util.ArrayList;

import java.awt.GraphicsEnvironment.*;

// GTGE
import com.golden.gamedev.*;
import com.golden.gamedev.object.*;
import com.golden.gamedev.object.background.*;
import com.golden.gamedev.object.collision.*;
import com.golden.gamedev.object.font.BitmapFont;
import com.golden.gamedev.object.font.SystemFont;
import com.golden.gamedev.engine.input.*;

//********* Being worked on by Aaron ************//
//finals are all caps with underscores btw/ words
//gui variables are first word lower case, Capitalize first letter following words
//Spritegroups are capitalize first letter of first word
//local function variables are same as gui variables

public class GoldenT extends Game {

	final int NUMBER_OF_HIVES;
	final int ATTACK_SPEED = 12;  //five seconds to go from one side of the screen to the other
	SystemFont sf;
	BufferedImage[] attck;
	//current gs, read from GameController.ViewableGS
	GameStateData currentGS;
	PlayField playfield;
	Background bg;
	Color c;
	ArrayList<MinionNumber> minionNumbers;
	int click;
	int selectedHive, sourceHive, destHive;
	//sprites
	ArrayList<AnimatedSprite> hives, attacks;
	SpriteGroup Hives, Attacks;
	//colisions
	CollisionBounds outOfBounds;
	CollisionGroup attackToHive;
	
	//constructor
	public GoldenT() {
		//read initial Viewable game state
		currentGS = GameController.ViewableGS.readGameState();
		NUMBER_OF_HIVES = currentGS.hives.size();
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
    	c = new Color(254,255,252);
    	setMaskColor(c);
    	
    	//get images
    	BufferedImage lbb = getImage("LargeBlueBall.gif", true);
    	BufferedImage lgb = getImage("LargeGreenBall.gif", true);
    	BufferedImage lsb = getImage("LargeSteelBall.gif", true);
    	BufferedImage sob = getImage("SmallOrangeBall.gif", true);
    	BufferedImage[] h = {lbb, lgb, lsb};
    	attck = new BufferedImage[1];
    	attck[0] = sob;
    	
    	//ColorModel cm = sob.getColorModel();
    	//int pixel = sob.getRGB(0, 0);
    	//System.out.println(cm.getRed(pixel) + " " + cm.getGreen(pixel) + " " + cm.getBlue(pixel));
    	
    	//set up Font
    	Font f = new Font("Helvetica", Font.PLAIN, 30);
    	sf = new SystemFont(f, Color.WHITE);
    	
    	//create sprites and spritegroups
    	Hives= new SpriteGroup("Hives");
    	hives = new ArrayList<AnimatedSprite>();
    	minionNumbers = new ArrayList<MinionNumber>();
    	for(int k = 0; k < NUMBER_OF_HIVES; k++){
    		int x = currentGS.hives.get(k).x;
    		int y = currentGS.hives.get(k).y;
    		//create sprite
    		hives.add(new AnimatedSprite(h, x, y));
    		//set initial color
    		if(currentGS.hives.get(k).startingPlayer == GameSettings.Control.PlayerA) {
    			hives.get(k).setAnimationFrame(0, 2);
    		} else if(currentGS.hives.get(k).startingPlayer == GameSettings.Control.PlayerB) {
    			hives.get(k).setAnimationFrame(1, 2);
    		} else if(currentGS.hives.get(k).startingPlayer == GameSettings.Control.Neutral) {
    			hives.get(k).setAnimationFrame(2, 2);
    		}
    		//set initial minion number
    		c = new Color(255, 160, 39);
    		MinionNumber mn = new MinionNumber(Color.WHITE, currentGS.hives.get(k).startingMinions, x + 70, y + 60);
    		minionNumbers.add(k, mn);
    		Hives.add(hives.get(k));
    	}
    	
    	attacks = new ArrayList<AnimatedSprite>();
    	Attacks = new SpriteGroup("Attacks");
    	
    	//add groups to playfield
    	playfield.addGroup(Hives);
    	playfield.addGroup(Attacks);

    	//set up collisions
    	outOfBounds = new OutOfBoundsCollision(bg);
    	outOfBounds.setBoundary(0, 0, 800, 600);
    	outOfBounds.setCollisionGroup(Attacks, Attacks);
    	//attackToHive = new KoopaToKoopaCollision(fireball[0]);
    	//attackToHive.setCollisionGroup(Attacks, Attacks);
    	
    	//start game
    	GameController.GameStarted = true;
    	click = 0;
    }
	
	
    //this function determines the x and y velocity it takes to get to a destination x and y at a specified speed
	//inputs: current x and y, destination x and y, and velocity
	//output: an array of doubles {x velocity, y velocity}
	public int[] getSpeedTo(int xOld, int yOld ,int xNew, int yNew){
		double hyp;
		int xv, yv, xdist, ydist;
		int ret[] = {0, 0};
		xdist = xNew - xOld;
		ydist = yNew - yOld;
		hyp =  Math.sqrt(xdist*xdist + ydist*ydist);
		xv = (int) ((xdist / hyp) * ATTACK_SPEED);
		yv = (int) ((ydist / hyp) * ATTACK_SPEED);
		ret[0] = xv;
		ret[1] = yv;
		return ret;
	}
	
	
	//update
    public void update(long elapsedTime) {
    	
    	//local variables
    	int startFrame;
    	
    	//update currentGS from ViewableGS
    	currentGS = GameController.ViewableGS.readGameState();
    	
    	//update hives
    	for(int k = 0; k < hives.size(); k ++){
    		//update minion number
    		minionNumbers.get(k).setNumber(currentGS.hives.get(k).numMinions);
    		//update hive color
    		startFrame = hives.get(k).getStartAnimationFrame();
    		if(currentGS.hives.get(k).controllingPlayer == GameSettings.Control.PlayerA) {
    			hives.get(k).setAnimationFrame(0, 2);
    		} else if(currentGS.hives.get(k).controllingPlayer == GameSettings.Control.PlayerB) {
    			hives.get(k).setAnimationFrame(1, 2);
    		} else if(currentGS.hives.get(k).controllingPlayer == GameSettings.Control.Neutral) {
    			hives.get(k).setAnimationFrame(2, 2);
    		}
    		if(hives.get(k).getStartAnimationFrame() != startFrame) {
    			//*explosion*
    		}
    	}
    	
    	//update attacks
    	//add or subtract correct number of attack sprites to sprite group
    	while(currentGS.attacks.size() < attacks.size()) {
    		attacks.remove(attacks.size() - 1);
    	}
    	while(currentGS.attacks.size() > attacks.size()) {
    		attacks.add(new AnimatedSprite(attck));
    	}
    	for(int k = 0; k < attacks.size(); k ++) {
    		int sourceX = currentGS.hives.get(currentGS.attacks.get(k).sourceHiveNum).x;
    		int sourceY = currentGS.hives.get(currentGS.attacks.get(k).sourceHiveNum).y;
    		int[] v = getSpeedTo(sourceX, sourceY, currentGS.hives.get(currentGS.attacks.get(k).destHiveNum).x,
    				currentGS.hives.get(currentGS.attacks.get(k).destHiveNum).y);
    		//x = sourceHive.x + traveledDistance
    		//or x = sourceHive.x + (currentTime - firingTime) * xSpeed
    		int x = (currentGS.hives.get(currentGS.attacks.get(k).sourceHiveNum).x + 60) + 
    		(currentGS.gameStateNum - currentGS.attacks.get(k).firingTime) * v[0];
    		int y = (currentGS.hives.get(currentGS.attacks.get(k).sourceHiveNum).y + 60) + 
    		(currentGS.gameStateNum - currentGS.attacks.get(k).firingTime) * v[1];
    		attacks.get(k).forceX(x);
    		attacks.get(k).forceY(y);
    		//*set animations*
    	}
    	
    	//check for mouse event
    	if(bsInput.isMouseDown(MouseEvent.BUTTON1)) {
    		int xm = getMouseX();
    		int ym = getMouseY();
    		for(int k = 0; k < hives.size(); k ++){	
				if(xm >= hives.get(k).getX() && xm <= hives.get(k).getX() + hives.get(k).getWidth() &&
    			   ym >= hives.get(k).getY() && ym <= hives.get(k).getY() + hives.get(k).getHeight()){
					selectedHive = k;
					click = 1;
    			}
    		}  	
    		
    		//if (click == 2){
    			//xySpeed = getSpeedTo(sprites[selected].getX(), sprites[selected].getY(), xm, ym, 0.1);
    			//sprites[selected].setSpeed(xySpeed[0], xySpeed[1]);
    		//}
    	}
    	
    	if(!bsInput.isMouseDown(MouseEvent.BUTTON1)){
    		if(click == 1){
    			click = 2;
    		}
    	}
    	//tell sprites which way to face when they are walking

    	//check collisons, boundary and sprite to sprite
    	
    	//update playfield
    	playfield.update(elapsedTime);
    }

    //render playfield
    public void render(Graphics2D g) {
    	playfield.render(g);
    	for(int k = 0; k < minionNumbers.size(); k++){
    		sf.setColor(minionNumbers.get(k).c);
        	sf.drawString(g, minionNumbers.get(k).Minions, GameFont.CENTER, minionNumbers.get(k).x, minionNumbers.get(k).y, 
        			sf.getWidth('1') * minionNumbers.get(k).Minions.length());
    	}
    }

    //main:
    //for testing  
    public static void main(String[] args) {
        GameLoader game = new GameLoader();
        game.setup(new GoldenT(), new Dimension(1024,768), false);
        game.start();
    }

}
