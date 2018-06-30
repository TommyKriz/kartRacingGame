package tomcom.kartGame.systems.Network.Commands;

import java.net.InetSocketAddress;

import com.badlogic.gdx.Gdx;
import com.quantumreboot.ganet.Network;

public class ConnectCommand extends Command {
	
	private Network network;
	private InetSocketAddress addr;
	
	public ConnectCommand(double time, Network network, InetSocketAddress addr) {
		super(time);
		this.network = network;
		this.addr = addr;
	}

	@Override
	public void execute() {
		Gdx.app.log("Client","Echo Client attempts to connect to " + addr);
		network.connect(addr);
	}
}