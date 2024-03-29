package hivewars;

//JFC
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.lang.Math;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import java.awt.GraphicsEnvironment.*;

// GTGE
import com.golden.gamedev.*;
import com.golden.gamedev.object.*;
import com.golden.gamedev.object.background.*;
import com.golden.gamedev.object.collision.*;
import com.golden.gamedev.object.font.BitmapFont;
import com.golden.gamedev.object.font.SystemFont;
import com.golden.gamedev.object.sprite.VolatileSprite;
import com.golden.gamedev.engine.BaseInput;
import com.golden.gamedev.engine.input.*;
import com.golden.gamedev.gui.*;
import com.golden.gamedev.gui.toolkit.*;


//********* Being worked on by Aaron ************//
//finals are all caps with underscores btw/ words
//gui variables are first word lower case, Capitalize first letter following words
//Spritegroups are capitalize first letter of first word
//local function variables are same as gui variables

public class GoldenT extends Game {

	final int NUMBER_OF_HIVES;
	SystemFont sf, label, err, ann, title;
	BufferedImage[] attck, expld, fireBall;
	//current gs, read from GameController.ViewableGS
	GameStateData currentGS;
	int oldGameStateNum;
	PlayField playfield;
	Background planetlogo, planet, supernova;
	Color c;
	ArrayList<MinionNumber> minionNumbers;
	int click1, click2;
	int selectedHive1, selectedHive2, sourceHive, destHive;
	int mode;
	//sprites
	ArrayList<VolatileSprite> explosions;
	ArrayList<AnimatedSprite> hives, attacks;
	SpriteGroup Hives, Attacks, Explosions, BigExplosions;
	//colisions
	CollisionBounds outOfBounds;
	CollisionGroup attackToHive;
	//GUI variables
	FrameWork frame;
	TTextField ipField, portField;
	TButton waitButton, connectToButton, connectButton, cancelWaitButton, cancelConnectButton;
	char userError;
	
	
	
	//constructor
	public GoldenT() {
		//read initial Viewable game state
		GameController.ViewableGS.getSemaphoreForReading();
		currentGS = GameController.ViewableGS.readGameState();
		GameController.ViewableGS.releaseSemaphoreForReading();
		NUMBER_OF_HIVES = currentGS.hives.size();
    	click1 = 1;
    	click2 = 2;
    	userError = 0;
    	oldGameStateNum = -1;
    	selectedHive1 = -1;
    	selectedHive2 = -1;
    	sourceHive = -1;
    	//determine starting mode
		//System.out.println("gui: " + GameController.Option + " " + GameController.arg0 + " " + GameController.arg1);
		if(GameController.Option != null){
	    	if(GameController.Option.equals("-m")){
	    		mode = 0;
	    	} else if (GameController.Option.equals("-w")){
	    		mode = 1;
	    	} else if (GameController.Option.equals("-c")){
	    		connect(GameController.arg0, GameController.arg1);
	    	} else {
	    		mode = 0;
	    	}
		} else {
			mode = 0;
		}
    	//System.out.println("mode: " + mode);
    	
	}
	
	
	
	//toggles flash screen, gtge logo in top right of screen, and fps in bottom left
	{ distribute = true; }

	
	
    //initResouces
	//this is the first function called by start(), it is only called once
	public void initResources() {
		
		showCursor();
		
		//set up gui pieces
		frame = new FrameWork(bsInput, 800, 600);
		ipField = new TTextField("", 350, 250, 100, 20) {
			public void doAction() {
				connect(ipField.getText(), portField.getText());
			}
		};
		ipField.setEditable(true);
		ipField.setMaxLength(15);
		ipField.setEnabled(false);
		ipField.setVisible(false);
		frame.add(ipField);
		portField = new TTextField("", 375, 300, 50, 20) {
			public void doAction() {
				connect(ipField.getText(), portField.getText());
			}
		};
		portField.setEditable(true);
		portField.setMaxLength(5);
		portField.setEnabled(false);
		portField.setVisible(false);
		frame.add(portField);
		waitButton = new TButton("Wait for opponent", 150, 250, 200, 50) {
			public void doAction() {
				mode = 1;
			}
		};
		frame.add(waitButton);
		connectToButton = new TButton("Connect to ...", 450, 250, 200, 50) {
			public void doAction() {
				mode = 3;
			}
		};
		frame.add(connectToButton);
		connectButton = new TButton("Connect", 425, 375, 200, 50) {
			public void doAction() {
				connect(ipField.getText(), portField.getText());
			}
		};
		connectButton.setEnabled(false);
		connectButton.setVisible(false);
		frame.add(connectButton);
		cancelWaitButton = new TButton("Cancel", 300, 400, 200, 50) {
			public void doAction() {
				mode = -2;
			}
		};
		cancelWaitButton.setEnabled(false);
		cancelWaitButton.setVisible(false);
		frame.add(cancelWaitButton);
		cancelConnectButton = new TButton("Cancel", 175, 375, 200, 50) {
			public void doAction() { 
				mode = -1;
			}
		};
		cancelConnectButton.setEnabled(false);
		cancelConnectButton.setVisible(false);
		frame.add(cancelConnectButton);
    	
    	//make background
    	planetlogo = new ImageBackground(getImage("PlanetSmallerLogo.jpg"),800,600);
    	planet = new ImageBackground(getImage("PlanetSmaller.jpg"),800,600);
    	supernova = new ImageBackground(getImage("SuperNova.jpg"),1024,768);
    	//initialize playfield
    	playfield = new PlayField(planetlogo);
    	
    	//set Mask color
    	c = new Color(254,255,252);
    	setMaskColor(c);
    	
    	//get images
    	BufferedImage lbb = getImage("MediumRedBall.gif", true);
    	BufferedImage lgb = getImage("MediumBlueBall.gif", true);
    	BufferedImage lsb = getImage("MediumBlackBall.gif", true);
    	//BufferedImage sob = getImage("greenBall.png", true);
    	BufferedImage bb = getImage("blueBall.png", true);
    	BufferedImage rb = getImage("redBall.png", true);
    	c = new Color(255,255,255);
    	setMaskColor(c);
    	//BufferedImage be0 = getImage("BubbleExplode0.gif", true);
    	//BufferedImage be1 = getImage("BubbleExplode1.gif", true);
     	//BufferedImage be2 = getImage("BubbleExplode2.gif", true);
     	//BufferedImage be3 = getImage("BubbleExplode3.gif", true);
    	//BufferedImage be4 = getImage("BubbleExplode4.gif", true);
    	BufferedImage fb1 = getImage("FireBall0.gif", true);
    	BufferedImage fb2 = getImage("FireBall1.gif", true);
    	BufferedImage fb3 = getImage("FireBall2.gif", true);
    	BufferedImage fb4 = getImage("FireBall3.gif", true);
    	BufferedImage[] h = {lbb, lgb, lsb};
    	attck = new BufferedImage[3];
    	attck[0] = bb;
    	attck[1] = bb;
    	attck[2] = rb;
    	expld = new BufferedImage[5];
    	//expld[0] = be0;
    	//expld[1] = be1;
    	//expld[2] = be2;
    	//expld[3] = be3;
    	//expld[4] = be4;
        fireBall = new BufferedImage[4];
        fireBall[0] = fb1;
        fireBall[1] = fb2;
        fireBall[2] = fb3;
        fireBall[3] = fb4;
    	//ColorModel cm = sob.getColorModel();
    	//int pixel = sob.getRGB(0, 0);
    	//System.out.println(cm.getRed(pixel) + " " + cm.getGreen(pixel) + " " + cm.getBlue(pixel));
    	
    	//set up Font
    	Font f = new Font("Helvetica", Font.PLAIN, 30);
    	sf = new SystemFont(f, Color.WHITE);
    	f = new Font("Helvetica", Font.PLAIN, 20);
    	label = new SystemFont(f, Color.RED);
    	f = new Font("Helvetica", Font.PLAIN, 30);
    	err = new SystemFont(f, Color.YELLOW);
    	f = new Font("Helvetica", Font.PLAIN, 30);
    	ann = new SystemFont(f, Color.RED);
    	f = new Font("Helvetica", Font.PLAIN, 100);
    	title = new SystemFont(f, Color.RED);
    	
    	//create sprites and spritegroups
    	Hives= new SpriteGroup("Hives");
    	hives = new ArrayList<AnimatedSprite>();
    	minionNumbers = new ArrayList<MinionNumber>();
    	for(int k = 0; k < NUMBER_OF_HIVES; k++){
    		int x = currentGS.hives.get(k).x;
    		int y = currentGS.hives.get(k).y;
    		//create sprite
    		hives.add(new AnimatedSprite(h, x - 41, y - 41));
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
    		MinionNumber mn = new MinionNumber(Color.WHITE, currentGS.hives.get(k).startingMinions, x, y);
    		minionNumbers.add(k, mn);
    		Hives.add(hives.get(k));
    	}
    	Hives.setActive(false);
    	
    	attacks = new ArrayList<AnimatedSprite>();
    	Attacks = new SpriteGroup("Attacks");
    	
    	explosions = new ArrayList<VolatileSprite>();
    	Explosions = new SpriteGroup("Explosions");
    	
    	BigExplosions = new SpriteGroup("BigExplosions");
    	
    	//add groups to playfield

    	playfield.addGroup(Hives);
    	playfield.addGroup(Attacks);
    	playfield.addGroup(Explosions);
    	playfield.addGroup(BigExplosions);

    	//set up collisions
    	outOfBounds = new OutOfBoundsCollision(supernova);
    	outOfBounds.setBoundary(0, 0, 800, 600);
    	outOfBounds.setCollisionGroup(Attacks, Attacks);
    	attackToHive = new AttackToHiveCollision(this);
    	attackToHive.setCollisionGroup(Attacks, Hives);
    	
    	GameController.init.release();
    }
	
	
    //this function determines the x and y velocity it takes to get to a destination x and y at a specified speed
	//inputs: current x and y, destination x and y, and velocity
	//output: an array of doubles {x velocity, y velocity}
	private double[] getSpeedTo(int xOld, int yOld ,int xNew, int yNew){
		double hyp, xv, yv;
		int xdist, ydist;
		double ret[] = {0, 0};
		xdist = xNew - xOld;
		ydist = yNew - yOld;
		hyp =  Math.sqrt(xdist*xdist + ydist*ydist);
		//xv = (int) ((xdist / hyp) * GameSettings.ATTACK_SPEED);
		//yv = (int) ((ydist / hyp) * GameSettings.ATTACK_SPEED);
		xv = (xdist * GameSettings.ATTACK_SPEED / hyp);
		yv = (ydist * GameSettings.ATTACK_SPEED / hyp);
		ret[0] = xv;
		ret[1] = yv;
		return ret;
	}
	
	
	
	//this functions sets the remote ip and port in GameController
	//inputs string ip and string port
	private void connect(String ip, String port){
		String p0 = new String();
		String p1 = new String();
		String p2 = new String();
		String p3 = new String();
		//check for valid input
		if(ip == null || port == null){
			userError = 1;
			mode = 3;
		} else if(ip.length() == 0 && port.length() == 0){
			userError = 1;		//please enter an ip address and a port number
		} else if(ip.length() == 0){
			userError = 2;		//please enter an ip address
		} else if(port.length() == 0){
			userError = 3;		//please enter a port number
		} else {
			GameController.remotePort = Integer.parseInt(port);
			//parse ip address from string
			int k = 0;
			while((ip.charAt(k) != '.') && (k < (ip.length()))){
				p0 += ip.charAt(k);
				k++;
			}
			k++;
			while((ip.charAt(k) != '.') && (k < (ip.length()))){
				p1 += ip.charAt(k);
				k++;
			}
			k++;
			while((ip.charAt(k) != '.') && (k < (ip.length()))){
				p2 += ip.charAt(k);
				k++;
			}
			k++;
			while(k < (ip.length())){
				p3 += ip.charAt(k);
				k++;
			}
			byte[] addr = new byte[4];
			addr[0] = (byte)Integer.parseInt(p0);
			addr[1] = (byte)Integer.parseInt(p1);
			addr[2] = (byte)Integer.parseInt(p2);
			addr[3] = (byte)Integer.parseInt(p3);
			try {
				GameController.remoteInetAddr = InetAddress.getByAddress(addr);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			GameController.Me = GameSettings.Control.PlayerB;
			sourceHive = 9;
			mode = 5;
			userError = 0;
		}
		//*return error if cannot connect*
	}
	

	
	//update
	//this function is called over and over again by the GTG engine
    public void update(long elapsedTime) {
    	//local variables
    	int startFrame;

    	if(mode == -2){
    		playfield.setBackground(planetlogo);
    		cancelWaitButton.setEnabled(false);
    		cancelWaitButton.setVisible(false);
    		waitButton.setEnabled(true);
    		waitButton.setVisible(true);
    		connectToButton.setEnabled(true);
    		connectToButton.setVisible(true);
    		frame.update();
    		mode = 0;
    	} else if(mode == -1){
    		playfield.setBackground(planetlogo);
    		waitButton.setEnabled(true);
    		waitButton.setVisible(true);
    		connectToButton.setEnabled(true);
    		connectToButton.setVisible(true);
    		ipField.setEnabled(false);
    		ipField.setVisible(false);
    		portField.setEnabled(false);
    		portField.setVisible(false);
    		connectButton.setEnabled(false);
    		connectButton.setVisible(false);
    		cancelConnectButton.setEnabled(false);
    		cancelConnectButton.setVisible(false);
    		frame.update();
    		mode = 0;
    	} else if(mode == 0){
        	frame.update();
    	} else if(mode == 1) {
    		playfield.setBackground(planet);
    		waitButton.setEnabled(false);
    		waitButton.setVisible(false);
    		connectToButton.setEnabled(false);
    		connectToButton.setVisible(false);
    		cancelWaitButton.setEnabled(true);
    		cancelWaitButton.setVisible(true);
    		mode = 2;
        	frame.update();
    	} else if(mode == 2){
    		if(GameController.GameStarted){
    			GameController.Me = GameSettings.Control.PlayerA;
    			sourceHive = 0;
    			mode = 5;
    		}
        	frame.update();
    	} else if(mode == 3) {
    		playfield.setBackground(planet);
    		waitButton.setEnabled(false);
    		waitButton.setVisible(false);
    		connectToButton.setEnabled(false);
    		connectToButton.setVisible(false);
    		ipField.setEnabled(true);
    		ipField.setVisible(true);
    		portField.setEnabled(true);
    		portField.setVisible(true);
    		connectButton.setEnabled(true);
    		connectButton.setVisible(true);
    		cancelConnectButton.setEnabled(true);
    		cancelConnectButton.setVisible(true);
    		mode = 4;
        	frame.update();
    	} else if (mode == 4){
        	frame.update();
    	} else if (mode == 5){
    		playfield.setBackground(supernova);
			cancelWaitButton.dispose();
			waitButton.dispose();
			connectToButton.dispose();
			ipField.dispose();
			portField.dispose();
			connectButton.dispose();
			cancelConnectButton.dispose();
			frame.update();
    		Hives.setActive(true);
    		mode = 6;
    	} else if(mode == 6){
    		////System.out.println(GameController.Me);
    		////System.out.println(GameController.GameStarted);
	    	//update currentGS from ViewableGS
	    	GameController.ViewableGS.getSemaphoreForReading();
    		currentGS = GameController.ViewableGS.readGameState();
    		GameController.ViewableGS.releaseSemaphoreForReading();
	    	if(currentGS.gameStateNum == oldGameStateNum){
				Thread.currentThread();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {e.printStackTrace();}
	    	} else {
		    	//update hives
	    		BigExplosions.removeInactiveSprites();
		    	for(int k = 0; k < hives.size(); k ++){
		    		//update minion number
		    		minionNumbers.get(k).setNumber(currentGS.hives.get(k).numMinions);
		    		//update hive color
		    		startFrame = hives.get(k).getStartAnimationFrame();
		    		if(currentGS.hives.get(k).controllingPlayer == GameSettings.Control.PlayerA) {
		    			if(hives.get(k).getFrame() != 0){
		    				if(sourceHive == k) {
			    				click1 = 1;
			    				sourceHive = -1;
		    				}
		    			}
		    			hives.get(k).setAnimationFrame(0, 2);
		    		} else if(currentGS.hives.get(k).controllingPlayer == GameSettings.Control.PlayerB) {
		    			if(hives.get(k).getFrame() != 1){
		    				if(sourceHive == k) {
			    				click1 = 1;
			    				sourceHive = -1;
		    				}
		    			}
		    			hives.get(k).setAnimationFrame(1, 2);
		    		} else if(currentGS.hives.get(k).controllingPlayer == GameSettings.Control.Neutral) {
		    			if(hives.get(k).getFrame() != 2){
		    				Timer t = new Timer(100);
		    				VolatileSprite v = new VolatileSprite(fireBall, hives.get(k).getX() - 70, 
		    						hives.get(k).getY() - 70);
		    				v.setAnimationTimer(t);
		    				BigExplosions.add(v);
		    			}
		    			hives.get(k).setAnimationFrame(2, 2);
		    		}
		    		if(hives.get(k).getStartAnimationFrame() != startFrame) {
		    			//*explosion*
		    		}
		    	}
		    	
		    	//update attacks
		    	//add or subtract correct number of attack sprites to sprite group
		    	while(currentGS.attacks.size() < attacks.size()) {
		    		attacks.get(attacks.size() - 1).setActive(false);
		    		attacks.remove(attacks.size() - 1);
		    		Attacks.removeInactiveSprites();
		    		Explosions.removeInactiveSprites();
		    		for(int k = 0; k < Explosions.getSize(); k++){
		    		    int size = explosions.size();
		    		    if(size > 0){
			    			explosions.remove(explosions.size() - 1);
		    		    }
		    		}
		    	}
		    	while(currentGS.attacks.size() > attacks.size()) {
		    		AnimatedSprite a = new AnimatedSprite(attck, 100, 100);
		    		a.setAnimationFrame(0, 0);
		    		a.setAnimate(false);
		    		a.setActive(true);
		    		attacks.add(a);
		    		Attacks.add(a);
		    	}
		    	//change attack locations on the screen
		    	for(int k = 0; k < attacks.size(); k ++) {
		    		int sourceX = currentGS.hives.get(currentGS.attacks.get(k).sourceHiveNum).x - 12;
		    		int sourceY = currentGS.hives.get(currentGS.attacks.get(k).sourceHiveNum).y - 12;
		    		int destX = currentGS.hives.get(currentGS.attacks.get(k).destHiveNum).x - 12;
		    		int destY = currentGS.hives.get(currentGS.attacks.get(k).destHiveNum).y - 12;
		    		double[] v = getSpeedTo(sourceX, sourceY, destX, destY);
		    		//x = sourceHive.x + traveledDistance
		    		//or x = sourceHive.x + (currentTime - firingTime) * xSpeed
		    		double x = sourceX + (currentGS.gameStateNum - currentGS.attacks.get(k).firingTime) * v[0];
		    		double y = sourceY + (currentGS.gameStateNum - currentGS.attacks.get(k).firingTime) * v[1];
		    		attacks.get(k).forceX(x);
		    		attacks.get(k).forceY(y);
		    		if(currentGS.attacks.get(k).player == GameSettings.Control.PlayerB) {
		    			attacks.get(k).setAnimationFrame(1, 1);
		    		} else {
		    			attacks.get(k).setAnimationFrame(2, 2);
		    		}
		    		////System.out.println("sourceX,Y: " + sourceX + "," + sourceY + " destX,Y: " + destX + "," + destY +
		    			//	" xv,yv: " + v[0] + "," + v[1] + " currX,Y: " + x + "," + y);
		    		//*set animations*
		    	}
		    	
		    	//check boundary collisions
		    	//outOfBounds.checkCollision();
		    	//attackToHive.checkCollision();
		    	
	    	}
	    	
	    	//check for mouse event
	    	if(bsInput.isMouseDown(MouseEvent.BUTTON1)) {
	    		int xm = getMouseX();
	    		int ym = getMouseY();
	    		selectedHive1 = -1;
	    		for(int k = 0; k < hives.size(); k ++){	
					if(xm >= hives.get(k).getX() && xm <= hives.get(k).getX() + hives.get(k).getWidth() &&
	    			   ym >= hives.get(k).getY() && ym <= hives.get(k).getY() + hives.get(k).getHeight()){
						selectedHive1 = k;
	    			}
	    		}  	
	    		if (click1 == 2) {
	    			if(selectedHive1 == -1) {
	    				click1 = 1;
	    			} else if(sourceHive != -1){
	    				click1 = 1;
	    				destHive = selectedHive1;
						try {
							if((currentGS.hives.get(sourceHive).numMinions - 1) != 0){
								GameController.writeCurrentAttack(new Attack(GameController.Me, 
										(char) sourceHive, (char) destHive, (short) currentGS.gameStateNum));
							}
								//System.out.println("new attack: source: " + sourceHive + " dest: " + destHive + " stateNum: " + currentGS.gameStateNum);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	    			} else {
	    				click1 = 1;
	    			}
	    		} else { //click1 = 1;
    				// do nothing
    			}
	    	}
	    	
	    	if(!bsInput.isMouseDown(MouseEvent.BUTTON1)){
	    		if(click1 == 1){
	    			click1 = 2;
	    		}
	    	}
	    	
	    	//check for mouse event
	    	if(bsInput.isMouseDown(MouseEvent.BUTTON3)) {
	    		int xm = getMouseX();
	    		int ym = getMouseY();
	    		selectedHive2 = -1;
	    		for(int k = 0; k < hives.size(); k ++){	
					if(xm >= hives.get(k).getX() && xm <= hives.get(k).getX() + hives.get(k).getWidth() &&
	    			   ym >= hives.get(k).getY() && ym <= hives.get(k).getY() + hives.get(k).getHeight()){
						selectedHive2 = k;
	    			}
	    		}  	
	    		if (click2 == 2) {
	    			if(selectedHive2 == -1) {
	    				click2 = 1;
	    			} else if(currentGS.hives.get(selectedHive2).controllingPlayer == GameController.Me){
	    				click2 = 1;
	    				sourceHive = selectedHive2;	
	    				//System.out.println("click2: " + click2 + " sourceHive: " + sourceHive);
	    				//*make sourceHive a special selected color*
	    			} 
	    		}
	    	}
	    	
	    	if(!bsInput.isMouseDown(MouseEvent.BUTTON3)){
	    		if(click2 == 1){
	    			click2 = 2;
	    		}
	    	}
	    	
	    	//*tell sprites which way to face when they are walking*
    		
	    	if(GameController.GameFinished){
    			mode = 7;
    			//System.out.println("Game finished!!");
    		}
    	} else {	//mode == 7
    		//System.out.println("Game finished!!");
    	}
    	
    	//update playfield
    	playfield.update(elapsedTime);
    }

    //render playfield
    public void render(Graphics2D g) {
    	playfield.render(g);
    	if(mode == 0){
    		//display hive wars
    		//title.drawString(g, "HiveWars", 400 - title.getWidth("HiveWars") / 2, 200 - title.getHeight());
        	frame.render(g);
    	} else if(mode == 2){
    		//display waiting
    		ann.setColor(Color.CYAN);
    		ann.drawString(g, "Waiting ...", 400 - ann.getWidth("Waiting ...") / 2, 250 - ann.getHeight());
        	ann.drawString(g, "Local Port: " + GameController.localPort, 
        			400 - ann.getWidth("Local Port: " + GameController.localPort) / 2, 350 - ann.getHeight());
        	ann.drawString(g, "Local IP Address: " + GameController.localInetAddr.toString(), 
        			400 - ann.getWidth("Local IP Address: " + GameController.localInetAddr.toString()) / 2, 
        			300 - ann.getHeight());
    		frame.render(g);
    	} if(mode == 4){
    		label.setColor(Color.CYAN);
    		label.drawString(g, "IP: ", 350 - label.getWidth("IP: "), 250);
    		label.drawString(g, "Port: ", 375 - label.getWidth("Port: "), 300);
        	if(userError == 1){
        		err.drawString(g, "Please enter an IP address and Port number", 
        				400 - err.getWidth("Please enter an IP address and Port number") / 2, 500);
        	} else if (userError == 2){
        		err.drawString(g, "Please enter an IP address", 
        				400 - err.getWidth("Please enter an IP address") / 2, 500);
        	} else if (userError ==3){
        		err.drawString(g, "Please enter a Port number", 
        				400 - err.getWidth("Please enter a Port number") / 2, 500);
        	}
        	frame.render(g);
    	} else if(mode == 6){
        	for(int k = 0; k < minionNumbers.size(); k++){
        		if(currentGS.hives.get(k).numMinions == currentGS.hives.get(k).hiveCapacity){
        			c = new Color(47, 186, 237);
        			sf.setColor(c);
        		} else {
            		sf.setColor(minionNumbers.get(k).c);
        		}
            	sf.drawString(g, minionNumbers.get(k).Minions, GameFont.CENTER, minionNumbers.get(k).x, 
            			minionNumbers.get(k).y, sf.getWidth('1') * minionNumbers.get(k).Minions.length());
        	}
        	if(GameController.Lagging == true){
        		ann.setColor(Color.YELLOW);
        		ann.drawString(g, "Waiting for opponent ...", 
        				400 - ann.getWidth("Waiting for opponent ...") / 2, 500);
        	}
    	} else if(mode == 7){
    		InetAddress winner;
    		c = new Color(42, 23, 122);
    		title.setColor(c);
    		title.drawString(g, "GameOver", 402 - title.getWidth("GameOver") / 2, 198 - title.getHeight());
    		title.drawString(g, "GameOver", 398 - title.getWidth("GameOver") / 2, 202 - title.getHeight());
    		title.drawString(g, "GameOver", 402 - title.getWidth("GameOver") / 2, 202 - title.getHeight());
    		title.drawString(g, "GameOver", 398 - title.getWidth("GameOver") / 2, 198 - title.getHeight());
    		title.setColor(Color.WHITE);
    		title.drawString(g, "GameOver", 400 - title.getWidth("GameOver") / 2, 200 - title.getHeight());
    		if(GameController.Winner == GameController.Me){
    			winner = GameController.localInetAddr;
    		} else {
    			winner = GameController.remoteInetAddr;
    		}
    		c = new Color(43, 23, 142);
    		err.setColor(c);
    		err.drawString(g, winner.toString() + " wins!", 
    				401 - err.getWidth(winner.toString() + " wins!") / 2, 199 + 10);
    		err.drawString(g, winner.toString() + " wins!", 
    				399 - err.getWidth(winner.toString() + " wins!") / 2, 201 + 10);
    		err.drawString(g, winner.toString() + " wins!", 
    				401 - err.getWidth(winner.toString() + " wins!") / 2, 201 + 10);
    		err.drawString(g, winner.toString() + " wins!", 
    				399 - err.getWidth(winner.toString() + " wins!") / 2, 199 + 10);
    		err.setColor(Color.WHITE);
    		err.drawString(g, winner.toString() + " wins!", 
    				400 - err.getWidth(winner.toString() + " wins!") / 2, 200 + 10);
        	frame.render(g);
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
