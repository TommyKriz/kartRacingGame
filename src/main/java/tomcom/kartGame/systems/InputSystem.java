package tomcom.kartGame.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector2;

public class InputSystem extends EntitySystem {

	private static final float KEYBOARD_AXIS_SPEED = 0.8f;
	public static Signal<Vector2> onInputReceived = new Signal<Vector2>();

	@Override
	public void update(float deltaTime) {
		float xAxis = 0;
		float yAxis = 0;
		for (Controller controller : Controllers.getControllers()) {
			xAxis = controller.getAxis(1);
			yAxis = controller.getAxis(4);
		}
		if (Controllers.getControllers().size == 0) {

			if (Gdx.input.isKeyPressed(Input.Keys.D)) {
				xAxis = KEYBOARD_AXIS_SPEED;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.A)) {
				xAxis = -KEYBOARD_AXIS_SPEED;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.W)) {
				yAxis = -KEYBOARD_AXIS_SPEED;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.S)) {
				yAxis = KEYBOARD_AXIS_SPEED;
			}
		}
		onInputReceived.dispatch(new Vector2(xAxis, yAxis));

	}
}
