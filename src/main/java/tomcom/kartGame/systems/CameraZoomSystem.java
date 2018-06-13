package tomcom.kartGame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraZoomSystem extends EntitySystem {

	private static final float CAMERA_ZOOM_SPEED = 0.2f;

	private float startingZoom = 8f;

	private OrthographicCamera worldCamera;

	public CameraZoomSystem(float startingZoom) {
		this.startingZoom = startingZoom;
	}

	@Override
	public void update(float deltaTime) {
		if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
			worldCamera.zoom += CAMERA_ZOOM_SPEED;
		} else if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
			worldCamera.zoom -= CAMERA_ZOOM_SPEED;
		}
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		worldCamera = engine.getSystem(CameraSystem.class).getWorldCamera();
		worldCamera.zoom = startingZoom;
	}
}