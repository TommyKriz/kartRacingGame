package tomcom.kartGame.systems.Network;

import java.util.List;

import com.quantumreboot.ganet.Peer;

import tomcom.kartGame.systems.Network.DataContainer.CarData;
import tomcom.kartGame.systems.Network.DataContainer.NetworkTransferData;
import tomcom.kartGame.systems.Network.DataContainer.SpawnData;

public interface ServerCommands {
	
	public void receivedNewConnection(Peer peer);
	public void receivedInputData(int entityId, float xAxis, float yAxis);
	public List<CarData> getCarData();
	public void sendCarData(CarData data);
	public void sendLevelData(int levelID);
	public void sendStartRace();
	public void sendEndRace(int winnerId);
	public void sendSpawnCommand(Peer owner, SpawnData spawnData);
}
