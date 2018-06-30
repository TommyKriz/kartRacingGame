package tomcom.kartGame.systems.Network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.quantumreboot.ganet.MessageQuality;
import com.quantumreboot.ganet.Peer;

import tomcom.kartGame.components.IDComponent;
import tomcom.kartGame.components.PivotComponent;
import tomcom.kartGame.systems.InputSystem;
import tomcom.kartGame.systems.Network.Commands.SendCarDataCommand;
import tomcom.kartGame.systems.Network.Commands.SendSpawnCommand;
import tomcom.kartGame.systems.Network.DataContainer.CarData;
import tomcom.kartGame.systems.Network.DataContainer.InputData;
import tomcom.kartGame.systems.Network.DataContainer.NetworkTransferData;
import tomcom.kartGame.systems.Network.DataContainer.SpawnData;

public class ServerSystem extends NetworkingSystem implements ServerCommands{

	public static final double DATA_UPDATE_DELAY = 0.05;
	private double lastUpdateTime;
	
	private Server server;
	private List<Peer> peers = new ArrayList<Peer>();
	
	public ServerSystem() {
		super();
		server = new Server(this);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		try {
			server.Start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		server.update(deltaTime);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receivedInputData(int entityId, float xAxis, float yAxis) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendStartRace() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendEndRace(int winnerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendSpawnCommand(Peer owner, SpawnData spawnData) {
		for(Peer p : peers) {
			Gdx.app.log("Server","Sending SpawnInfo to Client: " + p);
			if(p == owner) {
				spawnData.localControl = true;
			}
			server.commands.add(new SendSpawnCommand(0, 
				p, spawnData, MessageQuality.RELIABLE_ORDERED));
		}
		
	}
	@Override
	public void sendCarData(CarData data) {
		for(Peer p : peers) {
			server.commands.add(new SendCarDataCommand(0, p, data, MessageQuality.UNRELIABLE));
		}
		
	}

}
