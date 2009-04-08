package hivewars;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Attack implements Serializable{
	
	char player;	    //player originating attack
	char sourceHiveNum; //where attacking minion originated from
	char destHiveNum;	//minion's destination
	short firingTime; 	//GameState number when minion was deployed
	
	//optional: will reduce overall computation time
	short hitTime;		//GameState number when minion will hit target
	
	public Attack(char player, char sourceHiveNum, char destHiveNum, short firingTime) {
		this.player = player;
		this.sourceHiveNum = sourceHiveNum;
		this.destHiveNum = destHiveNum;
		this.firingTime = firingTime;
	}	
	
}
