package hivewars;

import java.awt.Color;

public class MinionNumber {
	
	Color c;
	String Minions;
	int x;
	int y;

	public MinionNumber(Color C, int m, int X, int Y) {
		c = C;
		Minions = Integer.toString(m);
		x = X;
		y = Y;
	}
	
	public void setNumber(int i) {
		Minions = Integer.toString(i);
	}

}
