package hivewars;

import com.golden.gamedev.object.Sprite;
import com.golden.gamedev.object.collision.CollisionGroup;

public class AttackToHiveCollision extends CollisionGroup {

	public AttackToHiveCollision() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void collided(Sprite s1, Sprite s2) {
		// TODO Auto-generated method stub
		if(Gui.gui.hives.indexOf(s2) == Gui.gui.currentGS.attacks.get(Gui.gui.attacks.indexOf(s1)).destHiveNum){
			s1.setActive(false);
		}
	}

}
