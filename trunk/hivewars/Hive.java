package hivewars;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Hive implements Serializable{
	
	char Base; 				//base number
	GameSettings.Control controllingPlayer; 
	char numMinions; 		//max 250 allowed in a hive

	int spawnRate;   		//time (in 100 miliseconds) before new minion is generated  
	int spawnTime;			//game state time when next minion will be spawned
	int hiveCapacity; 		//max number of minions this hive can contain
	
	
	public Hive(
			char baseNum, 
			GameSettings.Control controller, 
			char minionCount, 
			int spawningRate, 
			int capacity) {
		Base = baseNum;
		controllingPlayer = controller;
		numMinions = minionCount;
		spawnRate = spawningRate;
		hiveCapacity = capacity;
		//TODO: properly implement spawnTime initialization
		//spawnTime = (current state num) + spawnRate; 
	}
	
	// Default hive stats (this constructor is for debugging purposes only)
	public Hive(char baseNum){
		Base = baseNum;
		controllingPlayer = GameSettings.Control.Neutral;
		numMinions = 10;
		spawnRate = 10;
		hiveCapacity = 20;
		//TODO: properly implement spawnTime initialization
		//spawnTime = (current state num) + spawnRate;
	}

}
