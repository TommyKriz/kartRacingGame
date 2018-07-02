package tomcom.kartGame.systems.Network.Commands;

import com.badlogic.gdx.Gdx;
import com.quantumreboot.ganet.MessageEncoder;
import com.quantumreboot.ganet.MessageQuality;
import com.quantumreboot.ganet.Peer;

public class SendReceivedLevelData extends Command{


	private Peer peer;
	private MessageEncoder encoder = new MessageEncoder();
	private MessageQuality quality;

	public SendReceivedLevelData(double time, Peer peer,  MessageQuality quality) {
		super(time);
		this.peer = peer;
		this.quality = quality;
		
	}
	
	@Override
	public void execute() {
		Gdx.app.log("Server","Sending LevelData " + this.quality + " to " + peer.getAddr());
		encoder.reset();
		encoder.writeByte(1); // 1... Received LevelData
		peer.send(encoder.getMessage(), this.quality);
		
	}
}
