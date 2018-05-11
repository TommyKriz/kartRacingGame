package tomcom.kartGame.systems;

import tomcom.kartGame.components.CameraTargetComponent;
import tomcom.kartGame.components.PivotComponent;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class CameraFollowSystem extends IteratingSystem {

	private static final Family FAMILY = Family.all(PivotComponent.class,
			CameraTargetComponent.class).get();

	OrthographicCamera cam;

	Vector2 lastKnownPos;

	public CameraFollowSystem() {
		super(FAMILY);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

		PivotComponent pivot = entity.getComponent(PivotComponent.class);

		// TODO: only change camera pos when it was actually changed

		// Gdx.app.log("CameraFollowSystem", "Pivot: " +
		// pivot.getPos().toString());
		// Gdx.app.log("CameraFollowSystem", "Cam:   " +
		// lastKnownPos.toString());
		//
		// if (!pivot.equalTo(lastKnownPos)) {
		// // camera position has to change
		//
		// Gdx.app.log("CameraFollowSystem", "change");
		// lastKnownPos = pivot.getPos();
		// cam.position.set(lastKnownPos, 0);
		// }

		cam.position.set(pivot.getPos(), 0);

		Gdx.app.log("CameraFollowSystem", "Cam:   "
				+ new Vector2(cam.position.x, cam.position.y).toString());

	}

	@Override
	public void addedToEngine(Engine engine) {

		super.addedToEngine(engine);

		cam = getEngine().getSystem(CameraSystem.class).getCamera();

		lastKnownPos = new Vector2(cam.position.x, cam.position.y);
	}

}
