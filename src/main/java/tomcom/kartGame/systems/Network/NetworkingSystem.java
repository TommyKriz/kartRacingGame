package tomcom.kartGame.systems.Network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.quantumreboot.ganet.Peer;

import tomcom.kartGame.components.IDComponent;
import tomcom.kartGame.components.NetworkIdentityComponent;
import tomcom.kartGame.components.PivotComponent;
import tomcom.kartGame.systems.EntityManagerSystem;
import tomcom.kartGame.systems.InputSystem;
import tomcom.kartGame.systems.Network.DataContainer.ForceInputData;
import tomcom.kartGame.systems.Network.DataContainer.NetworkTransferData;
import tomcom.kartGame.systems.Network.DataContainer.SpawnData;
import tomcom.kartGame.systems.Network.DataContainer.VehicleData;

public class NetworkingSystem extends EntitySystem implements EntityListener{

	private static final Family NETWORK_IDENTITIES = Family.all(NetworkIdentityComponent.class)
			.get();
	
	public Signal<SpawnData> onSpawnOnClient = new Signal<SpawnData>();
	public Signal<Peer> onSpawnOnServer = new Signal<Peer>();	
	
	private boolean isServer;
	private Server server = null;
	private Client client = null;
	private EntityManagerSystem entityManager;
	private GameControllerSystem gameController;
	private List<Entity> networkEntities;
	
	public NetworkingSystem(boolean isServer) {
		super();
		this.isServer = isServer;
		networkEntities = new ArrayList<Entity>();
	}
	
	public void ServerInput() {
		InputSystem is = getEngine().getSystem(InputSystem.class);
		is.onApplyForceInput.add(new Listener<ForceInputData>(){
			@Override
			public void receive(Signal<ForceInputData> arg0,
					ForceInputData arg1) {
				gameController.ApplyForceCommand(arg1.entityID, new Vector2(arg1.xAxis, arg1.yAxis));
				
			}			
		});
	}
	
	public void ClientInput() {
		InputSystem is = getEngine().getSystem(InputSystem.class);
		is.onApplyForceInput.add(new Listener<ForceInputData>(){

			@Override
			public void receive(Signal<ForceInputData> arg0,
					ForceInputData arg1) {
				client.createApplyForceCommand(Client.peer ,0, arg1);
			}
		});
	}
	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		entityManager = engine.getSystem(EntityManagerSystem.class);
		gameController = engine.getSystem(GameControllerSystem.class);
		engine.addEntityListener(NETWORK_IDENTITIES, this);
		if(isServer) {
			server = new Server(this);
			ServerInput();
			try {
				server.Start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			client = new Client(this);
			ClientInput();
			try {
				client.Start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if(server!=null)
			server.update(deltaTime);
		if(client!=null)
			client.update(deltaTime);
	}
	
	public List<NetworkTransferData> getDataPackets() {
		List<NetworkTransferData> dataPackets = new ArrayList<NetworkTransferData>();
		for(Entity e : networkEntities)
			dataPackets.add(gameController.GetVehicleData(e));
		return dataPackets;
		
	}
	public void newConnection(Peer peer) {
		onSpawnOnServer.dispatch(peer);
	}
	
	public void sendSpawnInfo(Peer spawnOwner, SpawnData data) {
		server.sendSpawnCommand(spawnOwner,data);
	}
	
	public void receiveSpawnInfo(int entityId, float x, float y, boolean localControl) {
		SpawnData data = new SpawnData(entityId, x, y);
		data.localControl = localControl;
		onSpawnOnClient.dispatch(data);
	}
	
	@Override
	public void removedFromEngine(Engine engine) {

		// TODO Auto-generated method stub
		super.removedFromEngine(engine);
		try {
			if(server!=null)
				server.cleanup();
			if(client!=null)
				client.cleanup();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void receivedApplyForceCommand(ForceInputData inputData) {
		
		gameController.ApplyForceCommand(inputData.entityID, new Vector2(inputData.xAxis, inputData.yAxis));
		
	}
	
	public void receiveVehicleData(int entityID, float xPos, float yPos, float xVel, float yVel, int xRot, int yRot) {
		gameController.UpdateVehicleData(entityID, xPos, yPos, xVel, yVel, xRot, yRot);
		
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


