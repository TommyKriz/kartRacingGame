package tomcom.kartGame.systems.Network;

import com.quantumreboot.ganet.Peer;

import tomcom.kartGame.systems.Network.DataContainer.CarData;
import tomcom.kartGame.systems.Network.DataContainer.InputData;
import tomcom.kartGame.systems.Network.DataContainer.SpawnData;

public interface ClientCommands {
	public void sendInputData(Peer peer, InputData input);
	public void receiveLevelData(int levelID);
	public void receiveStartRace();
	public void receiveSpawnData(SpawnData spawnData);
	public void receiveEndRace(int winnerId);
	public void receiveCarData(CarData spawnData);
}
