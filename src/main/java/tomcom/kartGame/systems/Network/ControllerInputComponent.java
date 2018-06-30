package tomcom.kartGame.systems.Network;

import com.badlogic.ashley.core.Component;

public class ControllerInputComponent implements Component {

	// wheel turn
	private float xAxis;

	// gas / brake
	private float yAxis;

	public void updateInput(float xAxis, float yAxis) {
		this.xAxis = xAxis;
		this.yAxis = yAxis;
	}

	public float getxAxis() {
		return xAxis;
	}

	public float getyAxis() {
		return yAxis;
	}

}
