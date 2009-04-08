package hivewars;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Hive implements Serializable{
	
	char Base; 				//base number
	char controllingPlayer; 
	char numMinions; 		//max 250 allowed in a hive

	int spawnRate;   		//time (in 100 miliseconds) before new minion is generated  
	int hiveCapacity; 		//max number of minions this hive can contain
	
	int spawnTime;			//game state time when next minion will be spawned
	
	public Hive(char baseNum, char controller, char minionCount, int spawningRate, int capacity) {
		// TODO Auto-generated constructor stub
	}

}
