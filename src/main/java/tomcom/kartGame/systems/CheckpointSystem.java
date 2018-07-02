package tomcom.kartGame.systems;

import java.util.ArrayList;
import java.util.List;

import tomcom.kartGame.components.CheckpointComponent;
import tomcom.kartGame.components.CheckpointCounterComponent;
import tomcom.kartGame.components.collision.CollisionListeningSystem;
import tomcom.kartGame.entities.EntityBuilder;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;

public class CheckpointSystem extends EntitySystem implements
		CollisionListeningSystem {

	// private ComponentMapper<CheckpointCounterComponent> cc = ComponentMapper
	// .getFor(CheckpointCounterComponent.class);

	private int NUM_OF_CHECKPOINTS;

	private List<Vector2> initialCheckpointCoords;

	public CheckpointSystem(List<Vector2> checkpointCoords) {
		NUM_OF_CHECKPOINTS = checkpointCoords.size();
		initialCheckpointCoords = checkpointCoords;
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		int i = 0;
		for (Vector2 p : initialCheckpointCoords) {
			i++;
			engine.addEntity(EntityBuilder.buildCheckpoint(p.x, p.y, 0, i));
		}
	}

	@Override
	public void onBeginContact(Contact contact) {

		Object collisionData1 = contact.getFixtureA().getUserData();
		Object collisionData2 = contact.getFixtureB().getUserData();

		CheckpointCounterComponent checkpointCounter = null;
		CheckpointComponent checkpoint = null;

		boolean kartCollidesWithCheckpoint = false;

		if (collisionData1 instanceof CheckpointCounterComponent
				&& collisionData2 instanceof CheckpointComponent) {
			checkpointCounter = (CheckpointCounterComponent) collisionData1;
			checkpoint = (CheckpointComponent) collisionData2;
			kartCollidesWithCheckpoint = true;
		} else if (collisionData2 instanceof CheckpointCounterComponent
				&& collisionData1 instanceof CheckpointComponent) {
			checkpointCounter = (CheckpointCounterComponent) collisionData2;
			checkpoint = (CheckpointComponent) collisionData1;
			kartCollidesWithCheckpoint = true;
		}

		if (kartCollidesWithCheckpoint) {
			System.out.println(" kart collides with checkpoint");
			if (checkpointCounter.passedCheckpoints + 1 == checkpoint.getId()) {
				checkpointCounter.increment();
			}

			System.out.println(" asöodkfadklsfhjadsll    _> "
					+ checkpointCounter.passedCheckpoints);

			if (checkpointCounter.passedCheckpoints == NUM_OF_CHECKPOINTS) {
				Gdx.app.log("CheckpointSystem", "PLAYER WON");
			}

		}

	}
}
