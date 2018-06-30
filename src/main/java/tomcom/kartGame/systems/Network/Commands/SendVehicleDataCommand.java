package tomcom.kartGame.systems.Network.Commands;

import com.badlogic.gdx.Gdx;
import com.quantumreboot.ganet.MessageEncoder;
import com.quantumreboot.ganet.MessageQuality;
import com.quantumreboot.ganet.Peer;

import tomcom.kartGame.systems.Network.DataContainer.VehicleData;

public class SendVehicleDataCommand extends Command {

	private VehicleData vehicleData;

	private Peer peer;
	private MessageEncoder encoder = new MessageEncoder();
	private MessageQuality quality;

	public SendVehicleDataCommand(double time, Peer peer, VehicleData vehicleData, MessageQuality quality) {
		super(time);
		this.vehicleData = vehicleData;
		this.peer = peer;
		this.quality = quality;
		
	}
	
	@Override
	public void execute() {
		Gdx.app.log("Sercer","Sending VehicleData " + this.quality + " to " + peer.getAddr());
		encoder.reset();
		encoder.writeByte(2);
		encoder.writeInt(vehicleData.entityID);
		encoder.writeFloat(vehicleData.xPos);
		encoder.writeFloat(vehicleData.yPos);
		encoder.writeFloat(vehicleData.xVel);
		encoder.writeFloat(vehicleData.yVel);
		encoder.writeInt(vehicleData.xRot);
		encoder.writeInt(vehicleData.yRot);
		peer.send(encoder.getMessage(), this.quality);
		
	}
	
}
