package tomcom.kartGame.systems.vehicle;

import tomcom.kartGame.components.GamepadInputComponent;
import tomcom.kartGame.components.physics.Body2DComponent;
import tomcom.kartGame.components.vehicle.VehicleComponent;
import tomcom.kartGame.components.vehicle.Wheel;
import tomcom.kartGame.config.EntityConfig;
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

/**
 * TODO:
 * 
 * slip angle sinus funktion parametrisch modellieren (grafisch plotten?) .
 * initialer größerer antriebsaufwand beim wegfahren. luftwiderstand. .
 * Reibungskoeffizient.
 * 
 * TODO: wasserfläche!
 * 
 * 
 * @author Tommy
 *
 */
public class VehicleGamepadInputDebugRendererSystem extends IteratingSystem {

	private static final float FORCE_DRAWING_SCALE = 0.001f;

	private static final Family FAMILY = Family.all(VehicleComponent.class,
			Body2DComponent.class, GamepadInputComponent.class).get();

	private static final int MAXIMUM_WHEEL_ANGLE = 60;

	private static final float MAXIMUM_GAS_FORCE = 6000f;

	private static final float KAMMSCHER_KREIS_RADIUS = 12000;

	// TODO: replace with MASS / nWheels
	// private static final float NORMAL_FORCE = 2000;

	float normalForcePerWheel;

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

		normalForcePerWheel = EntityConfig.LAMBO_MASS
				/ vehicle.getWheels().size * 9.81f;

		for (Controller controller : Controllers.getControllers()) {

			turnWheels(vehicle, chassis, controller.getAxis(1));
			gasAndSideForce(vehicle, chassis, controller.getAxis(4));
			rollingResistance(vehicle, chassis);

			if (controller.getButton(1)) {
				// XBOX B
				brake(vehicle, chassis);
			}

		}
	}

	private void brake(VehicleComponent vehicle, Body2DComponent chassis) {

		for (Wheel w : vehicle.getWheels()) {

			Vector2 wheelPivot = chassis.toWorldPoint(w.offsetFromPivot);

			Vector2 velocity = chassis.getVelocity(wheelPivot).cpy();

			float speed = velocity.len();
			if (speed != 0) {
				Vector2 brakingForce = w.getDirectionVector().cpy().scl(-1, -1)
						.nor()
						// TODO: other brake Force?
						.scl(normalForcePerWheel * 1f);
				// drawVector(wheelPivot, brakingForce, FORCE_DRAWING_SCALE,
				// Color.ROYAL);
				chassis.applyForce(brakingForce, wheelPivot);
			}

		}

	}

	private void gasAndSideForce(VehicleComponent vehicle,
			Body2DComponent chassis, float axis) {

		for (Wheel w : vehicle.getWheels()) {

			Vector2 wheelPivot = chassis.toWorldPoint(w.offsetFromPivot);

			Vector2 gasForce;
			if (w.driven) {
				gasForce = gasForce(w, axis);
			} else {
				gasForce = new Vector2(0, 0);
			}

			 drawVector(wheelPivot, gasForce, FORCE_DRAWING_SCALE,
			 Color.PINK);

			Vector2 sideForce = sideForce(w, chassis);

			// drawVector(wheelPivot, sideForce, FORCE_DRAWING_SCALE,
			// Color.GOLDENROD);

			// initiale stärkere kraft
			if (chassis.getVelocity(chassis.getPosition()).len() < 4) {
				Gdx.app.log("VehicleGamepadInputSystem",
						"initial driving force available");
				gasForce.scl(2f);
			}

			chassis.applyForce(circleOfForces(gasForce, sideForce), wheelPivot);

		}

	}

	private Vector2 circleOfForces(Vector2 gasForce, Vector2 sideForce) {
		Vector2 force = gasForce.add(sideForce);
		if (force.len() > KAMMSCHER_KREIS_RADIUS) {
			force = force.nor().scl(KAMMSCHER_KREIS_RADIUS);
		}
		return force;
	}

	private Vector2 sideForce(Wheel w, final Body2DComponent chassis) {

		Vector2 directionVector = w.getDirectionVector().cpy().nor();

		// Vector2 wheelPivot = chassis.toWorldPoint(w.offsetFromPivot);
		//
		// Vector2 velocityAtThisWheel = chassis.getVelocity(wheelPivot).cpy();
		//
		// drawVector(wheelPivot, directionVector, 4, Color.PINK);
		// drawVector(wheelPivot, velocityAtThisWheel, 2, Color.GREEN);
		//
		// // float slipAngle = (float)
		// // Math.acos(normalDirectionVector.dot(velocity
		// // .cpy().nor()));
		//
		// float slipAngle =
		// directionVector.cpy().dot(velocityAtThisWheel.nor());
		//
		// // System.out.println("slipAngle: " +slipAngle);
		// System.out.println("slipAngle acos: " + Math.acos(slipAngle));

		Vector2 normalDirectionVector = directionVector.rotate(90);

		Vector2 sideForce = normalDirectionVector
				.scl(1.1f * normalForcePerWheel);

		float slipAngle = 0;

		if (slipAngle < 0) {
			return sideForce;
		} else if (slipAngle > 0) {
			return sideForce.scl(-1, -1);
		}
		return new Vector2(0, 0);

	}

	private Vector2 gasForce(Wheel w, float controllerYAxis) {
		return w.getDirectionVector().cpy().nor()
				.scl(-controllerYAxis * MAXIMUM_GAS_FORCE);
	}

	private float slipAngleCoefficient(float slipAngle) {
		if (slipAngle > 0) {
			return 1.1f;
		} else if (slipAngle < 0) {
			return -1.1f;
		}
		return 0;
	}

	// TODO: put this in vehicle
	float AXIS_HEIGHT;

	private void turnWheels(VehicleComponent vehicle, Body2DComponent chassis,
			float controllerXaxis) {

		float inputAngle = controllerXaxis * -MAXIMUM_WHEEL_ANGLE;

		adjustDrivingWheelsAngle(vehicle, chassis);

		AXIS_HEIGHT = getAxisHeight();

		if (inputAngle > 0) {
			Gdx.app.log("Ackermann",
					"\n\n##############################\nSTEERING LEFT  -  inputAngle: "
							+ inputAngle);
			setAckermannAngle(inputAngle, chassis, vehicle, 1);
		} else {
			Gdx.app.log("Ackermann",
					"\n\n##############################\nSTEERING RIGHT  -  inputAngle: "
							+ inputAngle);
			setAckermannAngle(inputAngle, chassis, vehicle, 0);
		}

	}

	private void setAckermannAngle(float inputAngle, Body2DComponent chassis,
			VehicleComponent vehicle, int axis) {

		Wheel firstSteelWheel = vehicle.getSteerableWheels().get(axis);

		Vector2 firstSteeringWheelPivot = chassis
				.toWorldPoint(firstSteelWheel.offsetFromPivot);

		Wheel firstBottomWheel = vehicle.getWheels().get(2 + axis);

		Vector2 firstBottomWheelPivot = chassis
				.toWorldPoint(firstBottomWheel.offsetFromPivot);

		// renderer.setColor(Color.FIREBRICK);
		// renderer.circle(firstBottomWheelPivot.x, firstBottomWheelPivot.y,
		// 0.2f);

		float offsetX = -(float) (Math.sin(inputAngle
				* MathUtils.degreesToRadians) * AXIS_HEIGHT);

		Vector2 intersectionPoint = chassis
				.toWorldPoint(firstBottomWheel.offsetFromPivot.cpy().add(
						offsetX, 0));

		// renderer.setColor(Color.GREEN);
		// renderer.circle(intersectionPoint.x, intersectionPoint.y, 0.2f);

		Wheel otherSteeringWheel = vehicle.getWheels().get(1 - axis);

		Vector2 otherSteeringWheelPivot = chassis
				.toWorldPoint(otherSteeringWheel.offsetFromPivot);

		Vector2 v = otherSteeringWheelPivot.cpy().sub(intersectionPoint.cpy());

		Vector2 v1 = otherSteeringWheelPivot.cpy().sub(
				firstBottomWheelPivot.cpy());

		float otherAngle = v1.angle() - v.angle();

		firstSteelWheel.updateAngle(firstSteelWheel.orientation + inputAngle);

		otherSteeringWheel.updateAngle(otherSteeringWheel.orientation
				+ otherAngle);
		// drawVector(firstSteeringWheelPivot,
		// firstSteelWheel.getDirectionVector(), 2, Color.GREEN);

		// drawVector(otherSteeringWheelPivot,
		// otherSteeringWheel.getDirectionVector(), 2, Color.YELLOW);
	}

	private float getAxisHeight() {
		return (EntityConfig.LAMBO_HEIGHT / 2 - EntityConfig.WHEEL_HEIGHT) * 2;
	}

	private void adjustDrivingWheelsAngle(VehicleComponent vehicle,
			Body2DComponent chassis) {
		float chassisAngle = chassis.getAngleInDegrees();
		for (Wheel w : vehicle.getWheels()) {
			w.updateAngle(chassisAngle);
			// drawVector(chassis.toWorldPoint(w.offsetFromPivot),
			// w.getDirectionVector(), 1, Color.YELLOW);
		}

	}

	private void rollingResistance(VehicleComponent vehicle,
			Body2DComponent chassis) {

		for (Wheel w : vehicle.getWheels()) {

			Vector2 wheelPivot = chassis.toWorldPoint(w.offsetFromPivot);

			Vector2 rollingResistanceForce = w.getDirectionVector().cpy()
					.scl(0.4f * normalForcePerWheel);

			float angle = w.getDirectionVector().cpy()
					.dot(chassis.getVelocity(wheelPivot));

			if (angle > 0) {
				rollingResistanceForce.scl(-1, -1);
			} else if (angle < 0) {

			} else {
				rollingResistanceForce = new Vector2(0, 0);
			}

			drawVector(wheelPivot, rollingResistanceForce, FORCE_DRAWING_SCALE,
					Color.WHITE);

			chassis.applyForce(rollingResistanceForce, wheelPivot);

		}
	}

	private void drawVector(Vector2 pivot, Vector2 v, float scale, Color color) {
		renderer.setColor(color);
		// TODO: v.cpy() is it too cost expensive
		renderer.rectLine(pivot, pivot.cpy().add(v.cpy().scl(scale)), 0.025f);
	}
}
