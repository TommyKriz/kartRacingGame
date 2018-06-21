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
 * ausrollen (rückwärtsfahren) , zur nuller force lerpen BREMSEN ackermann
 * lenkung (differential?) driften schleudern reibungskoeffizient
 */
public class VehicleGamepadInputDebugRendererSystem extends IteratingSystem {

	private static final int MAXIMUM_WHEEL_ANGLE = 60;

	private static final int SEITENFUEHRUNGSKRAFT = 4000;

	private static final float MAXIMUM_GAS_FORCE = -6000f;

	private static final Family FAMILY = Family.all(VehicleComponent.class,
			Body2DComponent.class, GamepadInputComponent.class).get();

	private static final float DIRECTION_VECTOR_SCALE = 1f;

	private static final float KAMMSCHER_KREIS_RADIUS = 12000;

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
			//
			// sideForce(vehicle, chassis);

			// rollingResistance(vehicle, chassis);

		}

	}

	private void gasAndSideForce(VehicleComponent vehicle,
			Body2DComponent chassis, float axis) {

		for (Wheel w : vehicle.getWheels()) {

			Vector2 wheelPivot = chassis.toWorldPoint(w.offsetFromPivot);

			Vector2 force = gasForce(w, axis).add(sideForce());

			if (force.len() > KAMMSCHER_KREIS_RADIUS) {
				force = force.nor().scl(KAMMSCHER_KREIS_RADIUS);
			}

			chassis.applyForce(force, wheelPivot);

		}

	}

	private Vector2 gasForce(Wheel w, float controllerYAxis) {
		return w.getDirectionVector().cpy()
				.scl(-controllerYAxis * MAXIMUM_GAS_FORCE);
	}

	private Vector2 sideForce() {
		// TODO Auto-generated method stub
		return null;
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
		//
		otherSteeringWheel.updateAngle(chassisAngle + otherAngle);
		//
		drawVector(firstSteeringWheelPivot,
				firstSteelWheel.getDirectionVector(), 2, Color.PINK);
		drawVector(otherSteeringWheelPivot,
				otherSteeringWheel.getDirectionVector(), 2, Color.PINK);

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
			System.out.println("Steering L E F T");
			return a;
		} else {
			// steering right
			System.out.println("Steering R I G H T");
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
		// TODO Auto-generated method stub
		return (float) (Math.tan(alpha * MathUtils.degreesToRadians) * b);
	}

	private void gas(VehicleComponent vehicle, Body2DComponent chassis,
			float controllerYaxis) {

		// TODO: if yAxis == 0, letzteForce - %, lerp

		Vector2 wheelPivot;

		for (Wheel w : vehicle.getWheels()) {

			// TODO: take out
			drawVector(chassis.toWorldPoint(w.offsetFromPivot),
					w.getDirectionVector(), 1, Color.PINK);
			Gdx.app.log("############# G A S ################", ""
					+ w.getDirectionVector().len());

			chassis.applyForce(
					w.getDirectionVector().cpy()
							.scl(calcNormalForce(controllerYaxis, vehicle)),
					chassis.toWorldPoint(w.offsetFromPivot));

		}

	}

	private float calcNormalForce(float controllerYaxis,
			VehicleComponent vehicle) {

		float normalForce = (EntityConfig.KART_MASS / vehicle.getWheels().size);
		System.out.println("normalForce    " + normalForce);

		// Normalkraft (masse / anzahl der wheels) * 9.81 ist die Kraft wie
		// stark das Auto aufs Wheel drückt
		//
		// normalkraft * reibungskoeffizienten (~1.8)

		return -controllerYaxis * normalForce * 1.8f * 9.81f;
	}

	// TODO:! !!
	private void rollingResistance(VehicleComponent vehicle,
			Body2DComponent chassis) {
		Vector2 wheelPivot;

		Vector2 directionalVector;

		for (Wheel w : vehicle.getWheels()) {

			wheelPivot = chassis.toWorldPoint(w.offsetFromPivot);

			directionalVector = w.getDirectionVector().cpy();

			drawVector(wheelPivot, directionalVector, 1f, Color.GREEN);

			Gdx.app.log("#######################################", ""
					+ directionalVector.len());

			float rollingResistance = directionalVector.dot(chassis
					.getVelocity(wheelPivot));

			Gdx.app.log("Rolling Resistance", "" + rollingResistance);
			//
			// Vector2 rollingResistanceForce = directionalVector.scl(1, -1);
			//
			// if (rollingResistance > -0.0001 && rollingResistance < 0.0001) {
			// Gdx.app.log("Rolling Resistance", "     IS ZERO !!!!!!!");
			// rollingResistanceForce.scl(-1, -1);
			// }
			//
			// drawVector(wheelPivot, rollingResistanceForce, 1f, Color.GREEN);
			//
			// chassis.applyForce(rollingResistanceForce, wheelPivot);

		}
	}

	private void sideForce(VehicleComponent vehicle, Body2DComponent chassis) {
		Vector2 wheelPivot;
		for (Wheel w : vehicle.getWheels()) {

			wheelPivot = chassis.toWorldPoint(w.offsetFromPivot);

			// visualize direction vector
			drawVector(wheelPivot, w.getDirectionVector(),
					DIRECTION_VECTOR_SCALE, Color.FIREBRICK);

			Vector2 normalDirectionVector = w.getDirectionVector().cpy()
					.rotate(90);
			// visualize normal to direction vector
			drawVector(wheelPivot, normalDirectionVector,
					DIRECTION_VECTOR_SCALE, Color.YELLOW);

			// visualize velocity
			// drawVector(wheelPivot, chassis.getVelocity(wheelPivot), 1,
			// Color.BLUE);

			renderer.setColor(Color.GOLDENROD);
			renderer.line(wheelPivot, chassis.getPosition());

			float seitenfuehrungsSpeed = normalDirectionVector.dot(chassis
					.getVelocity(wheelPivot));

			Vector2 sideForce = normalDirectionVector.cpy().scl(
					SEITENFUEHRUNGSKRAFT);
			if (seitenfuehrungsSpeed < 0) {
				// sideForce = normalDirectionVector;
			} else if (seitenfuehrungsSpeed > 0) {
				sideForce.scl(-1, -1);
			} else {
				sideForce = new Vector2(0, 0);
			}
			// drawVector(wheelPivot, sideForce, 0.001f, Color.PINK);

			chassis.applyForce(sideForce, wheelPivot);

		}

	}

	private void drawVector(Vector2 pivot, Vector2 v, float scale, Color color) {
		renderer.setColor(color);
		// TODO: v.cpy() is it too cost expensive
		renderer.rectLine(pivot, pivot.cpy().add(v.cpy().scl(scale)), 0.025f);
	}
}
