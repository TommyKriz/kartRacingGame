package tomcom.kartGame.systems.Network;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;

import tomcom.kartGame.components.PivotComponent;
import tomcom.kartGame.systems.EntityManagerSystem;

public class GameCommandSystem extends EntitySystem{

	private EntityManagerSystem entityManager;

	private ComponentMapper<PivotComponent> pc = ComponentMapper
			.getFor(PivotComponent.class);
	

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		entityManager = engine.getSystem(EntityManagerSystem.class);
	}



}
