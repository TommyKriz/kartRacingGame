package tomcom.kartGame.systems;

import tomcom.kartGame.game.GameConfig;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;

public class CameraSystem extends EntitySystem {

	private OrthographicCamera hudCamera;

	private OrthographicCamera worldCamera;

	public CameraSystem() {

		hudCamera = new OrthographicCamera(GameConfig.WIDTH, GameConfig.HEIGHT);

		hudCamera.translate(GameConfig.WIDTH / 2, GameConfig.HEIGHT / 2);

		worldCamera = new OrthographicCamera(GameConfig.WIDTH
				/ GameConfig.PIXELS_PER_METER, GameConfig.HEIGHT
				/ GameConfig.PIXELS_PER_METER);
		worldCamera.translate(worldCamera.viewportWidth / 2,
				worldCamera.viewportHeight / 2);

		worldCamera.zoom = 200;
	}

	@Override
	public void update(float deltaTime) {
		worldCamera.update();
	}

	public Matrix4 getProjectionMatrix() {
		return worldCamera.combined;
	}

	public OrthographicCamera getCamera() {
		return worldCamera;
	}

}
