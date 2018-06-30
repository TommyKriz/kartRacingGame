package tomcom.kartGame.systems.Network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.badlogic.gdx.Gdx;
import com.quantumreboot.ganet.LogLevel;
import com.quantumreboot.ganet.MessageDecoder;
import com.quantumreboot.ganet.MessageEncoder;
import com.quantumreboot.ganet.MessageQuality;
import com.quantumreboot.ganet.Network;
import com.quantumreboot.ganet.Peer;

import tomcom.kartGame.systems.Network.Commands.Command;
import tomcom.kartGame.systems.Network.Commands.CommandComparator;
import tomcom.kartGame.systems.Network.Commands.ConnectCommand;
import tomcom.kartGame.systems.Network.Commands.SendForceCommand;
import tomcom.kartGame.systems.Network.DataContainer.ForceInputData;

public class Client {

	public static double time;	
	public static Peer peer;
	
	private static final int DEFAULT_PORT = 54321;
	private static final String DEFAULT_HOST = "localhost";//192.168.0.118
	
	private static AtomicBoolean running = new AtomicBoolean();
	private static AtomicBoolean shuttingDown = new AtomicBoolean();
	
	private static Queue<Command> commands = new PriorityQueue<>(11, new CommandComparator());
	
	private Network network;
	private NetworkingSystem networkingSystem;
	
	public Client (NetworkingSystem networkingSystem) {
		this.networkingSystem = networkingSystem;
		printHeader();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				shutdown();
			} });
	}
	
	public void Start() throws IOException {
		
		running.set(true);
		shuttingDown.set(false);
		
		network = new Network();
		network.addListener(new ClientLogListener(LogLevel.INFO, running, this));
		network.startup();
		commands.add(new ConnectCommand(2.0, network, new InetSocketAddress(DEFAULT_HOST, DEFAULT_PORT)));
		
		Gdx.app.log("Client","\nClient started");
	}
	

	
	private static void printHeader() {
		Gdx.app.log("Client","Echo Client using Ganet for Java");
		Gdx.app.log("Client","Copyright (c) 2015 Quantum Reboot.");
		Gdx.app.log("Client"," All rights reserved.");
	}

	private static void shutdown() {
		if (running.get()) {
			running.set(false);
			shuttingDown.set(true);
			while (shuttingDown.get()) {
				// busy wait
			}
		}
	}	
	
	private static void processCommands() {
		while (!commands.isEmpty() && time >= commands.peek().time) {
			Command cmd = commands.poll();
			cmd.execute();
		}
	}
	
	public void update(float deltaTime) {
		time+=deltaTime;
		if(running.get()) {
			network.update(deltaTime);
			processCommands();
		}
		else {
		}
		
	}
	
	public void cleanup() throws IOException {
		network.shutdown();
		Gdx.app.log("Client","\nClient terminated");
		shuttingDown.set(false);
	}
	
	public void createApplyForceCommand(Peer peer, double delay, ForceInputData input) {
		commands.add(new SendForceCommand(time + delay, 
				peer, input, MessageQuality.RELIABLE_ORDERED));
	}
	public void receiveSpawnCommand(int entityID, float x, float y, boolean localControl) {
		Gdx.app.log("Client","SpawnInfo RECEIVED!");
		networkingSystem.receiveSpawnInfo(entityID, x,y, localControl);
	}
	public void receiveVehicleDataUpdate(int entityID, float xPos, float yPos, float xVel, float yVel,
			int xRot, int yRot) {
		networkingSystem.receiveVehicleData(entityID, xPos, yPos,xVel, yVel, xRot, yRot);
		Gdx.app.log("Client","Received Position for Entity: "+entityID+" Pos: " + xPos +" "+yPos);
		
	}
}






