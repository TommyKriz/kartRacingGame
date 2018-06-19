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

			// gas(vehicle, chassis, controller.getAxis(4));
			//
			// sideForce(vehicle, chassis);

			// rollingResistance(vehicle, chassis);

		}

	}

	// TODO: Lenkgeschwidnigkeit
	private void turnWheels(VehicleComponent vehicle, Body2DComponent chassis,
			float controllerXaxis) {
		// range from [-MWA, MWA]
		float inputAngle = controllerXaxis * -MAXIMUM_WHEEL_ANGLE;
		System.out.println("Input Angle " + inputAngle);

		kackermann(inputAngle, chassis, vehicle);

		// ackermann(inputAngle, chassis, vehicle);
	}

	private void kackermann(float inputAngle, Body2DComponent chassis,
			VehicleComponent vehicle) {

		if (inputAngle > 0) {

			Gdx.app.log("Ackermann", "STEERING LEFT  -  inputAngle: "
					+ inputAngle);
			ackermannLeft(inputAngle, chassis, vehicle);
		} else {

			Gdx.app.log("Ackermann", "STEERING RIGHT  -  inputAngle: "
					+ inputAngle);
			ackermannRight(inputAngle, chassis, vehicle);
		}

	}

	private void ackermannRight(float inputAngle, Body2DComponent chassis,
			VehicleComponent vehicle) {

		int AXIS_INDEX = 0;

		// TODO: automate picking inner wheel

		// pick right wheel as first wheel
		Wheel firstSteeringWheel = vehicle.getSteerableWheels().get(AXIS_INDEX);
		Vector2 firstSteeringWheelPivot = chassis
				.toWorldPoint(firstSteeringWheel.offsetFromPivot);

		drawVector(firstSteeringWheelPivot, firstSteeringWheel
				.getDirectionVector().cpy().rotate(inputAngle), 2, Color.PINK);

		// TODO: replace with axis center point ?
		Wheel drivingWheel = vehicle.getDrivenWheels().get(AXIS_INDEX);
		Vector2 drivingWheelPivot = chassis
				.toWorldPoint(drivingWheel.offsetFromPivot);

		float offsetX = calcIntersectionPointOffsetX(inputAngle,
				firstSteeringWheelPivot, drivingWheelPivot);

		Vector2 intersectionPoint = chassis
				.toWorldPoint(drivingWheel.offsetFromPivot.cpy()
						.add(offsetX, 0));

		renderer.circle(intersectionPoint.x, intersectionPoint.y, 0.4f);
		renderer.line(firstSteeringWheelPivot, intersectionPoint);
		renderer.line(drivingWheelPivot, intersectionPoint);
		// renderer.line(secondSteeringWheelPivot, intersectionPoint);

		drawVector(firstSteeringWheelPivot, firstSteeringWheel
				.getDirectionVector().cpy().rotate(inputAngle), 2, Color.PINK);

		// -------------------------------------------------------
		// second wheel stuff
		// -------------------------------------------------------

		int SECOND_WHEEL_INDEX = 1 - AXIS_INDEX;

		Wheel secondSteeringWheel = vehicle.getSteerableWheels().get(
				SECOND_WHEEL_INDEX);
		Vector2 secondSteeringWheelPivot = chassis
				.toWorldPoint(secondSteeringWheel.offsetFromPivot);

		renderer.line(secondSteeringWheelPivot, intersectionPoint);

		float secondAngle = 0;

		// TODO: take out they are constant
		float leftDrivingWheelOffsetX = vehicle.getDrivenWheels().get(1).offsetFromPivot.x;
		float rightDrivingWheelOffsetX = vehicle.getDrivenWheels().get(0).offsetFromPivot.x;

		// TODO: because origin is seen at driving car pivot as opposed to an
		// axis middle point
		// offsetX += rightDrivingWheelOffsetX;

		if (offsetX + rightDrivingWheelOffsetX > leftDrivingWheelOffsetX) {
			secondAngle = inputAngle;
		} else {

			Vector2 a = secondSteeringWheelPivot.cpy().sub(intersectionPoint);
			drawVector(secondSteeringWheelPivot, a, 3, Color.BLACK);
			Vector2 b = new Vector2(1, 0);
			drawVector(secondSteeringWheelPivot, b, 3, Color.BLACK);

			// float dot = a.cpy().nor().dot(b);
			// System.out.println("dot product: " + " acos of dot: "
			// + Math.acos(dot));
			// secondAngle = (float) (Math.acos(dot) /
			// MathUtils.degreesToRadians);

			// secondAngle = (float) (Math.atan2(b.y, b.x) - Math.atan2(a.y,
			// a.x));

			secondAngle = a.angle() - 90;

		}

		drawVector(secondSteeringWheelPivot, secondSteeringWheel
				.getDirectionVector().cpy().rotate(secondAngle), 2, Color.GREEN);

	}

	private void ackermannLeft(float inputAngle, Body2DComponent chassis,
			VehicleComponent vehicle) {

		int AXIS_INDEX = 1;

		// TODO: automate picking inner wheel

		// pick right wheel as first wheel
		Wheel firstSteeringWheel = vehicle.getSteerableWheels().get(AXIS_INDEX);
		Vector2 firstSteeringWheelPivot = chassis
				.toWorldPoint(firstSteeringWheel.offsetFromPivot);

		drawVector(firstSteeringWheelPivot, firstSteeringWheel
				.getDirectionVector().cpy().rotate(inputAngle), 2, Color.PINK);

		// TODO: replace with axis center point ?
		Wheel drivingWheel = vehicle.getDrivenWheels().get(AXIS_INDEX);
		Vector2 drivingWheelPivot = chassis
				.toWorldPoint(drivingWheel.offsetFromPivot);

		float offsetX = calcIntersectionPointOffsetX(inputAngle,
				firstSteeringWheelPivot, drivingWheelPivot);

		Vector2 intersectionPoint = chassis
				.toWorldPoint(drivingWheel.offsetFromPivot.cpy()
						.add(offsetX, 0));

		renderer.circle(intersectionPoint.x, intersectionPoint.y, 0.4f);
		renderer.line(firstSteeringWheelPivot, intersectionPoint);
		renderer.line(drivingWheelPivot, intersectionPoint);
		// renderer.line(secondSteeringWheelPivot, intersectionPoint);

		drawVector(firstSteeringWheelPivot, firstSteeringWheel
				.getDirectionVector().cpy().rotate(inputAngle), 2, Color.PINK);
	}

	private void ackermann(float inputAngle, Body2DComponent chassis,
			VehicleComponent vehicle) {

		// TODO: replace with axis center point ?
		Wheel drivingWheel = vehicle.getDrivenWheels().get(0);
		Vector2 drivingWheelPivot = chassis
				.toWorldPoint(drivingWheel.offsetFromPivot);

		// TODO: pick inner wheel as first wheel !
		Wheel firstSteeringWheel = vehicle.getSteerableWheels().get(0);
		Vector2 firstSteeringWheelPivot = chassis
				.toWorldPoint(firstSteeringWheel.offsetFromPivot);

		Wheel secondSteeringWheel = vehicle.getSteerableWheels().get(1);
		Vector2 secondSteeringWheelPivot = chassis
				.toWorldPoint(secondSteeringWheel.offsetFromPivot);

		float offsetX = calcIntersectionPointOffsetX(inputAngle,
				firstSteeringWheelPivot, drivingWheelPivot);

		Vector2 intersectionPoint = chassis
				.toWorldPoint(drivingWheel.offsetFromPivot.cpy()
						.add(offsetX, 0));

		renderer.circle(intersectionPoint.x, intersectionPoint.y, 0.4f);
		renderer.line(firstSteeringWheelPivot, intersectionPoint);
		renderer.line(drivingWheelPivot, intersectionPoint);
		renderer.line(secondSteeringWheelPivot, intersectionPoint);

		drawVector(firstSteeringWheelPivot, firstSteeringWheel
				.getDirectionVector().cpy().rotate(inputAngle), 2, Color.PINK);

		// TODO: replace witc
		//

		float nextAngle = 0;

		float leftDrivingWheelOffsetX = vehicle.getDrivenWheels().get(1).offsetFromPivot.x;
		float rightDrivingWheelOffsetX = vehicle.getDrivenWheels().get(0).offsetFromPivot.x;
		System.out.println(offsetX + " l  " + leftDrivingWheelOffsetX + " r "
				+ rightDrivingWheelOffsetX);

		// TODO: because origin is seen at driving car pivot as opposed to an
		// axis middle point
		offsetX += rightDrivingWheelOffsetX;

		nextAngle = drivingWheelPivot.cpy().sub(intersectionPoint).cpy()
				.dot(secondSteeringWheelPivot.cpy().sub(intersectionPoint));

		nextAngle = 90 - (-nextAngle);

		if (offsetX > leftDrivingWheelOffsetX
				&& offsetX < rightDrivingWheelOffsetX) {
			System.out.println("sdasdasds");
			nextAngle = inputAngle;
		}

		System.out.println("inputAngle: " + inputAngle + "  nextAngle: "
				+ nextAngle);

		drawVector(secondSteeringWheelPivot, secondSteeringWheel
				.getDirectionVector().cpy().rotate(nextAngle), 2, Color.PINK);

		// float alphaArcTan = (float) Math.atan2(a, b)
		// / MathUtils.degreesToRadians;
		//
		// System.out.println("alpha: " + alpha + " alphaArcTan: " +
		// alphaArcTan);
		//
		// renderer.circle(secondSteeringWheelPivot.x,
		// secondSteeringWheelPivot.y,
		// 0.4f);
		//

		//

		//
		// System.out.println("nextAngle: " + nextAngle);
		//
		// drawVector(firstSteeringWheelPivot,
		// firstSteeringWheel.getDirectionVector(), 2, Color.YELLOW);
		//
		// drawVector(secondSteeringWheelPivot,
		// secondSteeringWheel.getDirectionVector(), 2, Color.YELLOW);

		//
		// drawVector(intersectionPoint, new Vector2(0, 1).rotate(-nextAngle),
		// 2,
		// Color.GOLD);

		// TODO: https://www.quora.com/How-do-I-calculate-Ackerman-angle#
		// https://www.volksbot.de/ackermann-de.php
		// https://de.wikipedia.org/wiki/Momentanpol

		// // // // // TODO:
		// firstSteeringWheel.orientation = chassis.getAngleInRadians()
		// / MathUtils.degreesToRadians + inputAngle + 90;
		// firstSteeringWheel.getDirectionVector().setAngle(
		// firstSteeringWheel.orientation);

	}

	// for (Wheel w : vehicle.getSteerableWheels()) {
	// // TODO: +90?
	// // TODO: take out chassis degrad
	// w.orientation = maximumAngle;
	// w.getDirectionVector().setAngle(w.orientation);
	//
	//

	//
	// // Vector2 intersectionPoint = intersect(wheelPivot,
	// // w.getDirectionVector(), drivingWheelPivot,
	// // drivingWheel.getDirectionVector());
	//

	//

	//
	// drawLine(wheelPivot, chassis.toLocalPoint(intersectionPoint),
	// Color.SKY);
	// }

	// private void drawLine(Vector2 point1, Vector2 point2, Color color) {
	// renderer.setColor(color);
	// renderer.line(point1, point2);
	// }

	//
	// /**
	// * interpret x as k, y as d.
	// *
	// * @param line1
	// * @param line2
	// * @return
	// */
	// private Vector2 intersect(final Vector2 startingPoint1,
	// final Vector2 directionalVector1, final Vector2 startingPoint2,
	// Vector2 directionalVector2) {
	// float dx = startingPoint2.x - startingPoint1.x;
	// float dy = startingPoint2.y - startingPoint1.y;
	// Gdx.app.log("inetrsect", dx + "|" + dy);
	//
	// float det = directionalVector2.x * directionalVector1.y
	// - directionalVector2.y * directionalVector1.x;
	// float u = (dy * directionalVector2.x - dx * directionalVector2.y) / det;
	// float v = (dy * directionalVector1.x - dx * directionalVector1.y) / det;
	// Gdx.app.log("inetrsect", u + "|" + v);
	// return new Vector2(u, v);
	// }

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

		for (Wheel w : vehicle.getDrivenWheels()) {

			// w.orientation = angle;
			// w.getDirectionVector().setAngle(angle + 90);

			w.getDirectionVector().setAngle(
					chassis.getAngleInRadians() / MathUtils.degreesToRadians
							+ 90);

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
