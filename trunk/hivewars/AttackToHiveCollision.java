package hivewars;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.golden.gamedev.object.Sprite;
import com.golden.gamedev.object.SpriteGroup;
import com.golden.gamedev.object.Timer;
import com.golden.gamedev.object.collision.CollisionGroup;
import com.golden.gamedev.object.sprite.VolatileSprite;

public class AttackToHiveCollision extends CollisionGroup {

	GoldenT g;
	VolatileSprite v;
	SpriteGroup Explosions;
	ArrayList<VolatileSprite> explosions;
	BufferedImage[] Expld;
	Timer t;
	
	public AttackToHiveCollision(GoldenT gt) {
		// TODO Auto-generated constructor stub
		g = gt;
		Explosions = g.Explosions;
		explosions = g.explosions;
		Expld = g.expld;
		t = new Timer(100);
	}

	@Override
	public void collided(Sprite s1, Sprite s2) {
		// TODO Auto-generated method stub
		if(g.currentGS.hives.get(g.hives.indexOf(s2)).controllingPlayer != GameController.Me){
			v = new VolatileSprite(Expld, s1.getX() - 25 + Math.random() * 15, s1.getY() - 25 + Math.random() * 15);
			v.setAnimationTimer(t);
			explosions.add(v);
			Explosions.add(v);
		}
	}

}
