package tomcom.kartGame.systems.Network;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

import com.badlogic.gdx.Gdx;
import com.quantumreboot.ganet.LogLevel;
import com.quantumreboot.ganet.MessageDecoder;
import com.quantumreboot.ganet.Peer;

class ClientLogListener extends LogNetworkListener {

	private AtomicBoolean running;
	private MessageDecoder decoder = new MessageDecoder();
	private Client client;
	public ClientLogListener(LogLevel level, AtomicBoolean running, Client client) {
		super(level);
		this.running = running;
		this.client = client;
	}

	private void terminate() {
		running.set(false);
	}
	
	@Override
	public void onConnectionAccepted(Peer peer) {
		super.onConnectionAccepted(peer);
		Client.peer = peer;
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
		Gdx.app.log("Client","Got message from " + peer.getAddr());
		int type = decoder.readByte();
		switch (type) {
			case 0: client.receiveSpawnCommand(decoder.readInt(),decoder.readFloat(), decoder.readFloat(), decoder.readInt() == 1? true : false); break;
			case 2: 
			client.receiveVehicleDataUpdate(decoder.readInt(),decoder.readFloat(), decoder.readFloat(),decoder.readFloat(), decoder.readFloat(),decoder.readInt(), decoder.readInt());
			break;
		}
		

	}
	
}
