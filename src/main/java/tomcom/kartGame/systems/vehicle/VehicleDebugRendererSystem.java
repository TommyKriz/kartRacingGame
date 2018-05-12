package tomcom.kartGame.systems.vehicle;

import tomcom.kartGame.components.vehicle.VehicleComponent;
import tomcom.kartGame.components.vehicle.Wheel;
import tomcom.kartGame.systems.CameraSystem;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;

public class VehicleDebugRendererSystem extends IteratingSystem {

	private static final Family FAMILY = Family.all(VehicleComponent.class)
			.get();

	ComponentMapper<VehicleComponent> vm = ComponentMapper
			.getFor(VehicleComponent.class);

	ShapeRenderer renderer;

	public VehicleDebugRendererSystem() {
		super(FAMILY);
		renderer = new ShapeRenderer();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

		VehicleComponent vehicle = vm.get(entity);

		Array<Wheel> wheels = vehicle.getWheels();

		renderer.begin(ShapeType.Line);

		for (Wheel w : wheels) {
			/*
			 * this demonstrates using LOCAL Coords, as the origin 0,0 is the
			 * pivot of the entity.
			 */
			renderer.line(w.xOffsetFromPivot, w.yOffsetFromPivot, 0, 0);
		}

		renderer.end();
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		renderer.setProjectionMatrix(engine.getSystem(CameraSystem.class)
				.getProjectionMatrix());
	}

	// TODO: necessary?
	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);
		renderer.dispose();
	}

}
