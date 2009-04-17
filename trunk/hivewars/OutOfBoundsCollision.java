package hivewars;
//GTGE
import com.golden.gamedev.object.*;
import com.golden.gamedev.object.collision.*;


public class OutOfBoundsCollision extends CollisionBounds {

   public OutOfBoundsCollision(Background arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

   public void collided(Sprite s) {
	   // TODO Auto-generated method stub
	   s.setActive(false);
	}

}


