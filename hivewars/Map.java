package hivewars;

import java.util.ArrayList;

public class Map {
	
	public ArrayList<HiveDefinition> hives;
	

	public Map() {
		hives.add(new HiveDefinition(0, 1, 1, 1, 1));
		hives.add(new HiveDefinition(0, 1, 1, 1, 100));
		hives.add(new HiveDefinition(0, 1, 1, 50, 50));		
		
		calculateDistances();		
	}

	
	private void calculateDistances(){
		
	}
}
