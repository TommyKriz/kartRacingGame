package tomcom.kartGame.systems.vehicle;

import tomcom.kartGame.components.GamepadInputComponent;
import tomcom.kartGame.components.physics.Body2DComponent;
import tomcom.kartGame.components.vehicle.VehicleComponent;
import tomcom.kartGame.components.vehicle.Wheel;
import tomcom.kartGame.systems.CameraSystem;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class VehicleGamepadInputDebugRendererSystem extends IteratingSystem {

	private static final int MAXIMUM_WHEEL_ANGLE = 60;

	private static final int SEITENFUEHRUNGSKRAFT = 4000;

	private static final float MAXIMUM_GAS_FORCE = -30000f;

	private static final Family FAMILY = Family.all(VehicleComponent.class,
			Body2DComponent.class, GamepadInputComponent.class).get();

	private static final float DIRECTION_VECTOR_SCALE = 1f;

	private ComponentMapper<VehicleComponent> vc = ComponentMapper
			.getFor(VehicleComponent.class);

	private ComponentMapper<Body2DComponent> bc = ComponentMapper
			.getFor(Body2DComponent.class);

	ShapeRenderer renderer;

	public VehicleGamepadInputDebugRendererSystem() {
		super(FAMILY);
		renderer = new ShapeRenderer();
	}

	// TODO: do that for other system
	@Override
	public void update(float deltaTime) {

		// TODO: dirty flag?
		renderer.setProjectionMatrix(getEngine().getSystem(CameraSystem.class)
				.getProjectionMatrix());

		renderer.begin(ShapeType.Line);

		super.update(deltaTime);

		renderer.end();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		VehicleComponent vehicle = vc.get(entity);

		Body2DComponent chassis = bc.get(entity);

		for (Controller controller : Controllers.getControllers()) {

			turnWheels(vehicle, chassis, controller.getAxis(1));

			sideForce(vehicle, chassis);

			gas(vehicle, chassis, controller.getAxis(4));

		}

	}

	private void gas(VehicleComponent vehicle, Body2DComponent chassis,
			float controllerYaxis) {
		for (Wheel w : vehicle.getDrivenWheels()) {

			chassis.applyForce(
					w.getDirectionVector().cpy()
							.scl(controllerYaxis * MAXIMUM_GAS_FORCE), chassis
							.toWorldPoint(new Vector2(w.xOffsetFromPivot,
									w.yOffsetFromPivot)));
		}

	}

	private void sideForce(VehicleComponent vehicle, Body2DComponent chassis) {
		Vector2 wheelPivot;
		for (Wheel w : vehicle.getWheels()) {

			wheelPivot = chassis.toWorldPoint(new Vector2(w.xOffsetFromPivot,
					w.yOffsetFromPivot));

			// visualize direction vector
			drawVector(wheelPivot, w.getDirectionVector(),
					DIRECTION_VECTOR_SCALE, Color.FIREBRICK);

			Vector2 normalDirectionVector = w.getDirectionVector().cpy()
					.rotate(90);
			// visualize normal to direction vector
			drawVector(wheelPivot, normalDirectionVector,
					DIRECTION_VECTOR_SCALE, Color.YELLOW);

			// visualize velocity
			drawVector(wheelPivot, chassis.getVelocity(wheelPivot), 1,
					Color.BLUE);

			renderer.setColor(Color.GOLDENROD);
			renderer.line(wheelPivot, chassis.getPosition());

			float seitenfuehrungsSpeed = normalDirectionVector.dot(chassis
					.getVelocity(wheelPivot));

			Gdx.app.log("VehicleGameInputSystem", "SEITENFÜHRUNGSKRAFT: "
					+ seitenfuehrungsSpeed);

			Vector2 sideForce = normalDirectionVector.cpy().scl(
					SEITENFUEHRUNGSKRAFT);
			if (seitenfuehrungsSpeed < 0) {
				// sideForce = normalDirectionVector;
			} else if (seitenfuehrungsSpeed > 0) {
				sideForce.scl(-1, -1);
			} else {
				sideForce = new Vector2(0, 0);
			}
			drawVector(wheelPivot, sideForce, 0.001f, Color.PINK);

			chassis.applyForce(sideForce, wheelPivot);

		}

	}

	private void turnWheels(VehicleComponent vehicle, Body2DComponent chassis,
			float controllerXaxis) {
		for (Wheel w : vehicle.getSteerableWheels()) {
			// TODO: +90?
			// TODO: take out chassis degrad
			w.orientation = chassis.getAngleInRadians()
					/ MathUtils.degreesToRadians + controllerXaxis
					* -MAXIMUM_WHEEL_ANGLE + 90;
			w.getDirectionVector().setAngle(w.orientation);
		}
	}

	private void drawVector(Vector2 pivot, Vector2 v, float scale, Color color) {
		renderer.setColor(color);
		// TODO: v.cpy() is it too cost expensive
		renderer.rectLine(pivot, pivot.cpy().add(v.cpy().scl(scale)), 0.025f);
	}
}
