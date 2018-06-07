package tomcom.kartGame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraZoomSystem extends EntitySystem {

	private static final float CAMERA_STARTING_ZOOM = 8f;

	private OrthographicCamera worldCamera;

	public CameraZoomSystem() {

	}

	@Override
	public void update(float deltaTime) {
		if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
			worldCamera.zoom += 0.02;
		} else if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
			worldCamera.zoom -= 0.02;
		}
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		worldCamera = engine.getSystem(CameraSystem.class).getWorldCamera();
		worldCamera.zoom = CAMERA_STARTING_ZOOM;
	}
}