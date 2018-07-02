package tomcom.kartGame.components.collision;

import com.badlogic.ashley.core.Component;

public class ColliderComponent implements Component {

	private Collider collider;

	public ColliderComponent(Collider collider) {
		this.collider = collider;
	}

	public Collider getCollider() {
		return collider;
	}

}
