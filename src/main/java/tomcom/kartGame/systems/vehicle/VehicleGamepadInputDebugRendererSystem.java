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

	private static final float FORCE_DRAWING_SCALE = 0.001f;

	private static final Family FAMILY = Family.all(VehicleComponent.class,
			Body2DComponent.class, GamepadInputComponent.class).get();

	private static final int MAXIMUM_WHEEL_ANGLE = 55;

	private static final float MAXIMUM_GAS_FORCE = 6000f;

	private static final float KAMMSCHER_KREIS_RADIUS = 12000;

	// TODO: replace with MASS / nWheels
	private static final float NORMAL_FORCE = 2000;

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
				System.out.println("BRAKING");
				Vector2 brakingForce = velocity.scl(-1, -1).nor()
				// TODO: other brake Force?
						.scl(MAXIMUM_GAS_FORCE);
				drawVector(wheelPivot, brakingForce, FORCE_DRAWING_SCALE,
						Color.ROYAL);
				chassis.applyForce(brakingForce, wheelPivot);
			}

		}

	}

	private void gasAndSideForce(VehicleComponent vehicle,
			Body2DComponent chassis, float axis) {

		for (Wheel w : vehicle.getWheels()) {

			Vector2 wheelPivot = chassis.toWorldPoint(w.offsetFromPivot);

			Vector2 gasForce;
			if (w.steerable) {
				gasForce = new Vector2(0, 0);
			} else {
				gasForce = gasForce(w, axis);
			}

			drawVector(wheelPivot, gasForce, FORCE_DRAWING_SCALE,
					Color.GOLDENROD);

			Vector2 sideForce = sideForce(w.getDirectionVector().cpy(),
					chassis.getVelocity(wheelPivot));

			drawVector(wheelPivot, sideForce, FORCE_DRAWING_SCALE,
					Color.GOLDENROD);

			Vector2 force = gasForce.add(sideForce);

			if (force.len() > KAMMSCHER_KREIS_RADIUS) {
				force = force.nor().scl(KAMMSCHER_KREIS_RADIUS);
			}

			chassis.applyForce(force, wheelPivot);

		}

	}

	private Vector2 sideForce(Vector2 directionVector, final Vector2 velocity) {

		Vector2 normalDirectionVector = directionVector.rotate(90).nor();

		float slipAngle = normalDirectionVector.dot(velocity);

		Vector2 sideForce = normalDirectionVector.scl(1.1f * NORMAL_FORCE);

		if (slipAngle < 0) {
			return sideForce;
		} else if (slipAngle > 0) {
			return sideForce.scl(-1, -1);
		}
		return new Vector2(0, 0);

	}

	private Vector2 gasForce(Wheel w, float controllerYAxis) {
		return w.getDirectionVector().cpy()
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

	private void turnWheels(VehicleComponent vehicle, Body2DComponent chassis,
			float controllerXaxis) {

		float inputAngle = controllerXaxis * -MAXIMUM_WHEEL_ANGLE;

		adjustDrivingWheelsAngle(vehicle, chassis);

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

	private void adjustDrivingWheelsAngle(VehicleComponent vehicle,
			Body2DComponent chassis) {
		float chassisAngle = chassis.getAngleInDegrees();
		for (Wheel w : vehicle.getDrivenWheels()) {
			w.updateAngle(chassisAngle);
			drawVector(chassis.toWorldPoint(w.offsetFromPivot),
					w.getDirectionVector(), 2, Color.YELLOW);
		}

	}

	/**
	 * 
	 * @param inputAngle
	 * @param chassis
	 * @param vehicle
	 * @param axisIdx
	 *            0 for right, 1 for left
	 */
	private void setAckermannAngle(float inputAngle, Body2DComponent chassis,
			VehicleComponent vehicle, int axisIdx) {

		Wheel firstSteelWheel = vehicle.getSteerableWheels().get(axisIdx);

		Vector2 firstSteeringWheelPivot = chassis
				.toWorldPoint(firstSteelWheel.offsetFromPivot);

		renderer.setColor(Color.CHARTREUSE);
		renderer.circle(firstSteeringWheelPivot.x, firstSteeringWheelPivot.y,
				0.2f);

		Wheel drivingWheel = vehicle.getDrivenWheels().get(axisIdx);

		Vector2 drivingWheelPivot = chassis
				.toWorldPoint(drivingWheel.offsetFromPivot);

		renderer.setColor(Color.FIREBRICK);
		renderer.circle(drivingWheelPivot.x, drivingWheelPivot.y, 0.2f);

		float offsetX = -calcIntersectionPointOffsetX(inputAngle,
				firstSteeringWheelPivot, drivingWheelPivot);

		Vector2 intersectionPoint = chassis
				.toWorldPoint(drivingWheel.offsetFromPivot.cpy()
						.add(offsetX, 0));

		renderer.setColor(Color.GREEN);
		renderer.circle(intersectionPoint.x, intersectionPoint.y, 0.2f);

		// -------------------------------------------------------
		// second wheel stuff
		// -------------------------------------------------------

		int SECOND_WHEEL_INDEX = 1 - axisIdx;

		Wheel otherSteeringWheel = vehicle.getSteerableWheels().get(
				SECOND_WHEEL_INDEX);

		Vector2 otherSteeringWheelPivot = chassis
				.toWorldPoint(otherSteeringWheel.offsetFromPivot);

		renderer.setColor(Color.YELLOW);
		renderer.circle(intersectionPoint.x, intersectionPoint.y, 0.2f);

		renderer.setColor(Color.FIREBRICK);
		renderer.line(intersectionPoint, firstSteeringWheelPivot);
		renderer.line(drivingWheelPivot, otherSteeringWheelPivot);
		renderer.line(intersectionPoint, drivingWheelPivot);

		float chassisAngle = chassis.getAngleInDegrees();

		System.out.println("chassisAngle: " + chassisAngle);

		float otherAngle = 45
				- otherSteeringWheelPivot.cpy().sub(intersectionPoint).angle()
				+ 90 * (1 - axisIdx);

		otherAngle += chassisAngle;

		System.out.println("     INPUT ANGLE " + inputAngle + "   otherAngle: "
				+ otherAngle);

		firstSteelWheel.updateAngle(chassisAngle + inputAngle);

		otherSteeringWheel.updateAngle(chassisAngle + otherAngle);

		drawVector(firstSteeringWheelPivot,
				firstSteelWheel.getDirectionVector(), 2, Color.GREEN);
		drawVector(otherSteeringWheelPivot,
				otherSteeringWheel.getDirectionVector(), 2, Color.YELLOW);

		System.out.println("input "
				+ firstSteelWheel.getDirectionVector().angle() + " other "
				+ otherSteeringWheel.getDirectionVector().angle());

	}

	private float calcIntersectionPointOffsetX(float inputAngle,
			Vector2 firstSteeringWheelPivot, Vector2 drivingWheelPivot) {
		// Dreieck WSW Satz
		float alpha = Math.abs(inputAngle);
		// TODO: replace with Axis point this is constant!
		float b = firstSteeringWheelPivot.dst(drivingWheelPivot);
		float a = wswSatzRechtwinkligesDreieck(alpha, b);

		if (inputAngle > 0) {
			// steering left
			return a;
		} else {
			// steering right
			return -a;
		}
	}

	/**
	 * 
	 * @param alpha
	 * @param b
	 * @return
	 */
	private float wswSatzRechtwinkligesDreieck(float alpha, float b) {
		return (float) (Math.tan(alpha * MathUtils.degreesToRadians) * b);
	}

	private void rollingResistance(VehicleComponent vehicle,
			Body2DComponent chassis) {

		for (Wheel w : vehicle.getWheels()) {

			Vector2 wheelPivot = chassis.toWorldPoint(w.offsetFromPivot);

			Vector2 rollingResistanceForce = w.getDirectionVector().cpy()
					.scl(0.04f * NORMAL_FORCE);

			float angle = w.getDirectionVector().cpy()
					.dot(chassis.getVelocity(wheelPivot));

			if (angle > 0) {
				rollingResistanceForce.scl(-1, -1);
			} else if (angle < 0) {

			} else {
				rollingResistanceForce = new Vector2(0, 0);
			}

			drawVector(wheelPivot, rollingResistanceForce, FORCE_DRAWING_SCALE,
					Color.BLACK);

			chassis.applyForce(rollingResistanceForce, wheelPivot);

		}
	}

	private void drawVector(Vector2 pivot, Vector2 v, float scale, Color color) {
		renderer.setColor(color);
		// TODO: v.cpy() is it too cost expensive
		renderer.rectLine(pivot, pivot.cpy().add(v.cpy().scl(scale)), 0.025f);
	}
}
