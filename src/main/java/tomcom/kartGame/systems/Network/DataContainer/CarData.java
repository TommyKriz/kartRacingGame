package tomcom.kartGame.systems.Network.DataContainer;

public class CarData extends NetworkTransferData {

	public float xPos;
	public float yPos;
	public float rot;

	public CarData(int entityId, float xPos, float yPos, float rot) {
		super(entityId);
		this.xPos = xPos;
		this.yPos = yPos;
		this.rot = rot;
	}
}
