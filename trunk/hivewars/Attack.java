package hivewars;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Attack implements Serializable{
	
	public GameSettings.Control player;	    //player originating attack
	int sourceHiveNum; //where attacking minion originated from
	int destHiveNum;	//minion's destination
	int firingTime; 	//GameState number when minion was deployed
	
	//optional: will reduce overall computation time
	short hitTime;		//GameState number when minion will hit target
	
	public Attack(GameSettings.Control player, int i, int j, int k) {
		this.player = player;
		this.sourceHiveNum = i;
		this.destHiveNum = j;
		this.firingTime = k;
	}
	
	public static Attack copy(Attack a){		
		if(a != null){
			return new Attack(a.player, a.sourceHiveNum, a.destHiveNum, a.firingTime);
		} else {
			return null;
		}
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
