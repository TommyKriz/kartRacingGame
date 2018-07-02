package tomcom.kartGame.systems;

import tomcom.kartGame.components.CheckpointCounterComponent;
import tomcom.kartGame.components.collision.CollisionListeningSystem;
import tomcom.kartGame.entities.EntityBuilder;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Contact;

public class CheckpointSystem extends IteratingSystem implements
		CollisionListeningSystem {

	private static final Family FAMILY = Family.all(
			CheckpointCounterComponent.class).get();

	// private ComponentMapper<CheckpointCounterComponent> cc = ComponentMapper
	// .getFor(CheckpointCounterComponent.class);

	public CheckpointSystem() {
		super(FAMILY);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		// TODO: make different for each map
		// TODO: maybe make a list for automatic counter increment or move to
		// EntityBuilder
		engine.addEntity(EntityBuilder.buildCheckpoint(-30.5f, 38, 0, 1));
		engine.addEntity(EntityBuilder.buildCheckpoint(-50.6f, 14.5f, 90, 2));
		engine.addEntity(EntityBuilder.buildCheckpoint(-63.9f, -20.6f, 0, 3));
		engine.addEntity(EntityBuilder.buildCheckpoint(-39.7f, -58.5f, 0, 4));
		engine.addEntity(EntityBuilder.buildCheckpoint(-12.2f, -22.1f, 90, 5));
		engine.addEntity(EntityBuilder.buildCheckpoint(25, -1.3f, 0, 6));
		engine.addEntity(EntityBuilder.buildCheckpoint(56.4f, 30.9f, 90, 7));
		engine.addEntity(EntityBuilder.buildCheckpoint(29.7f, 58.2f, 0, 8));
		engine.addEntity(EntityBuilder.buildCheckpoint(-19.5f, 38, 0, 9));
	}

	@Override
	public void onBeginContact(Contact contact) {

		System.out.println("Contact " + contact.getFixtureA().getUserData()
				+ " - " + contact.getFixtureB().getUserData());

	}

}
