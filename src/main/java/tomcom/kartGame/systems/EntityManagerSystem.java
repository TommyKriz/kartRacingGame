package tomcom.kartGame.systems;

import java.util.Dictionary;
import java.util.Hashtable;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;

import tomcom.kartGame.components.IDComponent;


public class EntityManagerSystem extends EntitySystem implements EntityListener{
	
	private static final Family FAMILY = Family.all(IDComponent.class).get();
	private Dictionary<Integer, Entity> entities;
	public EntityManagerSystem() {
		entities = new Hashtable<Integer,Entity>();
	}

	public Entity GetEntity(int id) {
		return entities.get(id);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		engine.addEntityListener(FAMILY, this);
	}

	@Override
	public void entityAdded(Entity arg0) {
		entities.put(arg0.getComponent(IDComponent.class).id, arg0);
		
	}

	@Override
	public void entityRemoved(Entity arg0) {
		entities.remove(arg0.getComponent(IDComponent.class).id);
		
	}

}
