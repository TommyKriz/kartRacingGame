package tomcom.kartGame.components.collision;

import com.badlogic.ashley.core.Component;

public abstract class ColliderComponent implements Component {

	private float density;
	private float friction;
	private float restitution;

	public ColliderComponent(float density, float friction, float restitution) {
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
