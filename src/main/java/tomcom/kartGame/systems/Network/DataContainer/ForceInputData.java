package tomcom.kartGame.systems.Network.DataContainer;

public class ForceInputData extends NetworkTransferData {

	public float xAxis;
	public float yAxis;
	
	public ForceInputData(int entityID, float xAxis, float yAxis) {
		super(entityID);
		this.xAxis = xAxis;
		this.yAxis = yAxis;
	}
}
