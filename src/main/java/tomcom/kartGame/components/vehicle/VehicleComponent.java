package tomcom.kartGame.components.vehicle;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;

public class VehicleComponent implements Component {

	private Array<Wheel> wheels;
	private Array<Wheel> drivenWheels;
	private Array<Wheel> steerableWheels;

	public VehicleComponent() {
		wheels = new Array<>();
		drivenWheels = new Array<>();
		steerableWheels = new Array<>();
	}

	public VehicleComponent addWheel(Wheel w) {
		if (w.steerable) {
			steerableWheels.add(w);
		} else {
			drivenWheels.add(w);
		}
		wheels.add(w);
		return this;
	}

	public Array<Wheel> getWheels() {
		return wheels;
	}

	public Array<Wheel> getDrivenWheels() {
		return drivenWheels;
	}

	public Array<Wheel> getSteerableWheels() {
		return steerableWheels;
	}

}