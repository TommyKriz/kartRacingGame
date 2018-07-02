package tomcom.kartGame.systems.Network.DataContainer;

import com.badlogic.ashley.core.Entity;

public class SpawnData extends NetworkTransferData{

	public float x;
	public float y;
	public float rot;
	public boolean localControl;
	public SpawnData(int entityId, float x, float y, float rot) {
		super(entityId);
		this.x = x;
		this.y = y;
		this.rot = rot;
	}
}
