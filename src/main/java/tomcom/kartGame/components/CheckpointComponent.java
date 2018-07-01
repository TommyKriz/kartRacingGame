package tomcom.kartGame.components;

import com.badlogic.ashley.core.Component;

public class CheckpointComponent implements Component {

	private int id;

	private boolean active;

	public CheckpointComponent(int id) {
		this.id = id;
		active = true;
	}

	public boolean equals(int id) {
		return this.id == id;
	}

	public void disable() {
		active = false;
	}

	public boolean isActive() {
		return active;
	}

}
