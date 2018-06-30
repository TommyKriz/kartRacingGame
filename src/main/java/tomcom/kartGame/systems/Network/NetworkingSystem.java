package tomcom.kartGame.systems.Network;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;

import tomcom.kartGame.components.NetworkIdentityComponent;
import tomcom.kartGame.systems.EntityManagerSystem;
import tomcom.kartGame.systems.Network.DataContainer.NetworkTransferData;

public class NetworkingSystem extends EntitySystem implements EntityListener{

	private static final Family NETWORK_IDENTITIES = Family.all(NetworkIdentityComponent.class)
			.get();

	private EntityManagerSystem entityManager;
	protected GameCommandSystem gameCommandSystem;
	protected List<Entity> networkEntities;
	
	public NetworkingSystem() {
		super();
		networkEntities = new ArrayList<Entity>();
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		entityManager = engine.getSystem(EntityManagerSystem.class);
		gameCommandSystem = engine.getSystem(GameCommandSystem.class);
		engine.addEntityListener(NETWORK_IDENTITIES, this);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
	}

	
	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);
	}
	
	@Override
	public void entityAdded(Entity arg0) {
		networkEntities.add(arg0);	
	}

	@Override
	public void entityRemoved(Entity arg0) {
		networkEntities.remove(arg0);	
	}

}


