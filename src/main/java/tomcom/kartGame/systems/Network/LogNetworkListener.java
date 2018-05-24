package tomcom.kartGame.systems.Network;

import java.nio.ByteBuffer;

import com.badlogic.gdx.Gdx;
import com.quantumreboot.ganet.LogLevel;
import com.quantumreboot.ganet.NetworkListener;
import com.quantumreboot.ganet.Peer;

public class LogNetworkListener implements NetworkListener {

	private static final LogLevel DEFAULT_LOG_LEVEL = LogLevel.INFO;
	private LogLevel curLevel;

	public LogNetworkListener() {
		this(DEFAULT_LOG_LEVEL);
	}
	
	public LogNetworkListener(LogLevel logLevel) {
		this.curLevel = logLevel;
	}

	@Override
	public void onNewConnection(Peer peer) {
		Gdx.app.log("","Peer connected: " + peer.getAddr().toString());
	}

	@Override
	public void onPeerLost(Peer peer) {
		Gdx.app.log("","Peer lost: " + peer.getAddr().toString());
	}

	@Override
	public void onPeerDisconnected(Peer peer) {
		Gdx.app.log("","Peer disconnected: " + peer.getAddr().toString());
	}

	@Override
	public void onConnectionAccepted(Peer peer) {
		Gdx.app.log("","Connected accepted from: " + peer.getAddr().toString());
	}

	@Override
	public void onConnectionRefused(Peer peer) {
		Gdx.app.log("","Connected refused from: " + peer.getAddr().toString());
	}

	@Override
	public void onMessage(Peer peer, ByteBuffer msg) {
		Gdx.app.log("","got message from " + peer.getAddr().toString());
	}

	@Override
	public void onError(String errorMessage) {
		Gdx.app.log("","network error " + errorMessage);
	}

	@Override
	public void onLog(LogLevel level, String msg) {
		if (level.getValue() <= curLevel.getValue()) {
			Gdx.app.log("","[" + level.toString() + "]: " + msg);
		}
	}

	@Override
	public void onExtMessage(Peer peer, ByteBuffer msg) {
		// intentionally left empty00Ã¼	 ,,++-
	}
	
}
