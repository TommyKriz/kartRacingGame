package tomcom.kartGame.systems.Network.Commands;

import java.util.Comparator;

public class CommandComparator implements Comparator<Command> {

	@Override
	public int compare(Command c1, Command c2) {
		return Double.compare(c1.time, c2.time);
	}
	
}