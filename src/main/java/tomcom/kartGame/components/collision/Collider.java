package tomcom.kartGame.components.collision;

public abstract class Collider {

	private float density;
	private float friction;
	private float restitution;
	private boolean isSensor;
	private Object userData;

	public Collider(float density, float friction, float restitution) {
		this.density = density;
		this.friction = friction;
		this.restitution = restitution;
		isSensor = false;
		userData = null;
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

	public boolean isSensor() {
		return isSensor;
	}

	public Object getUserData() {
		return userData;
	}

	public Collider setSensor(boolean isSensor) {
		this.isSensor = isSensor;
		return this;
	}

	public Collider setUserData(Object userData) {
		this.userData = userData;
		return this;
	}

}
