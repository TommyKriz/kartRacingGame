package tomcom.kartGame.components.collision;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Box2DCollisionListener implements ContactListener {

	private final List<CollisionListeningSystem> collisionListeners;

	public Box2DCollisionListener(CollisionListeningSystem... listeningSystem) {
		collisionListeners = new ArrayList<>();
		for (CollisionListeningSystem system : listeningSystem) {
			collisionListeners.add(system);
		}

	}

	@Override
	public void beginContact(Contact contact) {
		for (CollisionListeningSystem system : collisionListeners) {
			system.onBeginContact(contact);
		}
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
