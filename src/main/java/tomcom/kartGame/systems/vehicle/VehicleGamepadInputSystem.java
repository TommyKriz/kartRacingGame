package tomcom.kartGame.systems.vehicle;

import tomcom.kartGame.components.GamepadInputComponent;
import tomcom.kartGame.components.physics.Body2DComponent;
import tomcom.kartGame.components.vehicle.VehicleComponent;
import tomcom.kartGame.components.vehicle.Wheel;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class VehicleGamepadInputSystem extends IteratingSystem {

	private static final int SEITENFUEHRUNGSKRAFT = 6;

	private static final float MAXIMUM_GAS_FORCE = -6f;

	private static final Family FAMILY = Family.all(VehicleComponent.class,
			Body2DComponent.class, GamepadInputComponent.class).get();

	private ComponentMapper<VehicleComponent> vc = ComponentMapper
			.getFor(VehicleComponent.class);

	private ComponentMapper<Body2DComponent> bc = ComponentMapper
			.getFor(Body2DComponent.class);

	public VehicleGamepadInputSystem() {
		super(FAMILY);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		VehicleComponent vehicle = vc.get(entity);

		Body2DComponent chassis = bc.get(entity);

		for (Controller controller : Controllers.getControllers()) {
			Gdx.app.log("VehicleGamepadInputSystem", controller.getName());

			Gdx.app.log("VehicleGamepadInputSystem", " axis 0 (LY): "
					+ controller.getAxis(0));

			Gdx.app.log("VehicleGamepadInputSystem", " axis 1 (LX): "
					+ controller.getAxis(1));

			for (Wheel w : vehicle.getSteerableWheels()) {
				// TODO: +90?
				w.orientation = controller.getAxis(1) * -100 + 90;
				w.getDirectionVector().setAngle(w.orientation);
			}

			Gdx.app.log("VehicleGamepadInputSystem", " axis 2 (RY): "
					+ controller.getAxis(2));

			Gdx.app.log("VehicleGamepadInputSystem", " axis 3 (RX): "
					+ controller.getAxis(3));

			Gdx.app.log("VehicleGamepadInputSystem", " axis 4 (LT RT): "
					+ controller.getAxis(4));

			Vector2 wheelPivot;
			for (Wheel w : vehicle.getWheels()) {

				wheelPivot = chassis.toWorldPoint(new Vector2(
						w.xOffsetFromPivot, w.yOffsetFromPivot));

				// TODO: visuzalize Forces

				// TODO: missing seitenführungskräfte! dependant on velocity on
				// wheelPivot

				Vector2 normalDirectionVector = w.getDirectionVector().cpy()
						.rotate(90);
				float seitenfuehrungsSpeed = normalDirectionVector.dot(chassis
						.getVelocity(wheelPivot));

				Gdx.app.log("VehicleGameInputSystem", "SEITENFÜHRUNGSKRAFT: "
						+ seitenfuehrungsSpeed);

				// chassis.getVelocity(wheelPivot)

				if (seitenfuehrungsSpeed > 0) {
					chassis.applyForce(new Vector2(-SEITENFUEHRUNGSKRAFT, 0),
							wheelPivot);
				} else if (seitenfuehrungsSpeed < 0) {
					chassis.applyForce(new Vector2(SEITENFUEHRUNGSKRAFT, 0),
							wheelPivot);
				}

				chassis.applyForce(
						w.getDirectionVector().cpy()
								.scl(controller.getAxis(4) * MAXIMUM_GAS_FORCE),
						wheelPivot);

			}
		}

	}

}
