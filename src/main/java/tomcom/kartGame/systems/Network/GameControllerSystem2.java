package tomcom.kartGame.systems.Network;

import tomcom.kartGame.components.IDComponent;
import tomcom.kartGame.components.PivotComponent;
import tomcom.kartGame.systems.EntityManagerSystem;
import tomcom.kartGame.systems.Network.DataContainer.ControllerInputData;
import tomcom.kartGame.systems.Network.DataContainer.VehicleData;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class GameControllerSystem2 extends EntitySystem {

	private EntityManagerSystem entityManager;

	private ComponentMapper<ControllerInputComponent> cc = ComponentMapper
			.getFor(ControllerInputComponent.class);

	private ComponentMapper<PivotComponent> pc = ComponentMapper
			.getFor(PivotComponent.class);

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		entityManager = engine.getSystem(EntityManagerSystem.class);
	}

	public void receiveInput(int entityId, float xAxis, float yAxis) {
		Entity entity = entityManager.GetEntity(entityId);
		cc.get(entity).updateInput(xAxis, yAxis);
	}

	public ControllerInputData sendInput(int entityId, float xAxis, float yAxis) {
		return new ControllerInputData(entityId, xAxis, yAxis);
	}

	public void UpdateVehicleData(int entityID, float xPos, float yPos,
			float xVel, float yVel, int xRot, int yRot) {
		Entity entity = entityManager.GetEntity(entityID);
		Gdx.app.log("GAmeControllerSystem", "entityId: " + entityID
				+ "  entity: " + entity);
		entity.getComponent(PivotComponent.class).setPos(
				new Vector2(xPos, yPos));
	}

	public VehicleData GetVehicleData(Entity entity) {
		PivotComponent pivot = entity.getComponent(PivotComponent.class);
		int id = entity.getComponent(IDComponent.class).id;
		float xPos = pivot.getPos().x;
		float yPos = pivot.getPos().y;

		return new VehicleData(id, xPos, yPos, xVel, yVel, xRot, yRot);

		// Body2DComponent body = bc.get(entity);
		// PivotComponent pivot = pc.get(entity);
		// pivot.setPos(body.getPosition(), body.getAngleInDegrees());

	}

}
