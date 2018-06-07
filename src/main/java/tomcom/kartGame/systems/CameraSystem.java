package tomcom.kartGame.systems;

import tomcom.kartGame.config.GameConfig;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;

public class CameraSystem extends EntitySystem {

	private OrthographicCamera worldCamera;

	public CameraSystem() {
		worldCamera = new OrthographicCamera();
		worldCamera.setToOrtho(false,
				GameConfig.WORLD_WIDTH_SEEN_THROUGH_CAMERA,
				GameConfig.WORLD_WIDTH_SEEN_THROUGH_CAMERA
						* (GameConfig.SCREEN_WIDTH / GameConfig.SCREEN_HEIGHT));
	}

	@Override
	public void update(float deltaTime) {
		worldCamera.update();
	}

	public Matrix4 getProjectionMatrix() {
		return worldCamera.combined;
	}

	public OrthographicCamera getWorldCamera() {
		return worldCamera;
	}

}
