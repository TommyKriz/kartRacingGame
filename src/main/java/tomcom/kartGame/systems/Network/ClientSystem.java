package tomcom.kartGame.systems.Network;

import java.io.IOException;

import com.badlogic.ashley.core.Engine;
import com.quantumreboot.ganet.MessageQuality;
import com.quantumreboot.ganet.Peer;

import tomcom.kartGame.systems.Network.Commands.SendForceCommand;
import tomcom.kartGame.systems.Network.DataContainer.CarData;
import tomcom.kartGame.systems.Network.DataContainer.InputData;
import tomcom.kartGame.systems.Network.DataContainer.SpawnData;

public class ClientSystem extends NetworkingSystem implements ClientCommands{

	private Client client;
	
	public ClientSystem() {
		super();
		client = new Client(this);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		try {
			client.Start();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		client.commands.add(new SendForceCommand(client.time, 
				peer, input, MessageQuality.RELIABLE_ORDERED));
		
	}
	@Override
	public void receiveLevelData(int levelID) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void receiveStartRace() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void receiveSpawnData(SpawnData spawnData) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void receiveEndRace(int winnerId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void receiveCarData(CarData spawnData) {
		// TODO Auto-generated method stub
		
	}
}
