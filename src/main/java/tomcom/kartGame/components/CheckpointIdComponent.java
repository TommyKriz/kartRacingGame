package tomcom.kartGame.components;

import com.badlogic.ashley.core.Component;

public class CheckpointIdComponent implements Component {

	private int id;

	public CheckpointIdComponent(int id) {
		this.id = id;
	}

	public boolean equals(int id) {
		return this.id == id;
	}

}
