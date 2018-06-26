package tomcom.kartGame.components.vehicle;

import com.badlogic.gdx.math.Vector2;

public class Wheel {

	public Vector2 offsetFromPivot;

	public float orientation = 0f;

	/**
	 * point upwards by default.
	 */
	private Vector2 directionVector = new Vector2(0, 1);

	/**
	 * wheel rotation can be controlled
	 */
	public boolean steerable;

	public boolean driven;

	public Wheel(float xOffsetFromPivot, float yOffsetFromPivot,
			boolean steerable, boolean driven) {
		offsetFromPivot = new Vector2(xOffsetFromPivot, yOffsetFromPivot);
		this.steerable = steerable;
		this.driven = driven;
	}

	public Vector2 getDirectionVector() {
		return directionVector;
	}

	public void updateAngle(float angle) {
		directionVector.x = 0;
		directionVector.y = 1;
		orientation = angle;
		directionVector.rotate(orientation);
	}

}