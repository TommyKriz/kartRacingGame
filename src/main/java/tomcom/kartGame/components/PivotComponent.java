package tomcom.kartGame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PivotComponent implements Component {

	private Vector2 pos;

	public PivotComponent(Vector2 pos) {
		this.pos = pos;
	}

	public Vector2 getPos() {
		return pos;
	}

	public void setPos(Vector2 pos) {
		this.pos = pos;
	}

}
