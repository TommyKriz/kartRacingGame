package tomcom.kartGame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class Box2DRenderingSystem extends EntitySystem {

	private World world;

	// private OrthographicCamera worldCamera;

	private Box2DDebugRenderer debugRenderer;

	public Box2DRenderingSystem() {
		debugRenderer = new Box2DDebugRenderer(true, false, false, true, true,
				true);
	}

	@Override
	public void update(float deltaTime) {
		debugRenderer.render(world, getEngine().getSystem(CameraSystem.class)
				.getWorldCamera().combined);
	}

	@Override
	public void addedToEngine(Engine engine) {
		world = engine.getSystem(Box2DPhysicsSystem.class).getWorld();
		// worldCamera = getEngine().getSystem(CameraSystem.class)
		// .getWorldCamera();
	}

}
