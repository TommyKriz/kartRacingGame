package tomcom.kartGame.systems;

import tomcom.kartGame.components.CameraTargetComponent;
import tomcom.kartGame.components.PivotComponent;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class CameraFollowSystem extends IteratingSystem {

	private static final Family FAMILY = Family.all(PivotComponent.class,
			CameraTargetComponent.class).get();

	ComponentMapper<PivotComponent> pm = ComponentMapper
			.getFor(PivotComponent.class);

	OrthographicCamera cam;

	public CameraFollowSystem() {
		super(FAMILY);
	}

	Vector3 oldPos;

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

		PivotComponent pivot = pm.get(entity);

		// TODO: only change camera pos when it was actually changed? is if and
		// position comparing/remembering slower?

		// if (!pivot.equalTo(lastKnownPos)) {
		// camera position has to change
		// }

		Vector3 newPos = oldPos.lerp(
				new Vector3(pivot.getPos().x, pivot.getPos().y, 0), 0.4f);

		// pivot.getPos().lerp

		cam.position.set(newPos);

		oldPos = newPos;

	}

	@Override
	public void addedToEngine(Engine engine) {

		super.addedToEngine(engine);

		cam = getEngine().getSystem(CameraSystem.class).getWorldCamera();

		oldPos = cam.position;

	}

}
