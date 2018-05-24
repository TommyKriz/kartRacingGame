package tomcom.kartGame.systems.Network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.quantumreboot.ganet.LogLevel;
import com.quantumreboot.ganet.MessageDecoder;
import com.quantumreboot.ganet.MessageEncoder;
import com.quantumreboot.ganet.MessageQuality;
import com.quantumreboot.ganet.Network;
import com.quantumreboot.ganet.NetworkListener;
import com.quantumreboot.ganet.Peer;

public class NetworkingSystem extends EntitySystem{

	private boolean isServer;
	private Server server=null;
	private Client client=null;
	public NetworkingSystem(boolean isServer) {
		super();
		this.isServer = isServer;
	}

	@Override
	public void addedToEngine(Engine engine) {
		// TODO Auto-generated method stub
		super.addedToEngine(engine);
		if(isServer) {
			server=new Server();
			try {
				server.Start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			client = new Client();
			try {
				client.Start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if(server!=null)
			server.update(deltaTime);
		if(client!=null)
			try {
				client.update(deltaTime);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	@Override
	public void removedFromEngine(Engine engine) {
		// TODO Auto-generated method stub
		super.removedFromEngine(engine);
		try {
			if(server!=null)
				server.cleanup();
			if(client!=null)
				client.cleanup();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}


