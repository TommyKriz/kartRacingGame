package tomcom.kartGame.systems;

import tomcom.kartGame.scenes.EntityBuilder;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class MouseInputSystem extends EntitySystem {

	OrthographicCamera worldCamera;

	@Override
	public void update(float deltaTime) {

		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {

			// get the click coords in screen coords
			Vector3 clickCoords = new Vector3(Gdx.input.getX(),
					Gdx.input.getY(), 0);

			Gdx.app.log("MouseInputSystem", "Click @ " + clickCoords.toString()
					+ " (SCREEN)");

			// convert the screen coords into world coords

			worldCamera.unproject(clickCoords);

			Gdx.app.log("MouseInputSystem", "Click @ " + clickCoords.toString()
					+ " (WORLD)");

			// TODO: delete this later, its just for fun

			getEngine().addEntity(
					EntityBuilder.buildRoadBlock(clickCoords.x, clickCoords.y));
		}

	}

	@Override
	public void addedToEngine(Engine engine) {
		worldCamera = engine.getSystem(CameraSystem.class).getWorldCamera();
	}

}
