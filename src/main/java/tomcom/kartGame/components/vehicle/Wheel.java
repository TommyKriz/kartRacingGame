package tomcom.kartGame.components.vehicle;

import com.badlogic.gdx.math.Vector2;

public class Wheel {

	public Vector2 offsetFromPivot;

	/**
	 * point upwards by default.
	 */
	private Vector2 directionVector = new Vector2(0, 1);

	public float orientation = 0f;

	public Wheel(float xOffsetFromPivot, float yOffsetFromPivot,
			boolean steerable) {
		offsetFromPivot = new Vector2(xOffsetFromPivot, yOffsetFromPivot);
		this.steerable = steerable;
	}

	public Vector2 getDirectionVector() {
		return directionVector;
	}

	/**
	 * wheel rotation can be controlled
	 */
	public boolean steerable;

}