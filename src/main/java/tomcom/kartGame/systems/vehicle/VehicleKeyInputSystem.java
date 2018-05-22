package tomcom.kartGame.systems.vehicle;

import tomcom.kartGame.components.KeyInputComponent;
import tomcom.kartGame.components.physics.Body2DComponent;
import tomcom.kartGame.components.vehicle.VehicleComponent;
import tomcom.kartGame.components.vehicle.Wheel;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class VehicleKeyInputSystem extends IteratingSystem {

	private static final Family FAMILY = Family.all(VehicleComponent.class,
			Body2DComponent.class, KeyInputComponent.class).get();

	/**
	 * in degrees
	 */
	private static final float WHEEL_TURN = 2;

	private static final float RIGHT_WHEEL_ORIENTATION_LIMIT = 110;

	private static final float LEFT_WHEEL_ORIENTATION_LIMIT = -110;

	private static final Vector2 NITRO_FORCE = new Vector2(0, 4);

	private ComponentMapper<VehicleComponent> vc = ComponentMapper
			.getFor(VehicleComponent.class);

	private ComponentMapper<KeyInputComponent> ic = ComponentMapper
			.getFor(KeyInputComponent.class);

	private ComponentMapper<Body2DComponent> bc = ComponentMapper
			.getFor(Body2DComponent.class);

	public VehicleKeyInputSystem() {
		super(FAMILY);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		VehicleComponent vehicle = vc.get(entity);
		int[] keys = ic.get(entity).getKeys();

		Array<Wheel> wheels = vehicle.getWheels();

		Body2DComponent chassis = bc.get(entity);

		// TODO: else's here???
		if (Gdx.input.isKeyPressed(keys[0])) {
			Gdx.app.log("Input received -", "STEERING WHEEL LEFT");
			steeringWheelLeft(wheels);
		}
		if (Gdx.input.isKeyPressed(keys[1])) {
			Gdx.app.log("Input received -", "STEERING WHEEL RIGHT");
			steeringWheelRight(wheels);
		}
		if (Gdx.input.isKeyPressed(keys[2])) {
			Gdx.app.log("Input received -", "PEDAL TO THE METAL");
			gas(wheels, chassis);
		}
		// if (Gdx.input.isKeyPressed(keys[3])) {
		// Gdx.app.log("Input received -", "BACK/BREAK");
		// vehicle.applyForce(DOWN_FORCE);
		// }
		if (Gdx.input.isKeyPressed(keys[4])) {
			Gdx.app.log("Input received -", "NITRO BOOST");
			chassis.applyForce(NITRO_FORCE);
		}

	}

	private void gas(Array<Wheel> wheels, Body2DComponent chassis) {
		float wx;
		float wy;
		Vector2 wheelPivot;
		for (Wheel w : wheels) {

			// TODO: whats faster??

			wx = chassis.getBody().getPosition().x + w.xOffsetFromPivot;
			wy = chassis.getBody().getPosition().y + w.yOffsetFromPivot;

			System.out.println("------- " + wx + "|" + wy);

			wheelPivot = chassis.toWorldPoint(new Vector2(w.xOffsetFromPivot,
					w.yOffsetFromPivot));

			System.out.println("---_--- " + wheelPivot.toString());

			// cpy()!!
			chassis.applyForce(w.getDirectionVector().cpy().scl(4.1f),
					wheelPivot);
		}
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
