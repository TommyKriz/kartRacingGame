package tomcom.kartGame.components.collision;

public class CircleColliderComponent extends ColliderComponent {

	private float radius;

	public CircleColliderComponent(float radius, float density, float friction,
			float restitution) {
		super(density, friction, restitution);
		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}
}
