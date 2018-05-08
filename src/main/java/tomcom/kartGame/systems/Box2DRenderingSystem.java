package tomcom.kartGame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class Box2DRenderingSystem extends EntitySystem {

	private World world;

	private OrthographicCamera cam;

	private Box2DDebugRenderer debugRenderer;

	public Box2DRenderingSystem() {
		debugRenderer = new Box2DDebugRenderer();
	}

	@Override
	public void update(float deltaTime) {
		debugRenderer.render(world, cam.combined);
	}

	@Override
	public void addedToEngine(Engine engine) {
		world = engine.getSystem(Box2DPhysicsSystem.class).getWorld();
		cam = getEngine().getSystem(CameraSystem.class).getCamera();
	}

}
