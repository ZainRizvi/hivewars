package hivewars;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Hive implements Serializable{
	
	// Defined in HiveDefinition
	public final int Base;				//base number
	public final GameSettings.Control startingPlayer;
	public final int startingMinions;
	public final int spawnRate;   		//time (in 100 miliseconds) before new minion is generated 
	public final int hiveCapacity; 		//max number of minions this hive can contain
	public final int x;
	public final int y;

	// Variable
	GameSettings.Control controllingPlayer; 
	char numMinions; 
	int nextSpawnTime;			//game state time when next minion will be spawned
	
	
	public Hive(
			int id, 
			GameSettings.Control startingPlayer, 
			int startingMinions, 
			int spawnRate, 
			int maxSize, 
			int x, 
			int y){
		
		this.Base = id;
		this.startingPlayer = startingPlayer;
		this.controllingPlayer = startingPlayer;
		this.startingMinions = startingMinions;
		this.numMinions = (char) startingMinions;
		this.spawnRate = spawnRate;
		this.hiveCapacity = maxSize;
		this.x = x;
		this.y = y;
		
		//TODO: properly implement spawnTime initialization
		//spawnTime = (current state num) + spawnRate; 
	}
	


}
