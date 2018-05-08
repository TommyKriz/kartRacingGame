package tomcom.kartGame.systems;

import tomcom.kartGame.game.GameConfig;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;

public class CameraSystem extends EntitySystem {

	private OrthographicCamera camera;

	public CameraSystem() {
		camera = new OrthographicCamera(GameConfig.WIDTH, GameConfig.HEIGHT);
		camera.translate(GameConfig.WIDTH / 2, GameConfig.HEIGHT / 2);
	}

	@Override
	public void update(float deltaTime) {
		camera.update();
	}

	public Matrix4 getProjectionMatrix() {
		return camera.combined;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}
}
