package tomcom.kartGame.components.collision;

public class CircleCollider extends Collider {

	private float radius;

	public CircleCollider(float radius, float density, float friction,
			float restitution) {
		super(density, friction, restitution);
		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}
}
