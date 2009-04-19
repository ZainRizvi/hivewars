package hivewars;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {
	
	public static HashMap<Integer,Hive> hives = new HashMap<Integer,Hive>();
	public static int[][] distances;
	

	public Map() {
		hives.put(0,new Hive(0, GameSettings.Control.PlayerA, 12, 10, 20, 50, 50));
		hives.put(1,new Hive(1, GameSettings.Control.PlayerB, 10, 10, 20, 600, 440));
		hives.put(2,new Hive(2, GameSettings.Control.Neutral, 5, 20, 10, 300, 210));
		hives.put(3,new Hive(3, GameSettings.Control.Neutral, 5, 20, 10, 550, 200));
		//hives.put(4,new Hive(4, GameSettings.Control.Neutral, 5, 20, 10, 50, 440));
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
			System.out.print("[");
			for (int j = 0; j < distances.length; j++){
				System.out.print(distances[i][j]);
				if (j != distances.length - 1){
					System.out.print("\t");
				}
			}
			System.out.println("]");
		}
	}
	
	
	public static void main(String[] args){
		Map map = new Map();
		map.printDistances();
	}
}
