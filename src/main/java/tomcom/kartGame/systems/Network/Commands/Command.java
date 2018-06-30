package tomcom.kartGame.systems.Network.Commands;

import java.util.Comparator;

public abstract class Command {
	public double time;
	
	public Command(double time) {
		this.time = time;
	}
	
	public abstract void execute();
}


