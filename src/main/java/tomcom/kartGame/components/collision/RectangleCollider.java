package tomcom.kartGame.components.collision;

public class RectangleCollider extends Collider {

	private float width;

	private float height;

	public RectangleCollider(float width, float height, float density,
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
