package tomcom.kartGame.systems.Network;


import java.nio.ByteBuffer;

import com.badlogic.gdx.Gdx;
import com.quantumreboot.ganet.LogLevel;
import com.quantumreboot.ganet.MessageDecoder;
import com.quantumreboot.ganet.MessageEncoder;
import com.quantumreboot.ganet.MessageQuality;
import com.quantumreboot.ganet.Peer;


public class ServerLogListener extends LogNetworkListener {

	private MessageDecoder decoder = new MessageDecoder();
	private MessageEncoder encoder = new MessageEncoder();
	private Server server;
	public ServerLogListener(LogLevel level, Server server) {
		super(level);
		this.server = server;
	}

	@Override
	public void onMessage(Peer peer, ByteBuffer msg) {
		decoder.reset(msg);
		
		int type = decoder.readInt();
		Gdx.app.log("Server","Got message from " + peer.getAddr()+" type: " +type);
		switch (type) {
			case 0: break;
			case 1: Gdx.app.log("Server","Received ApplyForce Command");
			server.receivedApplyForceCommand(decoder.readInt(), decoder.readFloat(), decoder.readFloat());
			break;
		}
//		decoder.reset(msg);
//		String msgTxt = decoder.readString();
//		Gdx.app.log("Server","Got message from \"" + peer.getAddr() + "\": '\" \r\n" +  msgTxt + "\"'\")");
//		encoder.reset();
//		encoder.writeString(msgTxt);
//		peer.send(encoder.getMessage(), MessageQuality.UNRELIABLE);
	}
	@Override
	public void onNewConnection(Peer peer) {
		
		Gdx.app.log("","Peer connected : " + peer.getAddr().toString());
		server.newConnection(peer);
	}

}