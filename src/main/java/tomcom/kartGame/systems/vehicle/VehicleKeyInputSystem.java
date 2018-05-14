package tomcom.kartGame.systems.vehicle;

import tomcom.kartGame.components.KeyInputComponent;
import tomcom.kartGame.components.vehicle.VehicleComponent;
import tomcom.kartGame.components.vehicle.Wheel;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class VehicleKeyInputSystem extends IteratingSystem {

	private static final Family FAMILY = Family.all(VehicleComponent.class,
			KeyInputComponent.class).get();

	/**
	 * in degrees
	 */
	private static final float WHEEL_TURN = 2;

	private static final float RIGHT_WHEEL_ORIENTATION_LIMIT = 110;

	private static final float LEFT_WHEEL_ORIENTATION_LIMIT = -110;

	private ComponentMapper<VehicleComponent> vc = ComponentMapper
			.getFor(VehicleComponent.class);

	private ComponentMapper<KeyInputComponent> ic = ComponentMapper
			.getFor(KeyInputComponent.class);

	public VehicleKeyInputSystem() {
		super(FAMILY);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		VehicleComponent vehicle = vc.get(entity);
		int[] keys = ic.get(entity).getKeys();

		Array<Wheel> wheels = vehicle.getWheels();

		// TODO: else's here???
		if (Gdx.input.isKeyPressed(keys[0])) {
			Gdx.app.log("Input received -", "STEERING WHEEL LEFT");
			steeringWheelLeft(wheels);
		}
		if (Gdx.input.isKeyPressed(keys[1])) {
			Gdx.app.log("Input received -", "STEERING WHEEL RIGHT");
			steeringWheelRight(wheels);
		}
		// if (Gdx.input.isKeyPressed(keys[2])) {
		// Gdx.app.log("Input received -", "PEDAL TO THE METAL");
		// vehicle.applyForce(UP_FORCE);
		// }
		// if (Gdx.input.isKeyPressed(keys[3])) {
		// Gdx.app.log("Input received -", "BACK/BREAK");
		// vehicle.applyForce(DOWN_FORCE);
		// }
		// if (Gdx.input.isKeyPressed(keys[4])) {
		// Gdx.app.log("Input received -", "NITRO BOOST");
		// vehicle.applyForce(NITRO_FORCE);
		// }

	}

	private void steeringWheelRight(Array<Wheel> wheels) {
		for (Wheel w : wheels) {
			if (w.steerable) {
				if (w.orientation + WHEEL_TURN < RIGHT_WHEEL_ORIENTATION_LIMIT) {
					w.orientation += WHEEL_TURN;
					w.getDirectionVector().rotate(WHEEL_TURN);
				}
			}
		}
	}

	private void steeringWheelLeft(Array<Wheel> wheels) {
		for (Wheel w : wheels) {
			if (w.steerable) {
				if (w.orientation - WHEEL_TURN > LEFT_WHEEL_ORIENTATION_LIMIT) {
					w.orientation -= WHEEL_TURN;
					w.getDirectionVector().rotate(-WHEEL_TURN);
				}
			}
		}
	}
}
