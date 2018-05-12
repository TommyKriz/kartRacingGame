package tomcom.kartGame.components.vehicle;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;

public class VehicleComponent implements Component {

	private Array<Wheel> wheels;

	public VehicleComponent() {
		wheels = new Array<Wheel>();
	}

	public VehicleComponent addWheel(Wheel w) {
		wheels.add(w);
		return this;
	}

	public Array<Wheel> getWheels() {
		return wheels;
	}

}