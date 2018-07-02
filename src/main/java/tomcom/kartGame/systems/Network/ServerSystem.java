package tomcom.kartGame.systems.Network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.quantumreboot.ganet.MessageQuality;
import com.quantumreboot.ganet.Peer;

import tomcom.kartGame.components.IDComponent;
import tomcom.kartGame.components.PivotComponent;
import tomcom.kartGame.systems.Network.Commands.SendCarDataCommand;
import tomcom.kartGame.systems.Network.Commands.SendLevelDataCommand;
import tomcom.kartGame.systems.Network.Commands.SendRaceStartCommand;
import tomcom.kartGame.systems.Network.Commands.SendSpawnCommand;
import tomcom.kartGame.systems.Network.DataContainer.CarData;
import tomcom.kartGame.systems.Network.DataContainer.InputData;
import tomcom.kartGame.systems.Network.DataContainer.SpawnData;
import tomcom.kartGame.systems.vehicle.VehicleGamepadInputDebugRendererSystem;

public class ServerSystem extends NetworkingSystem implements ServerCommands{

	public static Signal<Object> onDispose = new Signal<Object>();
	private static int counter = 0;
	public static final double DATA_UPDATE_DELAY = 0.05;
	private double lastUpdateTime;
	
	private boolean raceStarted=false;
	private Server server;
	private List<Peer> receivedLevel=new ArrayList<Peer>();
	private HashMap<Integer,Peer> peers = new HashMap<Integer,Peer>();
	
	public ServerSystem() {
		super();
		server = new Server(this);
		onDispose.add(new Listener<Object>() {

			@Override
			public void receive(Signal<Object> signal, Object object) {
				try {
					Gdx.app.log("ServerSystem", "Dispose Server!");
					server.cleanup();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		});
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		try {
			server.Start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		onLoadLevel.add(new Listener<Integer>() {

			@Override
			public void receive(Signal<Integer> arg0, Integer arg1) {
				sendLevelData(arg1);
				
			}
			
		});
		onStartRace.add(new Listener<Object>() {

			@Override
			public void receive(Signal<Object> signal, Object object) {
				sendStartRace();
				
			}
			
		});
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		server.update(deltaTime);
		if(raceStarted)
		if(server.time >= DATA_UPDATE_DELAY + lastUpdateTime) {

			lastUpdateTime = server.time;
			List<CarData> data = getCarData();
			for(CarData packet : data) {
				sendCarData(packet);
			}
		}
	}
	
	
	
	
	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);
		try {
			server.cleanup();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	@Override
	public void receivedNewConnection(Peer peer) {
		counter++;
		peers.put(counter, peer);
		Gdx.app.log("ServerSystem", "New Client Connection: " + counter);
		onNewConnection.dispatch(counter);
	}

	@Override
	public void receivedInputData(int entityId, float xAxis, float yAxis) {
//		Gdx.app.log("ServerSystem", "Received Input Data: " +entityId);
		VehicleGamepadInputDebugRendererSystem.onInputReceived.dispatch(new InputData(entityId, xAxis, yAxis));
		
	}

	@Override
	public List<CarData> getCarData() {
		List<CarData> dataPackets = new ArrayList<CarData>();
		for(Entity e : networkEntities) {
			int id = e.getComponent(IDComponent.class).id;
			PivotComponent pivot = e.getComponent(PivotComponent.class);
			dataPackets.add(new CarData(id, pivot.getPos().x, pivot.getPos().y, pivot.getPos().z));
		}
		return dataPackets;	
	}

	@Override
	public void sendLevelData(int levelID) {
		for(Peer p : peers.values()) {
			server.commands.add(new SendLevelDataCommand(0, p, levelID, MessageQuality.RELIABLE));
		}
	}

	@Override
	public void sendStartRace() {
		raceStarted = true;
		for(Peer p : peers.values()) {
			server.commands.add(new SendRaceStartCommand(0, p, MessageQuality.RELIABLE));
		}
		
	}

	@Override
	public void sendEndRace(int winnerId) {
		// TODO Auto-generated method stub
		
	}
	public void sendSpawnServerOnly() {
		
		for(int playerId : peers.keySet()) {
//			Gdx.app.log("ServerSystem", "PlayerId: "+playerId);
			SpawnData spawnData = new SpawnData(playerId,2+4*playerId,2);
			onSpawn.dispatch(spawnData);
			
		}
	}
	public void sendSpawns() {
		sendSpawnCommand(null, new SpawnData(0,2,2));//Server Cart
		for(Integer playerId : peers.keySet()) {
//			Gdx.app.log("ServerSystem", "PlayerId: "+playerId);
			SpawnData spawnData = new SpawnData(playerId,2+4*playerId,2);
//			Gdx.app.log("ServerSystem", "PlayerId: "+playerId);
//			Gdx.app.log("ServerSystem", "Peer: "+ peers.get(playerId));
			sendSpawnCommand(peers.get(playerId), spawnData);
			
		}
	}

	@Override
	public void sendSpawnCommand(Peer owner, SpawnData spawnData) {
		
		for(Peer p : peers.values()) {
			SpawnData newData= new SpawnData(spawnData.entityID, spawnData.x, spawnData.y);
//			Gdx.app.log("Server","Sending SpawnInfo to Client: " + p);
//			Gdx.app.log("ServerSystem", "Send SpawnData to peer: "+p.toString());
			if(owner!=null && p == owner) {
				newData.localControl = true;
//				Gdx.app.log("ServerSystem", "TRUE");
			}
			else {
				newData.localControl = false;
//				Gdx.app.log("ServerSystem", "FALSE");
			}
			server.commands.add(new SendSpawnCommand(0, 
				p, newData, MessageQuality.RELIABLE));
		}
		
	}
	@Override
	public void sendCarData(CarData data) {
		for(Peer p : peers.values()) {
			server.commands.add(new SendCarDataCommand(0, p, data, MessageQuality.UNRELIABLE));
		}
		
	}
	
	@Override
	public void receivedLevelDataReceived(Peer sender) {
		receivedLevel.add(sender);
		if(receivedLevel.size()==peers.values().size())
			sendSpawns();
		
	}

}
