package tomcom.kartGame.systems.Network.Commands;

import com.badlogic.gdx.Gdx;
import com.quantumreboot.ganet.MessageEncoder;
import com.quantumreboot.ganet.MessageQuality;
import com.quantumreboot.ganet.Peer;

import tomcom.kartGame.systems.Network.DataContainer.SpawnData;

public class SendSpawnCommand extends Command {

	private SpawnData spawnData;

	private Peer peer;
	private MessageEncoder encoder = new MessageEncoder();
	private MessageQuality quality;

	public SendSpawnCommand(double time, Peer peer, SpawnData spawnData, MessageQuality quality) {
		super(time);
		this.spawnData = spawnData;
		this.peer = peer;
		this.quality = quality;
		
	}
	
	@Override
	public void execute() {
		Gdx.app.log("Server","Sending SpawnData " + this.quality + " to " + peer.getAddr());
		encoder.reset();
		encoder.writeByte(0);
		encoder.writeInt(spawnData.entityID);
		encoder.writeFloat(spawnData.x);
		encoder.writeFloat(spawnData.y);
		encoder.writeInt(spawnData.localControl? 1:0);
		peer.send(encoder.getMessage(), this.quality);
		
	}
	
}
