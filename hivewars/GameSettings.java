package hivewars;

//
// Here's the place to store all your global settings
//
interface GameSettings {
	//total number of hives in the game
	static final int numHives = 10; 
	//maximum number of ants allowed on the field at a time per player
	static final int maxAnts = 125; 
	//initial state number when game begins
	static int initialState = 0;
	
	// Defines everyone who could control a hive
	enum Control{
		PlayerA, PlayerB, Neutral
	}
	
	// Default values for hives
	static final Control DefaultControler = GameSettings.Control.Neutral;
	static final char defaultNumMinions = 10;
	static final int defaultSpawnRate = 10;
	static final int defaultHiveCapacity = 20;
}
