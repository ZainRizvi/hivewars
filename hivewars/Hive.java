package hivewars;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Hive implements Serializable{
	
	// Defined in HiveDefinition
	public final int id;				//base number
	public final GameSettings.Control startingPlayer;
	public final int startingMinions;
	public final int spawnRate;   		//time (in 100 miliseconds) before new minion is generated 
	public final int hiveCapacity; 		//max number of minions this hive can contain
	public final int x;
	public final int y;

	// Variable
	GameSettings.Control controllingPlayer; 
	int numMinions; 
	int nextSpawnTime;			//game state time when next minion will be spawned
	
	
	public Hive(
			int id, 
			GameSettings.Control startingPlayer, 
			int startingMinions, 
			int spawnRate, 
			int maxSize, 
			int x, 
			int y){
		
		this.id = id;
		this.startingPlayer = startingPlayer;
		this.controllingPlayer = startingPlayer;
		this.startingMinions = startingMinions;
		this.numMinions = (char) startingMinions;
		this.spawnRate = spawnRate;
		this.nextSpawnTime = spawnRate;
		this.hiveCapacity = maxSize;
		this.x = x;
		this.y = y;
		
		//TODO: properly implement spawnTime initialization
		//spawnTime = (current state num) + spawnRate; 
	}
	
	public String toString(){
		String s = new String();
		
		s+= id + ":" + numMinions;
		
		return s;
	}
	


}
