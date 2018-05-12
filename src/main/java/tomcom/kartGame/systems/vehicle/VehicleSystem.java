package tomcom.kartGame.systems.vehicle;

import tomcom.kartGame.components.physics.Body2DComponent;
import tomcom.kartGame.components.vehicle.VehicleComponent;
import tomcom.kartGame.components.vehicle.Wheel;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;

public class VehicleSystem extends IteratingSystem {

	private static final Family FAMILY = Family.all(VehicleComponent.class,
			Body2DComponent.class).get();

	ComponentMapper<VehicleComponent> vm = ComponentMapper
			.getFor(VehicleComponent.class);
	ComponentMapper<Body2DComponent> bm = ComponentMapper
			.getFor(Body2DComponent.class);

	public VehicleSystem() {
		super(FAMILY);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

		Body2DComponent chassis = bm.get(entity);

		VehicleComponent vehicle = vm.get(entity);

		Array<Wheel> wheels = vehicle.getWheels();

		float normalForce = calcNormalForce(chassis, wheels.size);

		for (Wheel w : wheels) {

		}

	}

	private float calcNormalForce(Body2DComponent chassis, int numberOfWheels) {
		return (chassis.getDamping() / numberOfWheels) * 9.81f;
	}

}
