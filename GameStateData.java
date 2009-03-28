package hivewars;

import java.io.Serializable;
import java.util.ArrayList;

public class GameStateData implements Serializable{
    
	ArrayList<Hive> hives;
	ArrayList<Attack> attacks;
	
	
	public GameStateData() {
		// TODO Auto-generated constructor stub
	}

}
