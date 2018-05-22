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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class WheelDebugRendererSystem extends IteratingSystem {

	private static final int DIRECTION_VECTOR_SCALE = 4;

	private static final Family FAMILY = Family.all(VehicleComponent.class,
			PivotComponent.class).get();

	ComponentMapper<VehicleComponent> vm = ComponentMapper
			.getFor(VehicleComponent.class);
	ComponentMapper<PivotComponent> pm = ComponentMapper
			.getFor(PivotComponent.class);

	ShapeRenderer renderer;

	public WheelDebugRendererSystem() {
		super(FAMILY);
		renderer = new ShapeRenderer();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

		VehicleComponent vehicle = vm.get(entity);

		Array<Wheel> wheels = vehicle.getWheels();

		renderer.setProjectionMatrix(getEngine().getSystem(CameraSystem.class)
				.getProjectionMatrix());

		renderer.begin(ShapeType.Line);

		Vector2 entityPivot = pm.get(entity).getPos();

		float wx;
		float wy;

		for (Wheel w : wheels) {

			Gdx.app.log("WheelDebugRendererSystem",
					" ENTITY PIVOT (World Coords) " + entityPivot.toString());

			wx = entityPivot.x + w.xOffsetFromPivot;
			wy = entityPivot.y + w.yOffsetFromPivot;

			renderer.setColor(Color.FIREBRICK);
			renderer.line(wx, wy, wx + w.getDirectionVector().x
					* DIRECTION_VECTOR_SCALE, wy + w.getDirectionVector().y
					* DIRECTION_VECTOR_SCALE);
			renderer.setColor(Color.GOLDENROD);
			renderer.line(wx, wy, entityPivot.x, entityPivot.y);
		}

		renderer.end();
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		// renderer.setProjectionMatrix(engine.getSystem(CameraSystem.class)
		// .getProjectionMatrix());
	}

	// TODO: necessary?
	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);
		renderer.dispose();
	}

}
