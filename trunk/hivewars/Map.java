package hivewars;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {
	
	public static HashMap<Integer,Hive> hives = new HashMap<Integer,Hive>();
	public static int[][] distances;
	

	public Map() {
		
		// Original
		hives.put(0,new Hive(0, GameSettings.Control.PlayerA, 10, 10, 20, 50, 50));
		hives.put(1,new Hive(1, GameSettings.Control.Neutral, 5, 10, 10, 50, 550));
		hives.put(2,new Hive(2, GameSettings.Control.Neutral, 5, 10, 10, 200, 300));
		hives.put(3,new Hive(3, GameSettings.Control.Neutral, 5, 10, 10, 200, 450));
		hives.put(4,new Hive(4, GameSettings.Control.Neutral, 10, 10, 20, 425, 225));
		hives.put(5,new Hive(5, GameSettings.Control.Neutral, 10, 10, 20, 350, 375));
		hives.put(6,new Hive(6, GameSettings.Control.Neutral, 5, 10, 10, 750, 50));
		hives.put(7,new Hive(7, GameSettings.Control.Neutral, 5, 10, 10, 600, 150));
		hives.put(8,new Hive(8, GameSettings.Control.Neutral, 5, 10, 10, 600, 300));
		hives.put(9,new Hive(9, GameSettings.Control.PlayerB, 10, 10, 20, 750, 550));
		
		
		
		// Debug
		/*
		hives.put(0,new Hive(0, GameSettings.Control.PlayerA, 10, 10, 20, 50, 50));
		hives.put(1,new Hive(1, GameSettings.Control.Neutral, 5, 10, 10, 100, 100));
		hives.put(2,new Hive(2, GameSettings.Control.PlayerB, 10, 10, 20, 150, 150));
		*/
		//calculateDistances();		
	}

	
	private void calculateDistances(){
		distances = new int[hives.size()][hives.size()];
		
		for (int i = 0; i < hives.size(); i++){
			for (int j = 0; j < hives.size(); j++){
				if (i==j){
					distances[i][j] = 0;
				}else{
					int xi = hives.get(i).x;
					int yi = hives.get(i).y;
					int xj = hives.get(j).x;
					int yj = hives.get(j).y;
					
					int xdist = Math.abs(xi - xj);
					int ydist = Math.abs(yi - yj);
					
					int dist = (int) Math.sqrt( Math.pow(xdist, 2) + Math.pow(ydist, 2));
					distances[i][j] = dist;
				}
			}
		}		
	}
	
	public void printDistances(){
		for (int i = 0; i < distances.length; i++){
			//System.out.print("[");
			for (int j = 0; j < distances.length; j++){
				//System.out.print(distances[i][j]);
				if (j != distances.length - 1){
					//System.out.print("\t");
				}
			}
			//System.out.println("]");
		}
	}
	
	
	public static void main(String[] args){
		Map map = new Map();
		map.printDistances();
	}
}
