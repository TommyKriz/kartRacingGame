package tomcom.kartGame.systems.Network.Commands;

import com.badlogic.gdx.Gdx;
import com.quantumreboot.ganet.MessageEncoder;
import com.quantumreboot.ganet.MessageQuality;
import com.quantumreboot.ganet.Peer;

import tomcom.kartGame.systems.Network.DataContainer.InputData;

public class SendForceCommand extends Command {


	private InputData inputData;
	private Peer peer;
	private MessageEncoder encoder = new MessageEncoder();
	private MessageQuality quality;

	public SendForceCommand(double time, Peer peer, InputData inputData, MessageQuality quality) {
		super(time);
		this.inputData = inputData;
		this.peer = peer;
		this.quality = quality;
	}
	
	@Override
	public void execute() {
		Gdx.app.log("Client","Sending Inputs to " + peer.getAddr());
		Gdx.app.log("Client",inputData.entityID +" "+ inputData.xAxis+ " "+inputData.yAxis);
		encoder.reset();
		encoder.writeInt(1);// 1.... ForceInput
		encoder.writeInt(inputData.entityID);
		encoder.writeFloat(inputData.xAxis);
		encoder.writeFloat(inputData.yAxis);
		peer.send(encoder.getMessage(), this.quality);
	}
	
}
