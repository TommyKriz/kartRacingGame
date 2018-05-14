package tomcom.kartGame.systems.vehicle;

import tomcom.kartGame.components.PivotComponent;
import tomcom.kartGame.components.vehicle.VehicleComponent;
import tomcom.kartGame.components.vehicle.Wheel;
import tomcom.kartGame.systems.CameraSystem;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class VehicleDebugRendererSystem extends IteratingSystem {

	private static final int DIRECTION_VECTOR_SCALE = 4;

	private static final Family FAMILY = Family.all(VehicleComponent.class,
			PivotComponent.class).get();

	ComponentMapper<VehicleComponent> vm = ComponentMapper
			.getFor(VehicleComponent.class);
	ComponentMapper<PivotComponent> pm = ComponentMapper
			.getFor(PivotComponent.class);

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

		Vector2 entityPivot = pm.get(entity).getPos();
		float wx;
		float wy;

		for (Wheel w : wheels) {
			/*
			 * this demonstrates using LOCAL Coords, as the origin 0,0 is the
			 * pivot of the entity.
			 */

			w.updatePos(entityPivot);

			System.out.println(" WORLD PIVOT " + entityPivot.toString());

			System.out.println(" WHEEL PIVOT " + w.pos);

			// System.out.println(" WHEEL OFFSET " + w.xOffsetFromPivot + "|"
			// + w.yOffsetFromPivot);

			// wx = worldPivot.x + w.xOffsetFromPivot;
			// wy = worldPivot.y + w.yOffsetFromPivot;

			// System.out.println(" WHEEL PIVOT " + wx + "|" + wy);

			// renderer.line(wx, wy, wx + w.getDirectionVector().x
			// * DIRECTION_VECTOR_SCALE, wy + w.getDirectionVector().y
			// * DIRECTION_VECTOR_SCALE);

			renderer.line(w.pos.x, w.pos.y, 0, 0);
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
