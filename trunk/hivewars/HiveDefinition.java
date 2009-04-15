package hivewars;

public class HiveDefinition {
	public int id;
	public int startingAnts;
	public int spawnRate;
	public int x;
	public int y;

	public HiveDefinition(int id, int startingAnts, int spawnRate, int x, int y){
		this.id = id;
		this.startingAnts = startingAnts;
		this.spawnRate = spawnRate;
		this.x = x;
		this.y = y;
	}
}
