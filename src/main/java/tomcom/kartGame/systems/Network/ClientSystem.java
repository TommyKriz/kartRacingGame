package tomcom.kartGame.systems.Network;

import java.io.IOException;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.quantumreboot.ganet.MessageQuality;
import com.quantumreboot.ganet.Peer;

import tomcom.kartGame.systems.Network.Commands.SendInputCommand;
import tomcom.kartGame.systems.Network.Commands.SendReceivedLevelData;
import tomcom.kartGame.systems.Network.DataContainer.CarData;
import tomcom.kartGame.systems.Network.DataContainer.InputData;
import tomcom.kartGame.systems.Network.DataContainer.SpawnData;

public class ClientSystem extends NetworkingSystem implements ClientCommands{

	public static Signal<Object> onDispose = new Signal<Object>();
	private Client client;
	public Peer peer;
	private static final int DEFAULT_PORT = 54321;
	private static final String DEFAULT_HOST = "localhost";//192.168.0.118
	private int entityId=-1;
	private boolean raceStarted = false;
	
	public ClientSystem() {
		this(DEFAULT_HOST, DEFAULT_PORT);
	}
	public ClientSystem(String ip, int port) {
		super();
		client = new Client(this, ip, port);
		onDispose.add(new Listener<Object>() {

			@Override
			public void receive(Signal<Object> signal, Object object) {
				try {
					Gdx.app.log("ClientSystem", "Dispose Client!");
					client.cleanup();
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
			client.Start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		onInputReceived.add(new Listener<Vector2>() {

			@Override
			public void receive(Signal<Vector2> signal, Vector2 object) {
				sendInputData(peer,new InputData(entityId,object.x, object.y));
				
			}
			
		});
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		client.update(deltaTime);
	}
	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);
		try {
			client.cleanup();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	@Override
	public void sendInputData(Peer peer, InputData input) {
		if(raceStarted)
			client.commands.add(new SendInputCommand(client.time, 
					peer, input, MessageQuality.RELIABLE_ORDERED));
		
	}
	@Override
	public void receiveLevelData(int levelID) {
		loadLevelReceived.dispatch(levelID);
		client.commands.add(new SendReceivedLevelData(client.time, 
				peer, MessageQuality.RELIABLE_ORDERED));
		
	}
	@Override
	public void receiveStartRace() {
		raceStarted = true;
	}
	@Override
	public void receiveSpawnData(SpawnData spawnData) {
		if(spawnData.localControl)
			entityId= spawnData.entityID;
		Gdx.app.log("ClientSystem", "Received Spawn: "+spawnData.entityID+" "+spawnData.localControl);
		onSpawn.dispatch(spawnData);
		
	}
	@Override
	public void receiveEndRace(int winnerId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void receiveCarData(CarData carData) {
	
		onCarDataReceived.dispatch(carData);
		
	}
	@Override
	public void connected(Peer peer) {
		this.peer = peer;
		Gdx.app.log("ClientSystem", "Connected!");
		ClientCommands.onConnected.dispatch(0);
		
	}
}
