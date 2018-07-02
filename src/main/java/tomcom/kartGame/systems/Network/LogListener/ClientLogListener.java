package tomcom.kartGame.systems.Network.LogListener;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

import com.badlogic.gdx.Gdx;
import com.quantumreboot.ganet.LogLevel;
import com.quantumreboot.ganet.MessageDecoder;
import com.quantumreboot.ganet.Peer;

import tomcom.kartGame.systems.Network.Client;
import tomcom.kartGame.systems.Network.ClientSystem;
import tomcom.kartGame.systems.Network.DataContainer.CarData;
import tomcom.kartGame.systems.Network.DataContainer.SpawnData;

public class ClientLogListener extends LogNetworkListener {

	private AtomicBoolean running;
	private MessageDecoder decoder = new MessageDecoder();
	private ClientSystem clientSystem;
	public ClientLogListener(LogLevel level, AtomicBoolean running, ClientSystem clientSystem) {
		super(level);
		this.running = running;
		this.clientSystem = clientSystem;
	}

	private void terminate() {
		running.set(false);
	}
	
	@Override
	public void onConnectionAccepted(Peer peer) {
		super.onConnectionAccepted(peer);
		clientSystem.connected(peer);
		
	}
	
	@Override
	public void onPeerLost(Peer peer) {
		super.onPeerLost(peer);
		terminate();
	}

	@Override
	public void onPeerDisconnected(Peer peer) {
		super.onPeerDisconnected(peer);
		terminate();
	}

	@Override
	public void onConnectionRefused(Peer peer) {
		super.onConnectionRefused(peer);
		terminate();
	}
	
	@Override
	public void onMessage(Peer peer, ByteBuffer msg) {
		decoder.reset(msg);
		int type = decoder.readByte();
//		Gdx.app.log("Client","Got message from " + peer.getAddr()+" Type: "+type);
		
		switch (type) {
			case 0: Gdx.app.log("Client","Got LevelData: " );clientSystem.receiveLevelData(decoder.readInt()); break;
			case 1: SpawnData spawnData = new SpawnData(decoder.readInt(),decoder.readFloat(),decoder.readFloat(), decoder.readFloat());
			spawnData.localControl = decoder.readInt() == 0 ? false : true;
			Gdx.app.log("Client","Got SpawnData for kart: " + spawnData.entityID);
			clientSystem.receiveSpawnData(spawnData); break;
			case 2: clientSystem.receiveCarData(new CarData(decoder.readInt(),decoder.readFloat(),decoder.readFloat(),decoder.readFloat()));break;
			case 3: clientSystem.receiveStartRace();break;
			case 4: clientSystem.receiveEndRace(decoder.readInt());break;
		}
	}
	
}
