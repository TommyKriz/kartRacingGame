package tomcom.kartGame.systems.Network.Commands;

import com.badlogic.gdx.Gdx;
import com.quantumreboot.ganet.MessageEncoder;
import com.quantumreboot.ganet.MessageQuality;
import com.quantumreboot.ganet.Peer;

import tomcom.kartGame.systems.Network.DataContainer.CarData;

public class SendCarDataCommand extends Command {

	private CarData carData;

	private Peer peer;
	private MessageEncoder encoder = new MessageEncoder();
	private MessageQuality quality;

	public SendCarDataCommand(double time, Peer peer, CarData vehicleData, MessageQuality quality) {
		super(time);
		this.carData = vehicleData;
		this.peer = peer;
		this.quality = quality;
		
	}
	
	@Override
	public void execute() {
//		Gdx.app.log("Server","Sending CarData " + this.quality + " to " + peer.getAddr());
		encoder.reset();
		encoder.writeByte(2);
		encoder.writeInt(carData.entityID);
		encoder.writeFloat(carData.xPos);
		encoder.writeFloat(carData.yPos);
		encoder.writeFloat(carData.rot);
		peer.send(encoder.getMessage(), this.quality);
		
	}
	
}
