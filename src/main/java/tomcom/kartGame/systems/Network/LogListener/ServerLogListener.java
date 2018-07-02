package tomcom.kartGame.systems.Network.LogListener;


import java.nio.ByteBuffer;

import com.badlogic.gdx.Gdx;
import com.quantumreboot.ganet.LogLevel;
import com.quantumreboot.ganet.MessageDecoder;
import com.quantumreboot.ganet.MessageEncoder;
import com.quantumreboot.ganet.MessageQuality;
import com.quantumreboot.ganet.Peer;

import tomcom.kartGame.systems.Network.Server;
import tomcom.kartGame.systems.Network.ServerSystem;


public class ServerLogListener extends LogNetworkListener {

	private MessageDecoder decoder = new MessageDecoder();
	private MessageEncoder encoder = new MessageEncoder();
	private ServerSystem serverSystem;
	
	public ServerLogListener(LogLevel level, ServerSystem serverSystem) {
		super(level);
		this.serverSystem = serverSystem;
	}

	@Override
	public void onMessage(Peer peer, ByteBuffer msg) {
		decoder.reset(msg);
		
		int type = decoder.readByte();
//		Gdx.app.log("Server","Got message from " + peer.getAddr()+" type: " +type);
		switch (type) {
			case 0:	serverSystem.receivedInputData(decoder.readInt(), decoder.readFloat(), decoder.readFloat());break;
			case 1:	serverSystem.receivedLevelDataReceived(peer);break;
		}
	}
	@Override
	public void onNewConnection(Peer peer) {
		
		Gdx.app.log("","Peer connected : " + peer.getAddr().toString());
		serverSystem.receivedNewConnection(peer);
	}

}