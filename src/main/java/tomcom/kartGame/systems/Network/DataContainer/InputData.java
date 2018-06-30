package tomcom.kartGame.systems.Network.DataContainer;

public class InputData extends NetworkTransferData {

	public float xAxis;
	public float yAxis;
	
	public InputData(int entityID, float xAxis, float yAxis) {
		super(entityID);
		this.xAxis = xAxis;
		this.yAxis = yAxis;
	}
}
