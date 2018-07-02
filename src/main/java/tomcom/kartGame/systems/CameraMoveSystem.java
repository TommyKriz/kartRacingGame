package tomcom.kartGame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraMoveSystem extends EntitySystem {

	private static final float CAMERA_MOVE_SPEED = 0.4f;
	private OrthographicCamera worldCamera;

	public CameraMoveSystem() {

	}

	@Override
	public void update(float deltaTime) {
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			worldCamera.translate(-CAMERA_MOVE_SPEED, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			worldCamera.translate(CAMERA_MOVE_SPEED, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			worldCamera.translate(0, CAMERA_MOVE_SPEED);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			worldCamera.translate(0, -CAMERA_MOVE_SPEED);
		}
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		worldCamera = engine.getSystem(CameraSystem.class).getWorldCamera();
	}
}