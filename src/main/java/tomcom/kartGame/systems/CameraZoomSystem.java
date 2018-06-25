package tomcom.kartGame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
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

		for (Controller controller : Controllers.getControllers()) {

			if (controller.getButton(2)) {
				worldCamera.zoom += CAMERA_ZOOM_SPEED;
			} else if (controller.getButton(3)) {
				worldCamera.zoom -= CAMERA_ZOOM_SPEED;
			}

			// TODO: move to new camera rotate system
			if (controller.getButton(4)) {
				worldCamera.rotate(-1f);
			} else if (controller.getButton(5)) {
				worldCamera.rotate(1f);
			}

		}
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		worldCamera = engine.getSystem(CameraSystem.class).getWorldCamera();
		worldCamera.zoom = startingZoom;
	}
}