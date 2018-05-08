package tomcom.kartGame.components.collision;

public class RectangleColliderComponent extends ColliderComponent {

	private float width;

	private float height;

	public RectangleColliderComponent(float width, float height, float density,
			float friction, float restitution) {
		super(density, friction, restitution);
		this.width = width;
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

}
