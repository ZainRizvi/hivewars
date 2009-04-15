package hivewars;

import java.io.Serializable;
import java.util.concurrent.Semaphore;

@SuppressWarnings("serial")
public class Attack implements Serializable{
	private static Semaphore mutex = new Semaphore(1, true); //binary, fair
	
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
	
	public static Attack copy(Attack a){		
		return new Attack(a.player, a.sourceHiveNum, a.destHiveNum, a.firingTime);
	}
	
	public boolean equals(Attack attack){
		if(player != attack.player){
			return false;
		}
		if(sourceHiveNum != attack.sourceHiveNum){
			return false;
		}
		if(destHiveNum != attack.destHiveNum){
			return false;
		}
		if(firingTime != attack.firingTime){
			return false;
		}
		return true;	
	}
}
