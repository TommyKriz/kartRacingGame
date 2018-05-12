package tomcom.kartGame.components.collision;

public abstract class Collider {

	private float density;
	private float friction;
	private float restitution;

	public Collider(float density, float friction, float restitution) {
		this.density = density;
		this.friction = friction;
		this.restitution = restitution;
	}

	public float getDensity() {
		return density;
	}

	public float getFriction() {
		return friction;
	}

	public float getRestitution() {
		return restitution;
	}

}
