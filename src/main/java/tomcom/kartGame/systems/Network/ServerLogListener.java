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
	
	public ServerLogListener(LogLevel level) {
		super(level);
	}

	@Override
	public void onMessage(Peer peer, ByteBuffer msg) {
		decoder.reset(msg);
		String msgTxt = decoder.readString();
		Gdx.app.log("Server","Got message from \"" + peer.getAddr() + "\": '\" \r\n" +  msgTxt + "\"'\")");
		encoder.reset();
		encoder.writeString(msgTxt);
		peer.send(encoder.getMessage(), MessageQuality.UNRELIABLE);
	}

}