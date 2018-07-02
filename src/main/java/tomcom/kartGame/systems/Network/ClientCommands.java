package tomcom.kartGame.systems.Network;

import java.util.List;

import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.math.Vector2;
import com.quantumreboot.ganet.Peer;

import tomcom.kartGame.systems.Network.DataContainer.CarData;
import tomcom.kartGame.systems.Network.DataContainer.InputData;
import tomcom.kartGame.systems.Network.DataContainer.SpawnData;

public interface ClientCommands {
	public Signal<Integer> onConnected = new Signal<Integer>();
	public Signal<CarData> onCarDataReceived = new Signal<CarData>();
	public Signal<Integer> loadLevelReceived = new Signal<Integer>();
	public Signal<Vector2> onInputReceived = new Signal<Vector2>();
	public Signal<SpawnData> onSpawn = new Signal<SpawnData>();
	public void connected(Peer peer);
	public void sendInputData(Peer peer, InputData input);
	public void receiveLevelData(int levelID);
	public void receiveStartRace();
	public void receiveSpawnData(SpawnData spawnData);
	public void receiveEndRace(int winnerId);
	public void receiveCarData(CarData spawnData);
}
