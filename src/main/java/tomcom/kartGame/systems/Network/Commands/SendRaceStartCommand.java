package tomcom.kartGame.systems.Network.Commands;

import com.badlogic.gdx.Gdx;
import com.quantumreboot.ganet.MessageEncoder;
import com.quantumreboot.ganet.MessageQuality;
import com.quantumreboot.ganet.Peer;

import tomcom.kartGame.systems.Network.DataContainer.InputData;

public class SendRaceStartCommand extends Command{

	private Peer peer;
	private MessageEncoder encoder = new MessageEncoder();
	private MessageQuality quality;

	public SendRaceStartCommand(double time, Peer peer, MessageQuality quality) {
		super(time);
		this.peer = peer;
		this.quality = quality;
	}
	
	@Override
	public void execute() {
		Gdx.app.log("Client","Sending Race Started to " + peer.getAddr());
		encoder.reset();
		encoder.writeByte(3);// 3.... Race Started

		peer.send(encoder.getMessage(), this.quality);
	}
	
}
