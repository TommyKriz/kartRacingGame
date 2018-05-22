package tomcom.kartGame.components.vehicle;

import com.badlogic.gdx.math.Vector2;

public class Wheel {

	public float xOffsetFromPivot;

	public float yOffsetFromPivot;

	/**
	 * point upwards by default.
	 */
	private Vector2 directionVector = new Vector2(0, 1);

	public float orientation = 0f;

	public Wheel(float xOffsetFromPivot, float yOffsetFromPivot,
			boolean driven, boolean steerable) {
		this.xOffsetFromPivot = xOffsetFromPivot;
		this.yOffsetFromPivot = yOffsetFromPivot;
		this.driven = driven;
		this.steerable = steerable;
	}

	public Vector2 getDirectionVector() {
		return directionVector;
	}

	public boolean driven;

	/**
	 * wheel rotation can be controlled
	 */
	public boolean steerable;

}
