package tomcom.kartGame.systems.Network;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import tomcom.kartGame.components.IDComponent;
import tomcom.kartGame.components.PivotComponent;
import tomcom.kartGame.systems.Box2DPhysicsSystem;
import tomcom.kartGame.systems.EntityManagerSystem;
import tomcom.kartGame.systems.Network.DataContainer.VehicleData;

public class GameControllerSystem extends EntitySystem{

	private Box2DPhysicsSystem physics;
	private EntityManagerSystem entityManager;
	private final float FORCE_MULTIPLIER = 10f;
	
	@Override
	public void addedToEngine(Engine engine) {
		// TODO Auto-generated method stub
		super.addedToEngine(engine);
		physics = engine.getSystem(Box2DPhysicsSystem.class);
		entityManager = engine.getSystem(EntityManagerSystem.class);
	}
	
	public void ApplyForceCommand(int entityID, Vector2 force) {
		Entity entity = entityManager.GetEntity(entityID);
		Gdx.app.log("GameController", "Apply Force to " +entityID+" "+force);
		physics.ApplyForce(entity, new Vector2(force.x * FORCE_MULTIPLIER, force.y * FORCE_MULTIPLIER));
		
	}
	public void UpdateVehicleData(int entityID, float xPos, float yPos, float xVel, float yVel, int xRot, int yRot) {
		Entity entity = entityManager.GetEntity(entityID);
		entity.getComponent(PivotComponent.class).setPos(new Vector2(xPos,yPos));
	}
	public VehicleData GetVehicleData(Entity entity) {
		PivotComponent pivot = entity.getComponent(PivotComponent.class);
		int id = entity.getComponent(IDComponent.class).id;
		float xPos = pivot.getPos().x;
		float yPos = pivot.getPos().y;
		float xVel = 0;
		float yVel = 0;
		int xRot = 0;
		int yRot = 0;
		return new VehicleData(id, xPos, yPos, xVel, yVel, xRot, yRot);
	}

	
}
