package tomcom.kartGame.systems;

import tomcom.kartGame.game.GameConfig;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;

public class CameraSystem extends EntitySystem {

	private OrthographicCamera worldCamera;

	public CameraSystem() {
		// worldCamera = new OrthographicCamera(
		// GameConfig.WORLD_WIDTH_SEEN_THROUGH_CAMERA,
		// GameConfig.WORLD_HEIGHT_SEEN_THROUGH_CAMERA);
		worldCamera = new OrthographicCamera();
		worldCamera.setToOrtho(false,
				GameConfig.WORLD_WIDTH_SEEN_THROUGH_CAMERA,
				GameConfig.WORLD_HEIGHT_SEEN_THROUGH_CAMERA);
	}

	@Override
	public void update(float deltaTime) {
		worldCamera.update();

		// TODO: take out camera zoom

		if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
			// TODO: Why is this not shown??
			worldCamera.zoom += 0.02;
		} else if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
			worldCamera.zoom -= 0.02;
		}

	}

	public Matrix4 getProjectionMatrix() {
		return worldCamera.combined;
	}

	public OrthographicCamera getWorldCamera() {
		return worldCamera;
	}

}
