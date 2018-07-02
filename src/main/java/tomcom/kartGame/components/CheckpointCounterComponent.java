package tomcom.kartGame.components;

import com.badlogic.ashley.core.Component;

public class CheckpointCounterComponent implements Component {
	
	public int passedCheckpoints;

	public CheckpointCounterComponent() {
		passedCheckpoints = 0;
	}

	public void increment() {
		passedCheckpoints++;
	}

}
