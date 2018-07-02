package tomcom.kartGame.systems.Network.Commands;

import com.badlogic.gdx.Gdx;
import com.quantumreboot.ganet.MessageEncoder;
import com.quantumreboot.ganet.MessageQuality;
import com.quantumreboot.ganet.Peer;

public class SendLevelDataCommand extends Command {
	
	private int levelId;

	private Peer peer;
	private MessageEncoder encoder = new MessageEncoder();
	private MessageQuality quality;

	public SendLevelDataCommand(double time, Peer peer, int levelId, MessageQuality quality) {
		super(time);
		this.levelId = levelId;
		this.peer = peer;
		this.quality = quality;
		
	}
	
	@Override
	public void execute() {
		Gdx.app.log("Server","Sending LevelData " + this.quality + " to " + peer.getAddr());
		encoder.reset();
		encoder.writeByte(0); // 0... LevelData
		encoder.writeInt(levelId);
		peer.send(encoder.getMessage(), this.quality);
		
	}

}
