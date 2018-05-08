package tomcom.kartGame.systems;

import tomcom.kartGame.components.InputComponent;
import tomcom.kartGame.components.physics.Body2DComponent;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class InputSystem extends IteratingSystem {

	private static final Vector2 LEFT_FORCE = new Vector2(-10, 0);

	private static final Vector2 RIGHT_FORCE = new Vector2(10, 0);

	private static final Vector2 UP_FORCE = new Vector2(0, 7);

	private static final Vector2 DOWN_FORCE = new Vector2(0, -7);

	private static final Vector2 NITRO_FORCE = new Vector2(0, 20);

	private static final Family FAMILY = Family.all(Body2DComponent.class,
			InputComponent.class).get();

	private ComponentMapper<Body2DComponent> bc = ComponentMapper
			.getFor(Body2DComponent.class);

	private ComponentMapper<InputComponent> ic = ComponentMapper
			.getFor(InputComponent.class);

	public InputSystem() {
		super(FAMILY);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Body2DComponent body = bc.get(entity);
		int[] keys = ic.get(entity).getKeys();

		// TODO: else's here???
		if (Gdx.input.isKeyPressed(keys[0])) {
			Gdx.app.log("Input received -", "LEFT");
			body.applyForce(LEFT_FORCE);
		}
		if (Gdx.input.isKeyPressed(keys[1])) {
			Gdx.app.log("Input received -", "RIGHT");
			body.applyForce(RIGHT_FORCE);
		}
		if (Gdx.input.isKeyPressed(keys[2])) {
			Gdx.app.log("Input received -", "UP");
			body.applyForce(UP_FORCE);
		}
		if (Gdx.input.isKeyPressed(keys[3])) {
			Gdx.app.log("Input received -", "DOWN");
			body.applyForce(DOWN_FORCE);
		}
		if (Gdx.input.isKeyPressed(keys[4])) {
			Gdx.app.log("Input received -", "NITRO");
			body.applyForce(NITRO_FORCE);
		}

		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			Gdx.app.log(
					"Input received -",
					"MOUSE CLICKed @ " + Gdx.input.getX() + "|"
							+ Gdx.input.getY());
		}

	}
}
