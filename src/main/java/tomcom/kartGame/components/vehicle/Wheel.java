package tomcom.kartGame.components.vehicle;

public class Wheel {

	public float xOffsetFromPivot;

	public float yOffsetFromPivot;

	public Wheel(float xOffsetFromPivot, float yOffsetFromPivot,
			boolean driven, boolean steerable) {
		this.xOffsetFromPivot = xOffsetFromPivot;
		this.yOffsetFromPivot = yOffsetFromPivot;
		this.driven = driven;
		this.steerable = steerable;
	}

	// TODO: replace with direction vector?
	float orientation;

	public boolean driven;

	/**
	 * wheel rotation can be controlled
	 */
	public boolean steerable;

}
