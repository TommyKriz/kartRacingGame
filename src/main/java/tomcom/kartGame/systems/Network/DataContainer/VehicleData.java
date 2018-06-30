package tomcom.kartGame.systems.Network.DataContainer;

public class VehicleData extends NetworkTransferData {

	public float xVel;
	public float yVel;
	public float xPos;
	public float yPos;
	public int xRot;
	public int yRot;

	public VehicleData(int entityId, float xPos, float yPos, float xVel, float yVel, int xRot, int yRot) {
		super(entityId);
		this.xPos = xPos;
		this.yPos = yPos;
		this.xVel = xVel;
		this.yVel = yVel;
		this.xRot = xRot;
		this.yRot = yRot;
	}
}
