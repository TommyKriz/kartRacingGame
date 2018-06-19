package tomcom.kartGame.systems.vehicle;

import tomcom.kartGame.components.PivotComponent;
import tomcom.kartGame.components.vehicle.VehicleComponent;
import tomcom.kartGame.components.vehicle.Wheel;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

// TODO: Ackermann, Differential
@Deprecated
public class WheelSystem extends IteratingSystem {

	private static final Family FAMILY = Family.all(VehicleComponent.class,
			PivotComponent.class).get();

	ComponentMapper<VehicleComponent> vm = ComponentMapper
			.getFor(VehicleComponent.class);
	ComponentMapper<PivotComponent> pm = ComponentMapper
			.getFor(PivotComponent.class);

	public WheelSystem() {
		super(FAMILY);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		VehicleComponent vehicle = vm.get(entity);

		float angle = pm.get(entity).getPos().z;

		// System.out.println(angle);

		for (Wheel w : vehicle.getDrivenWheels()) {
			w.orientation = angle;
			w.getDirectionVector().setAngle(angle + 90);
		}

	}
}
