package tomcom.kartGame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class PivotComponent implements Component {

	/**
	 * x,y and angle in degrees
	 */
	private Vector3 pos;

	public PivotComponent(Vector2 pos) {
		this.pos = new Vector3(pos, 0);
	}

	public Vector3 getPos() {
		return pos;
	}

	public void setPos(Vector2 pos, float angle) {
		this.pos = new Vector3(pos, angle);
	}

}